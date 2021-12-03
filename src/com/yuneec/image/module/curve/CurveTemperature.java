package com.yuneec.image.module.curve;

import com.yuneec.image.module.Temperature;

import java.util.ArrayList;

public class CurveTemperature extends Temperature {

    public TYPE type = TYPE.CURVE;
    private ArrayList allLine; // one curveline Consists of multiple line
    private ArrayList curveTemperatureNodeMax;
    private ArrayList curveTemperatureNodeMin;
    private ArrayList curveTemperatureNodeAvg;

    public CurveTemperature(ArrayList allLine, ArrayList curveTemperatureNodeMax, ArrayList curveTemperatureNodeMin, ArrayList curveTemperatureNodeAvg) {
        this.allLine = allLine;
        this.curveTemperatureNodeMax = curveTemperatureNodeMax;
        this.curveTemperatureNodeMin = curveTemperatureNodeMin;
        this.curveTemperatureNodeAvg = curveTemperatureNodeAvg;
        setType(TYPE.CURVE);
    }

    public ArrayList getAllLine() {
        return allLine;
    }

    public ArrayList getCurveTemperatureNodeMax() {
        return curveTemperatureNodeMax;
    }

    public ArrayList getCurveTemperatureNodeMin() {
        return curveTemperatureNodeMin;
    }

    public ArrayList getCurveTemperatureNodeAvg() {
        return curveTemperatureNodeAvg;
    }

    private boolean maxWindowDraw;

    public boolean isMaxWindowDraw() {
        return maxWindowDraw;
    }

    public void setMaxWindowDraw(boolean maxWindowDraw) {
        this.maxWindowDraw = maxWindowDraw;
    }

}
