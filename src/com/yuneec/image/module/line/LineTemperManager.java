package com.yuneec.image.module.line;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import com.yuneec.image.module.curve.OneLine;
import com.yuneec.image.utils.*;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LineTemperManager {

    public static LineTemperManager instance;

    public static LineTemperManager getInstance() {
        if (instance == null) {
            instance = new LineTemperManager();
        }
        return instance;
    }


    private int startx, starty;

    public void setMouseMousePressedXY(int x, int y, CurveManager.MouseStatus status) {
        if (CenterPane.getInstance().centerSettingFlag != CenterPane.CenterSettingSelect.LINE) {
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

        CenterPane.getInstance().showImagePane.getChildren().remove(line);
        line = CenterPane.getInstance().drawLine(startx,starty,x,y,Configs.temperatureColor);
        CenterPane.getInstance().showImagePane.getChildren().add(line);

        for (int i=0;i<lineList.size();i++){
            Line line = ((OneLine) lineList.get(i)).getLine();
            if (!CenterPane.getInstance().showImagePane.getChildren().contains(line)){
                CenterPane.getInstance().showImagePane.getChildren().add(line);
            }
        }

        if (status == CurveManager.MouseStatus.MouseReleased){
            oneLine = new OneLine(line,startx,starty,x,y);
            lineList.add(oneLine);
            getOneLineAllxy();
            startCalculate();
        }
    }

    private void getOneLineAllxy() {
        int x1 = (int) line.getStartX(), y1 = (int) line.getStartY(), x2 = (int) line.getEndX(), y2 = (int) line.getEndY();
        BufferedImage img = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        Color color = Color.BLACK;
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
        int[] rgbArray = new int[img.getWidth()];

        for (int y = 0; y < img.getHeight(); y++) {
            img.getRGB(0, y, img.getWidth(), 1, rgbArray, 0, img.getWidth());
            for (int x = 0; x < rgbArray.length; x++) {
                if (rgbArray[x] == color.getRGB()) {
                    int[] xy = new int[]{x ,y};
                    linexylist.add(xy);
                }
            }
        }
    }

    private void startCalculate() {
        temperatureList.clear();
        getLineAllTemperature();
        getLineMaxMinTemperature();
        linexylist.clear();
    }


    private void getLineAllTemperature() {
        for (int i=0;i<linexylist.size();i++){
            int x = linexylist.get(i)[0];
            int y = linexylist.get(i)[1];
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
        int[] maxTemperatureXY = linexylist.get(maxTemperatureIndex);
        int[] minTemperatureXY = linexylist.get(minTemperatureIndex);

        ArrayList lineTemperatureNodeMax = CenterPane.getInstance().addLabelInImage(maxTemperatureXY[0],maxTemperatureXY[1],maxTemperature,Configs.red_color);
        ArrayList lineTemperatureNodeMin = CenterPane.getInstance().addLabelInImage(minTemperatureXY[0],minTemperatureXY[1],minTemperature,Configs.blue2_color);

        LineTemperature lineTemperature = new LineTemperature(oneLine,lineTemperatureNodeMax,lineTemperatureNodeMin);
        lineTemperature.setMaxWindowDraw(WindowChange.I().maxWindow);
        lineTemperatureList.add(lineTemperature);
        BackStepManager.getInstance().addTemperatureInfo(lineTemperature);
    }

    public void recalculate(){
        for (int i = 0; i < lineTemperatureList.size(); i++) {
            LineTemperature lineTemperature = (LineTemperature) lineTemperatureList.get(i);
            if (!lineTemperature.getLineTemperatureNodeMax().isEmpty()){
                javafx.scene.control.Label label = (javafx.scene.control.Label) lineTemperature.getLineTemperatureNodeMax().get(0);
                int x = (int) lineTemperature.getLineTemperatureNodeMax().get(5);
                int y = (int) lineTemperature.getLineTemperatureNodeMax().get(6);
                float newPointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                label.setText(Utils.getFormatTemperature(newPointTemperature));
            }
            if (!lineTemperature.getLineTemperatureNodeMin().isEmpty()){
                javafx.scene.control.Label label = (Label) lineTemperature.getLineTemperatureNodeMin().get(0);
                int x = (int) lineTemperature.getLineTemperatureNodeMin().get(5);
                int y = (int) lineTemperature.getLineTemperatureNodeMin().get(6);
                float newPointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                label.setText(Utils.getFormatTemperature(newPointTemperature));
            }
        }
    }

    List<int[]> linexylist = new ArrayList<int[]>();
    public ArrayList temperatureList = new ArrayList();
    public ArrayList lineTemperatureList = new ArrayList();
    Line line = null;
    OneLine oneLine;
    public ArrayList lineList = new ArrayList();
    private boolean getStartXY = false;


}
