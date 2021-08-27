package com.yuneec.image.module.point;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.utils.BackStepManager;
import com.yuneec.image.utils.Utils;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class PointManager {

    public ArrayList pointTemperatureNodeList = new ArrayList();

    public static PointManager instance;

    public static PointManager getInstance() {
        if (instance == null) {
            instance = new PointManager();
        }
        return instance;
    }

    public void setMouseMouseClickdXY(MouseEvent e, CurveManager.MouseStatus status) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        if (CenterPane.getInstance().centerSettingFlag != CenterPane.CenterSettingSelect.POINT) {
            return;
        }
        if (BackStepManager.getInstance().getCurrentPointCount() == BackStepManager.MAX_POINT_COUNT){
            BackStepManager.getInstance().backStep(Temperature.TYPE.POINT);
        }
        if(Utils.mouseLeftClick(e)){
            ArrayList pointNodeList = CenterPane.getInstance().addLabelInImage(x,y,CenterPane.getInstance().pointTemperature, Configs.white_color);
            PointTemperature pointTemperature = new PointTemperature(pointNodeList);
            pointTemperatureNodeList.add(pointTemperature);
            BackStepManager.getInstance().addTemperatureInfo(pointTemperature);
        }

    }


}
