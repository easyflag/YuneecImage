package com.yuneec.image.utils;

import com.yuneec.image.Configs;
import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.circle.CircleTemperManager;
import com.yuneec.image.module.circle.CircleTemperature;
import com.yuneec.image.module.circle.OneCircle;
import com.yuneec.image.module.colorpalette.ColorPalette;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import com.yuneec.image.module.curve.OneLine;
import com.yuneec.image.module.line.LineTemperManager;
import com.yuneec.image.module.line.LineTemperature;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.module.point.PointTemperature;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ColorPickerManager {

    private static ColorPickerManager instance;

    public static ColorPickerManager I() {
        if (instance == null) {
            instance = new ColorPickerManager();
        }
        return instance;
    }

    public void setColor(String color){
        Configs.temperatureColor = color;
        setPointTemperatureColor();
        setBoxTemperatureColor();
        setCurveTemperatureColor();
        setLineTemperatureColor();
        setCircleTemperatureColor();
    }

    private void setPointTemperatureColor() {
        for (int i = 0; i < PointManager.getInstance().pointTemperatureNodeList.size(); i++) {
            PointTemperature pointTemperature = (PointTemperature) PointManager.getInstance().pointTemperatureNodeList.get(i);
            ArrayList pointNodeList = pointTemperature.getPointTemperatureNode();
            Label label = (Label) pointNodeList.get(0);
            label.setTextFill(Color.web(Configs.temperatureColor));
        }
    }

    private void setBoxTemperatureColor() {
        for (int i = 0; i < BoxTemperatureManager.getInstance().boxTemperatureList.size(); i++) {
            BoxTemperature boxTemperature = (BoxTemperature) BoxTemperatureManager.getInstance().boxTemperatureList.get(i);
            if (!boxTemperature.getBoxTemperatureNodeMax().isEmpty()) {
                ArrayList boxTemperatureNodeMaxList = boxTemperature.getBoxTemperatureNodeMax();
                Label label = (Label) boxTemperatureNodeMaxList.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            if (!boxTemperature.getBoxTemperatureNodeMin().isEmpty()) {
                ArrayList boxTemperatureNodeMinList = boxTemperature.getBoxTemperatureNodeMin();
                Label label = (Label) boxTemperatureNodeMinList.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            if (!boxTemperature.getBoxTemperatureNodeAvg().isEmpty()) {
                ArrayList boxTemperatureNodeAvgList = boxTemperature.getBoxTemperatureNodeAvg();
                Label label = (Label) boxTemperatureNodeAvgList.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            boxTemperature.getTopLine().setStroke(Color.web(Configs.temperatureColor));
            boxTemperature.getBottomLine().setStroke(Color.web(Configs.temperatureColor));
            boxTemperature.getLeftLine().setStroke(Color.web(Configs.temperatureColor));
            boxTemperature.getRightLine().setStroke(Color.web(Configs.temperatureColor));
        }
    }

    private void setCurveTemperatureColor(){
        for (int i = 0; i < CurveManager.getInstance().curveTemperatureList.size(); i++) {
            CurveTemperature curveTemperature = (CurveTemperature) CurveManager.getInstance().curveTemperatureList.get(i);
            if (!curveTemperature.getCurveTemperatureNodeMax().isEmpty()){
                ArrayList curveTemperatureNodeMax = curveTemperature.getCurveTemperatureNodeMax();
                Label label = (Label) curveTemperatureNodeMax.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            if (!curveTemperature.getCurveTemperatureNodeMin().isEmpty()){
                ArrayList curveTemperatureNodeMin = curveTemperature.getCurveTemperatureNodeMin();
                Label label = (Label) curveTemperatureNodeMin.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            if (!curveTemperature.getCurveTemperatureNodeAvg().isEmpty()){
                ArrayList curveTemperatureNodeAvg = curveTemperature.getCurveTemperatureNodeAvg();
                Label label = (Label) curveTemperatureNodeAvg.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            ArrayList allLine = curveTemperature.getAllLine();
            for (int j=0;j<allLine.size();j++){
                OneLine oneLine = (OneLine) allLine.get(j);
                oneLine.getLine().setStroke(Color.web(Configs.temperatureColor));
            }
        }
    }

    private void setLineTemperatureColor(){
        for (int i = 0; i < LineTemperManager.getInstance().lineTemperatureList.size(); i++) {
            LineTemperature lineTemperature = (LineTemperature) LineTemperManager.getInstance().lineTemperatureList.get(i);
            if (!lineTemperature.getLineTemperatureNodeMax().isEmpty()){
                ArrayList curveTemperatureNodeMax = lineTemperature.getLineTemperatureNodeMax();
                Label label = (Label) curveTemperatureNodeMax.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            if (!lineTemperature.getLineTemperatureNodeMin().isEmpty()){
                ArrayList lineTemperatureNodeMin = lineTemperature.getLineTemperatureNodeMin();
                Label label = (Label) lineTemperatureNodeMin.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            OneLine oneLine = (OneLine) lineTemperature.getOneLine();
            oneLine.getLine().setStroke(Color.web(Configs.temperatureColor));
        }
    }

    private void setCircleTemperatureColor(){
        for (int i = 0; i < CircleTemperManager.getInstance().circleTemperatureList.size(); i++) {
            CircleTemperature circleTemperature = (CircleTemperature) CircleTemperManager.getInstance().circleTemperatureList.get(i);
            if (!circleTemperature.getCircleTemperatureNodeMax().isEmpty()){
                ArrayList temperatureNodeMax = circleTemperature.getCircleTemperatureNodeMax();
                Label label = (Label) temperatureNodeMax.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            if (!circleTemperature.getCircleTemperatureNodeMin().isEmpty()){
                ArrayList temperatureNodeMin = circleTemperature.getCircleTemperatureNodeMin();
                Label label = (Label) temperatureNodeMin.get(0);
                label.setTextFill(Color.web(Configs.temperatureColor));
            }
            OneCircle oneCircle = (OneCircle) circleTemperature.getOneCircle();
            oneCircle.getCircle().setStroke(Color.web(Configs.temperatureColor));
        }
    }
}
