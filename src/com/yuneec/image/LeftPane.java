package com.yuneec.image;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;

public class LeftPane {

    private static LeftPane instance;
    public static LeftPane getInstance() {
        if (instance == null) {
            instance = new LeftPane();
        }
        return instance;
    }

    public void init() {
        initLeftPane();
    }

    private FlowPane leftPane;
    public Label fileNameLable;
    private void initLeftPane() {
        leftPane = new FlowPane();
        leftPane.setPrefWidth(Configs.LeftPanelWidth);
        leftPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        Global.hBox.getChildren().add(leftPane);
//		addDragFileToLeftPane();
        Pane pane1 = new Pane();
        pane1.setPrefHeight(Configs.LineHeight);
        pane1.setPrefWidth(Configs.LeftPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        pane1.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));

        fileNameLable = new Label();
        fileNameLable.setTranslateX(10);
        fileNameLable.setTranslateY(10);
        fileNameLable.setTextFill(Color.WHITE);
        pane1.getChildren().add(fileNameLable);

        pane1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
                System.out.println("fileNameLable MouseClicked:" + s);
            }
        });

        leftPane.getChildren().add(pane1);
    }

    private void addDragFileToLeftPane() {
        leftPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != leftPane && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        leftPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    System.out.println("addDragFileToLeftPane:"+db.getFiles());
                    File file = db.getFiles().get(0);
                    String path = file.getAbsolutePath();
                    fileNameLable.setText(file.getName());
                    Global.currentOpenImagePath = path.replace("\\", "\\\\");
                    CenterPane.getInstance().showImage();
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }
}
