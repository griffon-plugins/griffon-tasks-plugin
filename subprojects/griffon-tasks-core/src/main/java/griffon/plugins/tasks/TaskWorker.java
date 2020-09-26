/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2020 The author and/or original authors.
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
package griffon.plugins.tasks;

import griffon.annotations.core.Nonnull;
import griffon.core.properties.PropertySource;
import griffon.plugins.tasks.util.UIThreadWorker;

/**
 * @author Andres Almiray
 */
public interface TaskWorker<V, C> extends PropertySource, UIThreadWorker<V, C> {
    Long getStartedTimestamp();

    Long getFinishTimestamp();

    @Nonnull
    String getPhase();

    void setPhase(@Nonnull String phase);

    @Nonnull
    Task<V, C> getTask();

    void setContext(@Nonnull TaskContext context);

    boolean isError();

    void publishProgress(int progress);

    void publishChunks(C... chunks);
}
