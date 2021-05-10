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

        Menu TemperatureManeu = new Menu("Temperature");
        CelsiusMenuItem = new RadioMenuItem("Celsius(℃)");
        CelsiusMenuItem.setOnAction(actionEvent -> TemperatureManeuClick(1));
        FachrenheitMenuItem = new RadioMenuItem("Fachrenheit(℉)");
        FachrenheitMenuItem.setOnAction(actionEvent -> TemperatureManeuClick(2));
        KelvinMenuItem = new RadioMenuItem("Kelvin(K)");
        KelvinMenuItem.setOnAction(actionEvent -> TemperatureManeuClick(3));
        CelsiusMenuItem.setSelected(true);
        TemperatureManeu.getItems().addAll(CelsiusMenuItem,FachrenheitMenuItem,KelvinMenuItem);

        settingsMenu.getItems().addAll(reportMenuItem,TemperatureManeu);
        Menu helpMenu = new Menu("Help");
        RadioMenuItem helpItem = new RadioMenuItem("Help");
        helpMenu.getItems().addAll(helpItem, new SeparatorMenuItem());
        menuBar.getMenus().addAll(fileMenu, settingsMenu);
    }

    RadioMenuItem CelsiusMenuItem, FachrenheitMenuItem, KelvinMenuItem;
    private void TemperatureManeuClick(int flag) {
        if (flag == 1) {
            Global.NowTemperatureUnit = Global.TemperatureUnit.Celsius;
            CelsiusMenuItem.setSelected(true);
            FachrenheitMenuItem.setSelected(false);
            KelvinMenuItem.setSelected(false);
        } else if (flag == 2) {
            Global.NowTemperatureUnit = Global.TemperatureUnit.Fachrenheit;
            CelsiusMenuItem.setSelected(false);
            FachrenheitMenuItem.setSelected(true);
            KelvinMenuItem.setSelected(false);
        } else if (flag == 3) {
            Global.NowTemperatureUnit = Global.TemperatureUnit.Kelvin;
            CelsiusMenuItem.setSelected(false);
            FachrenheitMenuItem.setSelected(false);
            KelvinMenuItem.setSelected(true);
        }
        CenterPane.getInstance().transitionTemperature();
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
            PdfReport.getInstance().creat(file.toString().replace("\\", "\\\\"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
