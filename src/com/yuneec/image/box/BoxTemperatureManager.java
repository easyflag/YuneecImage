package com.yuneec.image.box;

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

    public void backStep(){

    }

    public void nextStep(){

    }

    public void addBoxTemperature(BoxTemperature boxTemperature){
        boxTemperatureList.add(boxTemperature);
    }


}
