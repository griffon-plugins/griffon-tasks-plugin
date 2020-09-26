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

import griffon.annotations.core.Nonnull;
import griffon.core.artifact.GriffonModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.kordamp.jipsy.ServiceProviderFor;

import static javafx.collections.FXCollections.observableArrayList;

@ServiceProviderFor(GriffonModel.class)
public class AppModel extends AbstractGriffonModel {
    private IntegerProperty count;
    private DoubleProperty progress;
    private BooleanProperty running;
    private ObservableList<Integer> primes = observableArrayList();

    @Nonnull
    public final IntegerProperty countProperty() {
        if (count == null) {
            count = new SimpleIntegerProperty(this, "count", 5);
        }
        return count;
    }

    public int getCount() {
        return countProperty().get();
    }

    public void setCount(int count) {
        countProperty().set(count);
    }

    @Nonnull
    public DoubleProperty progressProperty() {
        if (progress == null) {
            progress = new SimpleDoubleProperty(this, "progress", 0d);
        }
        return progress;
    }

    public double getProgress() {
        return progressProperty().get();
    }

    public void setProgress(double progress) {
        progressProperty().set(progress);
    }

    public BooleanProperty runningProperty() {
        if (running == null) {
            running = new SimpleBooleanProperty(this, "running", false);
        }
        return running;
    }

    public boolean isRunning() {
        return runningProperty().get();
    }

    public void setRunning(boolean running) {
        runningProperty().set(running);
    }

    @Nonnull
    public ObservableList<Integer> getPrimes() {
        return primes;
    }
}