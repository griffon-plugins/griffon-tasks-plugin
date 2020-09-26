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

import griffon.annotations.inject.DependsOn;
import griffon.core.LifecycleHandler;
import griffon.core.env.Lifecycle;
import griffon.core.injection.Module;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.codehaus.griffon.runtime.util.ResourceBundleProvider;
import org.kordamp.jipsy.ServiceProviderFor;

import java.util.ResourceBundle;

import static griffon.util.AnnotationUtils.named;

@DependsOn("core")
@ServiceProviderFor(Module.class)
public class ApplicationModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        bind(LifecycleHandler.class)
            .withClassifier(named(Lifecycle.INITIALIZE.getName()))
            .to(Initialize.class)
            .asSingleton();

        bind(ResourceBundle.class)
            .withClassifier(named("applicationResourceBundle"))
            .toProvider(new ResourceBundleProvider("org.example.Config"))
            .asSingleton();
    }
}
