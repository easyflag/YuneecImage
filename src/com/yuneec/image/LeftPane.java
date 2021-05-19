package com.yuneec.image;

import com.yuneec.image.views.RightKeyMenu;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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

    private Pane leftPane;
    public Label fileNameLable;
    private void initLeftPane() {
        leftPane = new Pane();
        leftPane.setPrefWidth(Configs.LeftPanelWidth);
        leftPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        Global.hBox.getChildren().add(leftPane);
//		addDragFileToLeftPane();
        Pane imageNamePane = new Pane();
        imageNamePane.setPrefHeight(Configs.LineHeight);
        imageNamePane.setPrefWidth(Configs.LeftPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        imageNamePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));

        fileNameLable = new Label();
        fileNameLable.setTranslateX(10);
        fileNameLable.setTranslateY(10);
        fileNameLable.setTextFill(Color.WHITE);
        imageNamePane.getChildren().add(fileNameLable);

        imageNamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
                System.out.println("fileNameLable MouseClicked:" + s);
            }
        });

        imageNamePane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                Node node = event.getPickResult().getIntersectedNode();
//                RightKeyMenu.getInstance().show(node, Side.RIGHT, -70, 10);
            }
        });

        leftPane.getChildren().add(imageNamePane);

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
