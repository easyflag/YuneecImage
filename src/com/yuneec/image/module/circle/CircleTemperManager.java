package com.yuneec.image.module.circle;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.OneLine;
import com.yuneec.image.module.line.LineTemperature;
import com.yuneec.image.utils.*;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CircleTemperManager {

    public static CircleTemperManager instance;

    public static CircleTemperManager getInstance() {
        if (instance == null) {
            instance = new CircleTemperManager();
        }
        return instance;
    }


    private int startx, starty;

    public void setMouseMousePressedXY(int x, int y, CurveManager.MouseStatus status) {
        if (CenterPane.getInstance().centerSettingFlag != CenterPane.CenterSettingSelect.CIRCLE) {
            return;
        }
        if (x >= Global.currentOpenImageWidth){
            x = (int) Global.currentOpenImageWidth;
        }
        if (x <= 0){
            x = 0;
        }
        if (y >= Global.currentOpenImageHeight){
            y = (int) Global.currentOpenImageHeight;
        }
        if (y <= 0){
            y = 0;
        }
        if (status == CurveManager.MouseStatus.MousePressed){
            if (BackStepManager.getInstance().getCurrentLineCount() == BackStepManager.MAX_LINE_COUNT && BackStepManager.openTemperatureLimit){
                BackStepManager.getInstance().backStep(Temperature.TYPE.LINE);
            }
            getStartXY = true;
            if (getStartXY){
                this.startx = x;
                this.starty = y;
                getStartXY = false;
            }
        }

        CenterPane.getInstance().showImagePane.getChildren().remove(circle);
        double radius = Math.sqrt((x-startx)*(x-startx)+(y-starty)*(y-starty));
        circle = drawCircle(startx,starty,radius);
        CenterPane.getInstance().showImagePane.getChildren().add(circle);

        for (int i=0;i<circleList.size();i++){
            Circle circle = ((OneCircle) circleList.get(i)).getCircle();
            if (!CenterPane.getInstance().showImagePane.getChildren().contains(circle)){
                CenterPane.getInstance().showImagePane.getChildren().add(1,circle);
            }
        }

        if (status == CurveManager.MouseStatus.MouseReleased){
            oneCircle = new OneCircle(circle,startx,starty,radius);
            circleList.add(oneCircle);
            getOneCircleAllxy();
            startCalculate();
        }
    }

    private Circle drawCircle(int x,int y,double radius){
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
        circle.setStroke(javafx.scene.paint.Color.web(Configs.temperatureColor));
        circle.setFill(javafx.scene.paint.Color.TRANSPARENT);
        return circle;
    }

    private void getOneCircleAllxy() {
        for (int j = (int) oneCircle.getRadius(); j > 0; j--){
            for (int i = 0; i < 360; i += 1) {
                int x = (int) (oneCircle.getStartX() + j * Math.cos(i));
                int y = (int) (oneCircle.getStartY() + j * Math.sin(i));
                int[] xy = new int[]{x, y};
                oneCirclexylist.add(xy);
            }
        }
    }

    private void startCalculate() {
        temperatureList.clear();
        getLineAllTemperature();
        getLineMaxMinTemperature();
        oneCirclexylist.clear();
    }


    private void getLineAllTemperature() {
        for (int i=0;i<oneCirclexylist.size();i++){
            int x = oneCirclexylist.get(i)[0];
            int y = oneCirclexylist.get(i)[1];
            temperatureList.add(TemperatureAlgorithm.getInstance().getTemperature(x,y));
        }
    }

    float maxTemperature = 0;
    int maxTemperatureIndex = 0;
    float minTemperature = 0;
    int minTemperatureIndex = 0;
    private void getLineMaxMinTemperature(){
        maxTemperatureIndex = 0;
        minTemperatureIndex = 0;
        maxTemperature = (float)temperatureList.get(0);
        minTemperature = (float)temperatureList.get(0);
        for (int i = 0; i < temperatureList.size(); i++) {
            if ((float)temperatureList.get(i) > maxTemperature) {
                maxTemperature = (float)temperatureList.get(i);
                maxTemperatureIndex = i;
            }
            if ((float)temperatureList.get(i) < minTemperature) {
                minTemperature = (float)temperatureList.get(i);
                minTemperatureIndex = i;
            }
        }
        int[] maxTemperatureXY = oneCirclexylist.get(maxTemperatureIndex);
        int[] minTemperatureXY = oneCirclexylist.get(minTemperatureIndex);

        ArrayList lineTemperatureNodeMax = CenterPane.getInstance().addLabelInImage(maxTemperatureXY[0],maxTemperatureXY[1],maxTemperature,Configs.red_color);
        ArrayList lineTemperatureNodeMin = CenterPane.getInstance().addLabelInImage(minTemperatureXY[0],minTemperatureXY[1],minTemperature,Configs.blue2_color);

//        LineTemperature lineTemperature = new LineTemperature(oneLine,lineTemperatureNodeMax,lineTemperatureNodeMin);
//        lineTemperature.setMaxWindowDraw(WindowChange.I().maxWindow);
//        lineTemperatureList.add(lineTemperature);
//        BackStepManager.getInstance().addTemperatureInfo(lineTemperature);
    }

    public void recalculate(){
        for (int i = 0; i < lineTemperatureList.size(); i++) {
            LineTemperature lineTemperature = (LineTemperature) lineTemperatureList.get(i);
            if (!lineTemperature.getLineTemperatureNodeMax().isEmpty()){
                Label label = (Label) lineTemperature.getLineTemperatureNodeMax().get(0);
                int x = (int) lineTemperature.getLineTemperatureNodeMax().get(5);
                int y = (int) lineTemperature.getLineTemperatureNodeMax().get(6);
                float newPointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                label.setText(Utils.getFormatTemperature(newPointTemperature));
            }
            if (!lineTemperature.getLineTemperatureNodeMin().isEmpty()){
                Label label = (Label) lineTemperature.getLineTemperatureNodeMin().get(0);
                int x = (int) lineTemperature.getLineTemperatureNodeMin().get(5);
                int y = (int) lineTemperature.getLineTemperatureNodeMin().get(6);
                float newPointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                label.setText(Utils.getFormatTemperature(newPointTemperature));
            }
        }
    }

    Circle circle = null;
    public ArrayList circleList = new ArrayList();
    OneCircle oneCircle;

    List<int[]> oneCirclexylist = new ArrayList<int[]>();
    public ArrayList temperatureList = new ArrayList();
    public ArrayList lineTemperatureList = new ArrayList();
    private boolean getStartXY = false;


}
