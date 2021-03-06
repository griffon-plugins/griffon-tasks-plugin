/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2021 The author and/or original authors.
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

import griffon.annotations.core.Nonnull;
import griffon.util.AbstractMapResourceBundle;

import java.util.Map;

import static griffon.util.CollectionUtils.map;
import static java.util.Collections.singletonList;

public class Config extends AbstractMapResourceBundle {
    @Override
    protected void initialize(@Nonnull Map<String, Object> entries) {
        map(entries)
            .e("application", map()
                .e("title", "Prime Number Calculator")
                .e("startupGroups", singletonList("app"))
                .e("autoShutdown", true)
            )
            .e("mvcGroups", map()
                .e("app", map()
                    .e("model", "org.example.AppModel")
                    .e("view", "org.example.AppView")
                    .e("controller", "org.example.AppController")
                )
            );
    }
}