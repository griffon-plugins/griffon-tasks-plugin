/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2021 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.tasks.util;

import griffon.annotations.core.Nonnull;
import griffon.core.ExecutorServiceManager;
import griffon.core.threading.UIThreadManager;
import griffon.plugins.tasks.util.UIThreadWorker;
import org.codehaus.griffon.runtime.core.properties.AbstractPropertySource;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public abstract class AbstractUIThreadWorker<T, V> extends AbstractPropertySource implements UIThreadWorker<T, V> {
    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_STATE = "state";
    private static final ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    private final FutureTask<T> future;
    private volatile int progress;
    private volatile StateValue state;
    private ExecutorServiceManager executorServiceManager;
    private UIThreadManager uiThreadManager;

    public AbstractUIThreadWorker() {
        Callable<T> callable = () -> {
            setState(StateValue.STARTED);
            return doInBackground();
        };

        future = new FutureTask<T>(callable) {
            protected void done() {
                callDoneInsideUIThread();
                setState(StateValue.DONE);
            }
        };

        state = StateValue.PENDING;
    }

    @Inject
    public void setExecutorServiceManager(@Nonnull ExecutorServiceManager executorServiceManager) {
        requireNonNull(executorServiceManager, "Argument 'executorServiceManager' must not be bull");
        if (this.executorServiceManager != null) {
            this.executorServiceManager.remove(DEFAULT_EXECUTOR_SERVICE);
        }
        this.executorServiceManager = executorServiceManager;
        this.executorServiceManager.add(DEFAULT_EXECUTOR_SERVICE);
    }

    @Inject
    public void setUiThreadManager(@Nonnull UIThreadManager uiThreadManager) {
        this.uiThreadManager = requireNonNull(uiThreadManager, "Argument 'uiThreadManager' must not be null");
    }

    protected abstract T doInBackground() throws Exception;

    protected void process(List<V> chunks) {
        // empty
    }

    protected void done() {
        // empty
    }

    protected final void publish(V... chunks) {
        final List<V> args = new ArrayList<V>();
        Collections.addAll(args, chunks);

        uiThreadManager.executeInsideUISync(() -> process(args));
    }

    public final void run() {
        future.run();
    }

    public final int getProgress() {
        return progress;
    }

    protected final void setProgress(final int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Value should be from 0 to 100");
        }
        if (this.progress == progress) {
            return;
        }
        final int oldProgress = this.progress;
        this.progress = progress;
        if (!pcs.hasListeners(KEY_PROGRESS)) {
            return;
        }

        uiThreadManager.executeInsideUISync(() -> firePropertyChange(KEY_PROGRESS, oldProgress, progress));
    }

    @Nonnull
    public final StateValue getState() {
        return isDone() ? StateValue.DONE : state;
    }

    private void setState(@Nonnull StateValue state) {
        pcs.firePropertyChange(KEY_STATE, this.state, this.state = state);
    }

    public final void execute() {
        DEFAULT_EXECUTOR_SERVICE.submit(this);
    }

    public final boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    public final boolean isCancelled() {
        return future.isCancelled();
    }

    public final boolean isDone() {
        return future.isDone();
    }

    public final T get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    public final T get(long timeout, TimeUnit unit) throws InterruptedException,
        ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    private void callDoneInsideUIThread() {
        uiThreadManager.executeInsideUISync(this::done);
    }
}
