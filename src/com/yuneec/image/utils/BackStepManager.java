package com.yuneec.image.utils;

import com.yuneec.image.CenterPane;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.module.point.PointTemperature;

import java.util.ArrayList;

public class BackStepManager {

    public ArrayList temperatureInfoList = new ArrayList(); // include box and curve

    public static final int MAX_POINT_COUNT = 10;
    public static final int MAX_BOX_COUNT = 3;
    public static final int MAX_CURVE_COUNT = 3;
    public int currentCurveCount = 0;
    public int currentBoxCount = 0;

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
            if (type == Temperature.TYPE.POINT){
                PointTemperature pointTemperature = (PointTemperature) temperatureInfoList.get(endIndex);
                CenterPane.getInstance().showImagePane.getChildren().removeAll(pointTemperature.getPointTemperatureNode());
            } else if (type == Temperature.TYPE.BOX){
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

    public void backStep(Temperature.TYPE type) {
        if (!temperatureInfoList.isEmpty()) {
            int len = temperatureInfoList.size();
            for (int i = len - 1; i >= 0; i--) {
                Temperature temperature = (Temperature) temperatureInfoList.get(i);
                if (type == temperature.type){
                    if (type == Temperature.TYPE.POINT){
                        PointTemperature pointTemperature = (PointTemperature) temperature;
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(pointTemperature.getPointTemperatureNode());
                        PointManager.getInstance().pointTemperatureNodeList.remove(PointManager.getInstance().pointTemperatureNodeList.size()-1);
                    } else if (type == Temperature.TYPE.BOX){
                        BoxTemperature boxTemperature = (BoxTemperature) temperature;
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getTopLine(), boxTemperature.getBottomLine(),
                                boxTemperature.getLeftLine(), boxTemperature.getRightLine());
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeMax());
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeMin());
                        BoxTemperatureManager.getInstance().boxTemperatureList.remove(BoxTemperatureManager.getInstance().boxTemperatureList.size()-1);
                    } else if (type == Temperature.TYPE.CURVE){
                        CurveTemperature curveTemperature = (CurveTemperature) temperature;
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(curveTemperature.getAllLine());
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(curveTemperature.getCurveTemperatureNodeMax());
                        CenterPane.getInstance().showImagePane.getChildren().removeAll(curveTemperature.getCurveTemperatureNodeMin());
                        CurveManager.getInstance().curveTemperatureList.remove(CurveManager.getInstance().curveTemperatureList.size()-1);
                    }
                    temperatureInfoList.remove(i);
                    break;
                }
            }
        }
    }

    public int getCurrentPointCount() {
        int currentPointCount = 0;
        if (!temperatureInfoList.isEmpty()) {
            int len = temperatureInfoList.size();
            for (int i = len - 1; i >= 0; i--) {
                Temperature temperature = (Temperature) temperatureInfoList.get(i);
                if (temperature.type == Temperature.TYPE.POINT) {
                    currentPointCount++;
                }
            }
        }
        return currentPointCount;
    }

    public int getCurrentCurveCount() {
        currentCurveCount = 0;
        if (!temperatureInfoList.isEmpty()) {
            int len = temperatureInfoList.size();
            for (int i = len - 1; i >= 0; i--) {
                Temperature temperature = (Temperature) temperatureInfoList.get(i);
                if (temperature.type == Temperature.TYPE.CURVE) {
                    currentCurveCount++;
                }
            }
        }
        return currentCurveCount;
    }

    public int getCurrentBoxCount() {
        currentBoxCount = 0;
        if (!temperatureInfoList.isEmpty()) {
            int len = temperatureInfoList.size();
            for (int i = len - 1; i >= 0; i--) {
                Temperature temperature = (Temperature) temperatureInfoList.get(i);
                if (temperature.type == Temperature.TYPE.BOX) {
                    currentBoxCount++;
                }
            }
        }
        return currentBoxCount;
    }


}
