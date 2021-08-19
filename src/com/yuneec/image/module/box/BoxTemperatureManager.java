package com.yuneec.image.module.box;

import com.yuneec.image.CenterPane;
import com.yuneec.image.utils.BackStepManager;

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

    public void nextStep(){

    }

    public void addBoxTemperature(BoxTemperature boxTemperature){
        boxTemperatureList.add(boxTemperature);
        BackStepManager.getInstance().addTemperatureInfo(boxTemperature);
    }


}
