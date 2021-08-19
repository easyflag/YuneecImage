package com.yuneec.image.utils;

import com.yuneec.image.CenterPane;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.curve.CurveTemperature;

import java.util.ArrayList;

public class BackStepManager {

    public ArrayList temperatureInfoList = new ArrayList(); // include box and curve

    public static BackStepManager instance;

    public static BackStepManager getInstance() {
        if (instance == null) {
            instance = new BackStepManager();
        }
        return instance;
    }

    public void addTemperatureInfo(Temperature obj){
        temperatureInfoList.add(obj);
    }

    public void backStep() {
        if (!temperatureInfoList.isEmpty()) {
            int endIndex = temperatureInfoList.size() - 1;
            Temperature temperature = (Temperature) temperatureInfoList.get(endIndex);
            Temperature.TYPE type = temperature.type;
            if (type == Temperature.TYPE.BOX){
                BoxTemperature boxTemperature = (BoxTemperature) temperatureInfoList.get(endIndex);
                CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getTopLine(), boxTemperature.getBottomLine(),
                        boxTemperature.getLeftLine(), boxTemperature.getRightLine());
                CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeMax());
                CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeMin());
            } else if (type == Temperature.TYPE.CURVE){
                CurveTemperature curveTemperature = (CurveTemperature) temperatureInfoList.get(endIndex);
                CenterPane.getInstance().showImagePane.getChildren().removeAll(curveTemperature.getAllLine());
                CenterPane.getInstance().showImagePane.getChildren().removeAll(curveTemperature.getCurveTemperatureNodeMax());
                CenterPane.getInstance().showImagePane.getChildren().removeAll(curveTemperature.getCurveTemperatureNodeMin());
            }
            temperatureInfoList.remove(endIndex);
        }
    }


}
