package com.yuneec.image;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Timer;
import java.util.TimerTask;

public class ToastUtil {
    private static Stage stage=new Stage();
    private static Label label=new Label();
    static {
        stage.initStyle(StageStyle.TRANSPARENT);
    }
    public static void toast(String msg) {
        toast(msg,3000);
    }

    public static void toast(String msg, int time) {
        label.setText(msg);
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->stage.close());
            }
        };
        init(msg);
        Timer timer=new Timer();
        timer.schedule(task,time);
        stage.show();
    }

    private static void init(String msg) {
        Label label=new Label(msg);
        label.setStyle("-fx-background: rgba(56,56,56,0.7);-fx-border-radius: 25;-fx-background-radius: 25");
        label.setTextFill(Color.rgb(225,255,226));
        label.setPrefHeight(50);
        label.setPadding(new Insets(15));
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font(20));
        Scene scene=new Scene(label);
        scene.setFill(null);
        Screen screen = Screen.getPrimary();
        Rectangle2D r2 = screen.getVisualBounds();
//        stage.setWidth(r2.getWidth());
//        stage.setHeight(r2.getHeight());
        stage.setX(r2.getWidth()/2 - 240);
        stage.setY(r2.getHeight()/2 + 100);
        stage.setScene(scene);
    }
}



