package com.yuneec.image.module.box;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.utils.BackStepManager;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class BoxTemperatureManager {

    public ArrayList boxTemperatureList = new ArrayList();

    public static BoxTemperatureManager instance;

    public static BoxTemperatureManager getInstance() {
        if (instance == null) {
            instance = new BoxTemperatureManager();
        }
        return instance;
    }

    private void backStep() {
        if (!boxTemperatureList.isEmpty()) {
            int endIndex = boxTemperatureList.size() - 1;
            BoxTemperature boxTemperature = (BoxTemperature) boxTemperatureList.get(endIndex);
            CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getTopLine(), boxTemperature.getBottomLine(),
                    boxTemperature.getLeftLine(), boxTemperature.getRightLine());
            CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeMax());
            CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeMin());
            boxTemperatureList.remove(endIndex);
        }
    }

    private int lastx, lasty;
    private int x, y;

    public void setMouseMousePressedXY(int x, int y, CurveManager.MouseStatus status) {
        if (CenterPane.getInstance().centerSettingFlag != CenterPane.CenterSettingSelect.BOX) {
            return;
        }
//        YLog.I("CurveManager : " + "x=" + x + " y=" + y );
        this.x = x;
        this.y = y;
        if (status == CurveManager.MouseStatus.MousePressed){
            if (BackStepManager.getInstance().getCurrentBoxCount() == BackStepManager.MAX_BOX_COUNT){
                BackStepManager.getInstance().backStep(Temperature.TYPE.BOX);
            }
            this.lastx = x;
            this.lasty = y;
        }
        this.lastx = x;
        this.lasty = y;

        if (status == CurveManager.MouseStatus.MouseReleased){

        }
    }

    public void addBoxTemperature(BoxTemperature boxTemperature){
        boxTemperatureList.add(boxTemperature);
        BackStepManager.getInstance().addTemperatureInfo(boxTemperature);
    }


}
