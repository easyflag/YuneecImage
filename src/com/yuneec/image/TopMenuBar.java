package com.yuneec.image;

import com.yuneec.image.module.custom.CustomParameters;
import com.yuneec.image.module.leftpane.LeftImagePathPane;
import com.yuneec.image.module.Language;
import com.yuneec.image.utils.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class TopMenuBar {

    public Menu fileMenu,settingsMenu;
    public MenuItem openFileMenuItem,openFolderMenuItem,exitMenuItem;
    public MenuItem reportMenuItem,saveImageMenuItem;
    public Menu TemperatureManeu,LanguageManeu;
    public RadioMenuItem EnglishMenuItem,ChineseMenuItem;
    public RadioMenuItem CelsiusMenuItem, FahrenheitMenuItem, KelvinMenuItem;
    public MenuItem customParametersMenuItem;
    public MenuItem aboutMenuItem;

    private static TopMenuBar instance;
    public static TopMenuBar getInstance() {
        if (instance == null) {
            instance = new TopMenuBar();
        }
        return instance;
    }

    public void init(BorderPane root) {
        initMenu(root);
        nowTemperatureUnitValue = SaveSettings.I().get(SaveSettings.temperatureUnit_KEY);
        TemperatureManeuClick(nowTemperatureUnitValue);
        nowLanguageValue = SaveSettings.I().get(SaveSettings.language_KEY);
        LanguageClick(nowLanguageValue);
    }

    private void initMenu(BorderPane root) {
        MenuBar menuBar = new MenuBar();
        menuBar.setPrefHeight(Configs.MenuHeight);
        menuBar.prefWidthProperty().bind(Global.primaryStage.widthProperty());
//		menuBar.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color),null,null)));
        root.setTop(menuBar);
        fileMenu = new Menu("Add File",new ImageView("image/file.png"));
//		fileMenu.setStyle("-fx-font-size:13px;");
        openFileMenuItem = new MenuItem("Open File\t\t");
        openFileMenuItem.setOnAction(actionEvent -> openFile());
        openFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+o"));
        openFolderMenuItem = new MenuItem("Open Folder\t\t");
        openFolderMenuItem.setOnAction(actionEvent -> openFolder());
        openFolderMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+f"));
        exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> exitSoft());
        fileMenu.getItems().addAll(openFileMenuItem,openFolderMenuItem, new SeparatorMenuItem(),exitMenuItem);

        settingsMenu = new Menu("Settings",new ImageView("image/setting.png"));
        reportMenuItem = new MenuItem("Create Report");
        reportMenuItem.setOnAction(actionEvent -> createReport());
        saveImageMenuItem = new MenuItem("Save Image");
        saveImageMenuItem.setOnAction(actionEvent -> saveImage());

        TemperatureManeu = new Menu("Temperature Unit");
        CelsiusMenuItem = new RadioMenuItem("Celsius(℃)");
        CelsiusMenuItem.setOnAction(actionEvent -> TemperatureManeuClick(1));
        FahrenheitMenuItem = new RadioMenuItem("Fahrenheit(℉)");
        FahrenheitMenuItem.setOnAction(actionEvent -> TemperatureManeuClick(2));
        KelvinMenuItem = new RadioMenuItem("Kelvin(K)");
        KelvinMenuItem.setOnAction(actionEvent -> TemperatureManeuClick(3));
        CelsiusMenuItem.setSelected(true);
        TemperatureManeu.getItems().addAll(CelsiusMenuItem,FahrenheitMenuItem,KelvinMenuItem);

        LanguageManeu = new Menu("Language");
        EnglishMenuItem = new RadioMenuItem("English");
        EnglishMenuItem.setOnAction(actionEvent -> LanguageClick(1));
        ChineseMenuItem = new RadioMenuItem("Chinese");
        ChineseMenuItem.setOnAction(actionEvent -> LanguageClick(2));
        EnglishMenuItem.setSelected(true);
        LanguageManeu.getItems().addAll(EnglishMenuItem,ChineseMenuItem);

        customParametersMenuItem = new MenuItem("Custom Parameters");
        customParametersMenuItem.setOnAction(actionEvent -> customParameters());

        aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(actionEvent -> about());

        settingsMenu.getItems().addAll(reportMenuItem,saveImageMenuItem,
                TemperatureManeu,LanguageManeu,customParametersMenuItem,aboutMenuItem);
        Menu helpMenu = new Menu("Help");
        RadioMenuItem helpItem = new RadioMenuItem("Help");
        helpMenu.getItems().addAll(helpItem, new SeparatorMenuItem());
        menuBar.getMenus().addAll(fileMenu, settingsMenu);

    }

    public void exitSoft() {
        boolean result = YDialog.showConfirmDialog(Language.getString(
                "Confirm exit Software !",
                "确认退出程序 !"));
        if (result){
            SaveSettings.I().save(SaveSettings.temperatureUnit_KEY,nowTemperatureUnitValue);
            SaveSettings.I().save(SaveSettings.language_KEY,nowLanguageValue);
            Platform.exit();
        }
    }

    private int nowTemperatureUnitValue;
    private void TemperatureManeuClick(int flag) {
        nowTemperatureUnitValue = flag;
        if (flag == 1) {
            Global.NowTemperatureUnit = Global.TemperatureUnit.Celsius;
            CelsiusMenuItem.setSelected(true);
            FahrenheitMenuItem.setSelected(false);
            KelvinMenuItem.setSelected(false);
        } else if (flag == 2) {
            Global.NowTemperatureUnit = Global.TemperatureUnit.Fachrenheit;
            CelsiusMenuItem.setSelected(false);
            FahrenheitMenuItem.setSelected(true);
            KelvinMenuItem.setSelected(false);
        } else if (flag == 3) {
            Global.NowTemperatureUnit = Global.TemperatureUnit.Kelvin;
            CelsiusMenuItem.setSelected(false);
            FahrenheitMenuItem.setSelected(false);
            KelvinMenuItem.setSelected(true);
        }
        UnitTransitions.transitionTemperature();
    }

    private int nowLanguageValue;
    private void LanguageClick(int flag) {
        nowLanguageValue = flag;
        if (flag == 1) {
            Language.LanguageSelect = Language.Languages.English;
            EnglishMenuItem.setSelected(true);
            ChineseMenuItem.setSelected(false);
            Language.getInstance().setEnglish();
        }else if (flag == 2) {
            Language.LanguageSelect = Language.Languages.Chinese;
            ChineseMenuItem.setSelected(true);
            EnglishMenuItem.setSelected(false);
            Language.getInstance().setChinese();
        }
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All", "*.*"));
        File file = fileChooser.showOpenDialog(Global.primaryStage);
        if (file != null) {
            String path = file.getAbsolutePath();
            YLog.I("openFile:"+path);
//            LeftPane.getInstance().fileNameLable.setText(file.getName());
            LeftImagePathPane.getInstance().addImageItem(file.getName(),path);
        }
    }

    private void openFolder(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(Global.primaryStage);
        if (file != null) {
            String path = file.getPath();
            LeftImagePathPane.getInstance().addImagePath(path);
            YLog.I("openFolder:"+path);
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
        YLog.I("createReport:"+file);
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

    private void saveImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.JPG"),
                new FileChooser.ExtensionFilter("All", "*.*"));
        File file = fileChooser.showSaveDialog(Global.primaryStage);
        YLog.I("saveImage:"+file);
        if (file == null) {
            return;
        }
        try {
            SaveImage.I().save(file.toString().replace("\\", "\\\\"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void customParameters() {
        new CustomParameters().start(new Stage());
    }

    private void about() {
        YDialog.showInformationDialog(Language.getString(Language.About_en,Language.About_ch),
                (Language.getString(Configs.version_en,Configs.version_ch)) + "\n\n" +
                (Language.getString(Configs.copyright,Configs.copyright_ch)));
    }
}
