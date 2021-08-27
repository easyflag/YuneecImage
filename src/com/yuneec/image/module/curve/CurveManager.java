package com.yuneec.image.module.curve;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.utils.BackStepManager;
import com.yuneec.image.utils.TemperatureAlgorithm;
import com.yuneec.image.utils.YLog;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class CurveManager {

    public static CurveManager instance;

    public static CurveManager getInstance() {
        if (instance == null) {
            instance = new CurveManager();
        }
        return instance;
    }

    public enum MouseStatus {
        MouseDragged,
        MousePressed,
        MouseReleased,
        MouseClicked
    }

    private int lastx, lasty;
    private int x, y;

    public void setMouseMousePressedXY(int x, int y, MouseStatus status) {
        if (CenterPane.getInstance().centerSettingFlag != CenterPane.CenterSettingSelect.CURVE) {
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
        this.x = x;
        this.y = y;
        if (status == MouseStatus.MousePressed){
            if (BackStepManager.getInstance().getCurrentCurveCount() == BackStepManager.MAX_CURVE_COUNT){
                BackStepManager.getInstance().backStep(Temperature.TYPE.CURVE);
            }
            this.lastx = x;
            this.lasty = y;
            xyList.clear();
            lineList = new ArrayList();
            lineList.clear();
        }
        Line line = CenterPane.getInstance().drawLine(lastx,lasty,x,y,Configs.white_color);
        CenterPane.getInstance().showImagePane.getChildren().add(line);
        lineList.add(line);
        this.lastx = x;
        this.lasty = y;

        getCurveAllXY();
        if (status == MouseStatus.MouseReleased){
            startCalculate();
        }
    }

    private void startCalculate() {
        temperatureList.clear();
        getCurveAllTemperature();
        getCurveMaxMinTemperature();
        xyList.clear();
    }

    private ArrayList lineList = new ArrayList();
    private ArrayList<int[]> xyList = new ArrayList<int[]>();
    public ArrayList temperatureList = new ArrayList();
    public ArrayList curveTemperatureList = new ArrayList();

    private void getCurveAllXY() {
        int[] xy = new int[]{x ,y};
        xyList.add(xy);
    }

    private void getCurveAllTemperature() {
        for (int i=0;i<xyList.size();i++){
            int x = xyList.get(i)[0];
            int y = xyList.get(i)[1];
            temperatureList.add(TemperatureAlgorithm.getInstance().getTemperature(x,y));
        }
    }

    float maxTemperature = 0;
    int maxTemperatureIndex = 0;
    float minTemperature = 0;
    int minTemperatureIndex = 0;
    private void getCurveMaxMinTemperature(){
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
//		YLog.I("CurveTemperatureUtil..." + " maxTemperature:" + maxTemperature  + " maxTemperatureIndex:" + maxTemperatureIndex
//				+ " minTemperature:" + minTemperature + " minTemperatureIndex:" + minTemperatureIndex);

        int[] maxTemperatureXY = xyList.get(maxTemperatureIndex);
        int[] minTemperatureXY = xyList.get(minTemperatureIndex);

        ArrayList curveTemperatureNodeMax = CenterPane.getInstance().addLabelInImage(maxTemperatureXY[0],maxTemperatureXY[1],maxTemperature,Configs.red_color);
        ArrayList curveTemperatureNodeMin = CenterPane.getInstance().addLabelInImage(minTemperatureXY[0],minTemperatureXY[1],minTemperature,Configs.blue2_color);

        CurveTemperature curveTemperature = new CurveTemperature(lineList,curveTemperatureNodeMax,curveTemperatureNodeMin);
        curveTemperatureList.add(curveTemperature);
        BackStepManager.getInstance().addTemperatureInfo(curveTemperature);
    }

}
