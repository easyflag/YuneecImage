package com.yuneec.image;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class TopMenuBar {
    private static TopMenuBar instance;
    public static TopMenuBar getInstance() {
        if (instance == null) {
            instance = new TopMenuBar();
        }
        return instance;
    }

    public void init(BorderPane root) {
        initMenu(root);
    }

    private void initMenu(BorderPane root) {
        MenuBar menuBar = new MenuBar();
        menuBar.setPrefHeight(Configs.MenuHeight);
        menuBar.prefWidthProperty().bind(Global.primaryStage.widthProperty());
//		menuBar.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color),null,null)));
        root.setTop(menuBar);
        Menu fileMenu = new Menu("Add File");
//		fileMenu.setStyle("-fx-font-size:13px;");
        MenuItem openFileMenuItem = new MenuItem("Open File");
        openFileMenuItem.setOnAction(actionEvent -> openFile());
        MenuItem openFolderMenuItem = new MenuItem("Open Folder");
        openFolderMenuItem.setOnAction(actionEvent -> openFolder());
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
        fileMenu.getItems().addAll(openFileMenuItem, new SeparatorMenuItem(),exitMenuItem);
        Menu settingsMenu = new Menu("Settings");
        MenuItem reportMenuItem = new MenuItem("Create Report");
        reportMenuItem.setOnAction(actionEvent -> createReport());
        settingsMenu.getItems().add(reportMenuItem);
        Menu helpMenu = new Menu("Help");
        RadioMenuItem helpItem = new RadioMenuItem("Help");
        helpMenu.getItems().addAll(helpItem, new SeparatorMenuItem());
        menuBar.getMenus().addAll(fileMenu, settingsMenu);
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All", "*.*"));
        File file = fileChooser.showOpenDialog(Global.primaryStage);
        if (file != null) {
            String path = file.getAbsolutePath();
            System.out.println("openFile:"+path);
            LeftPane.getInstance().fileNameLable.setText(file.getName());
            Global.currentOpenImagePath = path.replace("\\", "\\\\");
            CenterPane.getInstance().showImage();
        }
    }

    private void openFolder(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(Global.primaryStage);
        if (file != null) {
            String path = file.getPath();
            System.out.println(path);
        }
    }

    private void createReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("pdf", "*.pdf"),
                new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("txt", "*.txt"));
        File file = fileChooser.showSaveDialog(Global.primaryStage);
        System.out.println("createReport:"+file);
        if (file == null) {
            return;
        }
        try {
            PdfReport.getInstance().creat(file.toString().replace("\\", "\\\\"),Global.currentOpenImagePath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
