package com.yuneec.image.module.circle;

import com.yuneec.image.module.Temperature;

import java.util.ArrayList;

public class CircleTemperature extends Temperature {

    public TYPE type = TYPE.CIRCLE;
    private OneCircle oneCircle;
    private ArrayList circleTemperatureNodeMax;
    private ArrayList circleTemperatureNodeMin;

    public CircleTemperature(OneCircle oneCircle, ArrayList circleTemperatureNodeMax, ArrayList circleTemperatureNodeMin) {
        this.oneCircle = oneCircle;
        this.circleTemperatureNodeMax = circleTemperatureNodeMax;
        this.circleTemperatureNodeMin = circleTemperatureNodeMin;
        setType(TYPE.CIRCLE);
    }

    public OneCircle getOneCircle() {
        return oneCircle;
    }

    public ArrayList getCircleTemperatureNodeMax() {
        return circleTemperatureNodeMax;
    }

    public ArrayList getCircleTemperatureNodeMin() {
        return circleTemperatureNodeMin;
    }

    private boolean maxWindowDraw;

    public boolean isMaxWindowDraw() {
        return maxWindowDraw;
    }

    public void setMaxWindowDraw(boolean maxWindowDraw) {
        this.maxWindowDraw = maxWindowDraw;
    }

}
