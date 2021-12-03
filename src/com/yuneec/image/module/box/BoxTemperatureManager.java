package com.yuneec.image.module.box;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.point.PointTemperature;
import com.yuneec.image.utils.*;
import javafx.scene.control.Label;
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
            CenterPane.getInstance().showImagePane.getChildren().removeAll(boxTemperature.getBoxTemperatureNodeAvg());
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
            if (BackStepManager.getInstance().getCurrentBoxCount() == BackStepManager.MAX_BOX_COUNT && BackStepManager.openTemperatureLimit){
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
        boxTemperature.setMaxWindowDraw(WindowChange.I().maxWindow);
        boxTemperatureList.add(boxTemperature);
        BackStepManager.getInstance().addTemperatureInfo(boxTemperature);
    }

    public void recalculate(){
        float allTemperature=0;
        for (int i = 0; i < boxTemperatureList.size(); i++) {
            BoxTemperature boxTemperature = (BoxTemperature) boxTemperatureList.get(i);
            if (!boxTemperature.getBoxTemperatureNodeMax().isEmpty()){
                Label label = (Label) boxTemperature.getBoxTemperatureNodeMax().get(0);
                int x = (int) boxTemperature.getBoxTemperatureNodeMax().get(5);
                int y = (int) boxTemperature.getBoxTemperatureNodeMax().get(6);
                float newPointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                label.setText(Utils.getFormatTemperature(newPointTemperature));
            }
            if (!boxTemperature.getBoxTemperatureNodeMin().isEmpty()){
                Label label = (Label) boxTemperature.getBoxTemperatureNodeMin().get(0);
                int x = (int) boxTemperature.getBoxTemperatureNodeMin().get(5);
                int y = (int) boxTemperature.getBoxTemperatureNodeMin().get(6);
                float newPointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                label.setText(Utils.getFormatTemperature(newPointTemperature));
            }
            if (!boxTemperature.getBoxTemperatureNodeAvg().isEmpty()){
                Label label = (Label) boxTemperature.getBoxTemperatureNodeAvg().get(0);
                for (int ii = 0; ii < BoxTemperatureUtil.getInstance().temperatureList.size(); ii++) {
                    allTemperature += (float) BoxTemperatureUtil.getInstance().temperatureList.get(ii);
                }
                float newAvgTemperature = allTemperature / BoxTemperatureUtil.getInstance().temperatureList.size();
                label.setText(Utils.getFormatTemperature(newAvgTemperature));
            }
        }
    }


}
