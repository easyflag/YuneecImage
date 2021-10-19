package com.yuneec.image.utils;

import com.yuneec.image.module.colorpalette.ColorPalette;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.module.point.PointTemperature;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ColorPickerManager {

    private String newColor;
    private static ColorPickerManager instance;

    public static ColorPickerManager I() {
        if (instance == null) {
            instance = new ColorPickerManager();
        }
        return instance;
    }

    public void setColor(String color){
        newColor = color;
        setColorPointTemperatureColor();
    }

    private void setColorPointTemperatureColor() {
        for (int i = 0; i < PointManager.getInstance().pointTemperatureNodeList.size(); i++) {
            PointTemperature pointTemperature = (PointTemperature) PointManager.getInstance().pointTemperatureNodeList.get(i);
            ArrayList pointNodeList = pointTemperature.getPointTemperatureNode();
            Label label = (Label) pointNodeList.get(0);
            label.setTextFill(Color.web(newColor));
        }
    }
}
