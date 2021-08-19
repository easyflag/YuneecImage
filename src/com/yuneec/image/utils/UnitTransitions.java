package com.yuneec.image.utils;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Global;
import com.yuneec.image.RightPane;
import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class UnitTransitions {

    public static void transitionTemperature(){
        for (int i=0;i<CenterPane.getInstance().pointTemperatureNodeList.size();i++){
            ArrayList pointNodeList = (ArrayList) CenterPane.getInstance().pointTemperatureNodeList.get(i);
            Label label = (Label) pointNodeList.get(0);
            float temperature = (float) pointNodeList.get(4);
            label.setText(Utils.getFormatTemperature(temperature));
        }
        for (int i = 0; i < BoxTemperatureManager.getInstance().boxTemperatureList.size(); i++) {
            BoxTemperature boxTemperature = (BoxTemperature) BoxTemperatureManager.getInstance().boxTemperatureList.get(i);
            if (!boxTemperature.getBoxTemperatureNodeMax().isEmpty()){
                ((Label)boxTemperature.getBoxTemperatureNodeMax().get(0)).setText(
                        Utils.getFormatTemperature((float) boxTemperature.getBoxTemperatureNodeMax().get(4)));
            }
            if (!boxTemperature.getBoxTemperatureNodeMin().isEmpty()){
                ((Label)boxTemperature.getBoxTemperatureNodeMin().get(0)).setText(
                        Utils.getFormatTemperature((float) boxTemperature.getBoxTemperatureNodeMin().get(4)));
            }
        }
        for (int i = 0; i < CurveManager.getInstance().curveTemperatureList.size(); i++) {
            CurveTemperature curveTemperature = (CurveTemperature) CurveManager.getInstance().curveTemperatureList.get(i);
            if (!curveTemperature.getCurveTemperatureNodeMax().isEmpty()){
                ((Label)curveTemperature.getCurveTemperatureNodeMax().get(0)).setText(
                        Utils.getFormatTemperature((float) curveTemperature.getCurveTemperatureNodeMax().get(4)));
            }
            if (!curveTemperature.getCurveTemperatureNodeMin().isEmpty()){
                ((Label)curveTemperature.getCurveTemperatureNodeMin().get(0)).setText(
                        Utils.getFormatTemperature((float) curveTemperature.getCurveTemperatureNodeMin().get(4)));
            }
        }
        if (Global.currentOpenImagePath != null){
            RightPane.getInstance().showXYlabel.setText(CenterPane.getInstance().rightXYlabel +
                    Utils.getFormatTemperature(CenterPane.getInstance().pointTemperature));
        }
    }

}
