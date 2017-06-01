/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example;

import griffon.core.artifact.GriffonView;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import java.util.Collections;
import java.util.stream.Collectors;

import static javafx.beans.binding.Bindings.bindBidirectional;
import static javafx.beans.binding.Bindings.not;

@ArtifactProviderFor(GriffonView.class)
public class SampleView extends AbstractJavaFXGriffonView {
    private SampleController controller;
    private SampleModel model;

    @FXML private TextField count;
    @FXML private ProgressBar progressBar;
    @FXML private TextArea result;

    public void setController(SampleController controller) {
        this.controller = controller;
    }

    public void setModel(SampleModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String, Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setResizable(false);
        getApplication().getWindowManager().attach("mainWindow", stage);
    }

    // sample the UI
    private Scene init() {
        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);

        Node node = loadFromFXML();
        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);

        progressBar.progressProperty().bind(model.progressProperty());
        bindBidirectional(count.textProperty(), model.countProperty(), new NumberStringConverter());
        model.getPrimes().addListener((ListChangeListener<Integer>) change -> {
            result.setText(model.getPrimes().stream().map(String::valueOf).collect(Collectors.joining(", ")));
        });

        toolkitActionFor(controller, "start").enabledProperty().bind(not(model.runningProperty()));
        toolkitActionFor(controller, "cancel").enabledProperty().bind(model.runningProperty());

        return scene;
    }
}