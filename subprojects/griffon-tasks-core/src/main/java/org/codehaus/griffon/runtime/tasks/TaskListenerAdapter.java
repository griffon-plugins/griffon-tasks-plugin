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
package org.codehaus.griffon.runtime.tasks;

import griffon.plugins.tasks.ChangeEvent;
import griffon.plugins.tasks.Task;
import griffon.plugins.tasks.TaskListener;

/**
 * @author <a href="mailto:eike.kettner@gmail.com">Eike Kettner</a>
 * @since 20.07.11 09:14
 */
public class TaskListenerAdapter implements TaskListener {
    public void stateChanged(ChangeEvent<Task.State> event) {
    }

    public void progressChanged(ChangeEvent<Integer> event) {
    }

    public void phaseChanged(ChangeEvent<String> event) {
    }
}
