package com.yuneec.image.module.point;

import com.yuneec.image.module.Temperature;

import java.util.ArrayList;

public class PointTemperature extends Temperature {

    public TYPE type = TYPE.POINT;

    private ArrayList pointTemperatureNode;

    public PointTemperature(ArrayList pointTemperatureNode) {
        this.pointTemperatureNode = pointTemperatureNode;
    }

    public ArrayList getPointTemperatureNode() {
        return pointTemperatureNode;
    }

}
