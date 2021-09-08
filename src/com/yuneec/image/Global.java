package com.yuneec.image;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Global {
    public static Stage primaryStage;
    public static HBox hBox;
    public static String currentOpenImagePath;
    public static String currentLeftSelectImagePath;
    public static double currentOpenImageWidth;
    public static double currentOpenImageHeight;
    public static boolean hasTemperatureBytes;

    public static String cameraMode = "";
    public static String cameraE20TMode = "E20T";
    public static String cameraE10TMode = "E10T";

    public static TemperatureUnit NowTemperatureUnit = TemperatureUnit.Celsius;
    public enum TemperatureUnit {
        Celsius,
        Fachrenheit,
        Kelvin
    }
}
