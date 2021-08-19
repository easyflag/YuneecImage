package com.yuneec.image.demo;

import com.yuneec.image.Configs;
import javafx.application.Application;

import javafx.event.Event;
import javafx.event.EventHandler;

import javafx.scene.Cursor;
import javafx.scene.Scene;

import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;

import javafx.stage.Stage;


public class DrawOnCanvas extends Application {
    @Override

    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(400, 400);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            graphicsContext.stroke();

        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
            }
        });
        StackPane root = new StackPane();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 500, 500);
        root.setBackground(new Background(new BackgroundFill(Color.web(Configs.red_color), null, null)));
        primaryStage.setTitle("java-buddy.blogspot.com");

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        gc.setFill(Color.LIGHTGRAY);
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(5);
        gc.fill();
//        gc.strokeRect(0, 0, canvasWidth, canvasHeight);
//        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);

        gc.setLineWidth(1);

    }

}
