package com.yuneec.image.demo;


import java.io.File;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageMaxMin extends Application {

    Point2D dragDistance = null;

    public void start(Stage primaryStage) {

        ImageView imageView = new ImageView();
        String file = "F:\\intellijSpace\\YuneecImage\\src\\image\\DJI_01.jpg";
        System.out.println(file);
        Image image = new Image("file:" + file);
        imageView.setImage(image);

        final double w = image.getWidth();
        final double h = image.getHeight();
        final double max = Math.max(w, h);
        final int width = (int) (500 * w / max);
        final int heigth = (int) (500 * h / max);
        imageView.setFitHeight(heigth);
        imageView.setFitWidth(width);

        Pane pane = new Pane();
        StackPane stackPane = new StackPane(pane);
        Scene scene = new Scene(stackPane, 700, 800);
        pane.getChildren().add(imageView);
        pane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                imageView.setY((newValue.doubleValue() - imageView.getFitHeight()) / 2);
            }

        });

        pane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                imageView.setX((newValue.doubleValue() - imageView.getFitWidth()) / 2);
            }

        });


        final double scale = 5;
        stackPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double rate = 0;
            if (event.getDeltaY() > 0) {
                rate = 0.05;
            } else {
                rate = -0.05;
            }
            double newWidth = imageView.getFitWidth() + w * rate;
            double newHeight = imageView.getFitHeight() + h * rate;
            if (newWidth <= width || newWidth > scale * width) {
                return;
            }

            Point2D eventPoint = new Point2D(event.getSceneX(), event.getSceneY());
            Point2D imagePoint = pane.localToScene(new Point2D(imageView.getX(), imageView.getY()));
            Rectangle2D imageRect = new Rectangle2D(imagePoint.getX(), imagePoint.getY(), imageView.getFitWidth(), imageView.getFitHeight());
            Point2D ratePoint;
            Point2D eventPointDistance;
            if (newWidth > scale / 4 * width && imageRect.contains(eventPoint)) {
                ratePoint = eventPoint.subtract(imagePoint);
                ratePoint = new Point2D(ratePoint.getX() / imageView.getFitWidth(), ratePoint.getY() / imageView.getFitHeight());
                eventPointDistance = pane.sceneToLocal(eventPoint);
            } else {
                ratePoint = new Point2D(0.5, 0.5);
                eventPointDistance = new Point2D(pane.getWidth() / 2,
                        pane.getHeight() / 2);
            }

            imageView.setX(eventPointDistance.getX() - newWidth * ratePoint.getX());
            imageView.setY(eventPointDistance.getY() - newHeight * ratePoint.getY());
            imageView.setFitWidth(newWidth);
            imageView.setFitHeight(newHeight);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
