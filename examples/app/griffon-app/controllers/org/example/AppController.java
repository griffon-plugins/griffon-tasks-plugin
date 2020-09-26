/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 The author and/or original authors.
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
package org.example;

import griffon.core.artifact.GriffonController;
import griffon.plugins.tasks.ChangeEvent;
import griffon.plugins.tasks.Task;
import griffon.plugins.tasks.TaskControl;
import griffon.plugins.tasks.TaskManager;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.codehaus.griffon.runtime.tasks.TaskListenerAdapter;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Inject;
import java.util.List;

@ServiceProviderFor(GriffonController.class)
public class AppController extends AbstractGriffonController {
    private AppModel model;
    private TaskControl<List<Integer>> taskControl;

    @Inject
    private TaskManager taskManager;

    public void setModel(AppModel model) {
        this.model = model;
    }

    public void start() {
        PrimeNumbersTask task = new PrimeNumbersTask(model.getCount(), model.getPrimes());
        taskControl = taskManager.create(task);
        taskControl.getContext().addListener(new TaskListenerAdapter() {
            @Override
            public void progressChanged(ChangeEvent<Integer> event) {
                model.setProgress(event.getNewValue() / 100d);
            }

            @Override
            public void stateChanged(ChangeEvent<Task.State> event) {
                switch (event.getNewValue()) {
                    case STARTED:
                        model.setRunning(true);
                        break;
                    case DONE:
                    case CANCELLED:
                    case FAILED:
                        model.setRunning(false);
                        taskControl = null;
                }
            }
        });
        taskControl.execute();
    }

    public void cancel() {
        if (model.isRunning()) {
            taskControl.cancel();
        }
    }
}