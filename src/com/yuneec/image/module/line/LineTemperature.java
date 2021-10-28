package com.yuneec.image.module.line;

import com.yuneec.image.module.Temperature;
import com.yuneec.image.module.curve.OneLine;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class LineTemperature extends Temperature {

    public TYPE type = TYPE.LINE;
    private OneLine oneLine; // one lineline Consists of multiple line
    private ArrayList lineTemperatureNodeMax;
    private ArrayList lineTemperatureNodeMin;

    public LineTemperature(OneLine oneLine, ArrayList lineTemperatureNodeMax, ArrayList lineTemperatureNodeMin) {
        this.oneLine = oneLine;
        this.lineTemperatureNodeMax = lineTemperatureNodeMax;
        this.lineTemperatureNodeMin = lineTemperatureNodeMin;
        setType(TYPE.LINE);
    }

    public OneLine getOneLine() {
        return oneLine;
    }

    public ArrayList getLineTemperatureNodeMax() {
        return lineTemperatureNodeMax;
    }

    public ArrayList getLineTemperatureNodeMin() {
        return lineTemperatureNodeMin;
    }

    private boolean maxWindowDraw;

    public boolean isMaxWindowDraw() {
        return maxWindowDraw;
    }

    public void setMaxWindowDraw(boolean maxWindowDraw) {
        this.maxWindowDraw = maxWindowDraw;
    }

}
