package com.yuneec.image.views;

import com.yuneec.image.Configs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DeveloperView extends Application {
    double windowWidth = 640;
    double windowHeight = 512;

    public void start(Stage primaryStage) {

        FlowPane anchorPane = new FlowPane();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
        Scene scene = new Scene(anchorPane, windowWidth, windowHeight);

        scene.setFill(Paint.valueOf(Configs.backgroundColor));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
