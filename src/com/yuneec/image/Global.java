package com.yuneec.image;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Global {
    public static Stage primaryStage;
    public static HBox hBox;
    public static String currentOpenImagePath;
    public static String currentLeftSelectImagePath;
    public static double currentOpenImageWidth = Configs.DefaultImageWidth;
    public static double currentOpenImageHeight = Configs.DefaultImageHeight;
    public static boolean hasTemperatureBytes;
    public static int imagePaletteType;
    public static float dzoom;

    public static String cameraMode = "";
    public static String cameraE20TMode = "E20T";
    public static String cameraE10TMode = "E10T";
    public static String cameraETxProMode = "ETx Pro";
    public static String cameraE20ProMode = "E20 Pro";

    public static TemperatureUnit NowTemperatureUnit = TemperatureUnit.Celsius;
    public enum TemperatureUnit {
        Celsius,
        Fachrenheit,
        Kelvin
    }
}
