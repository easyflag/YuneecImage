package com.yuneec.image.box;

import javafx.scene.shape.Line;

import java.util.ArrayList;

public class BoxTemperature {
    private int startLineX = 0;
    private int startLineY = 0;
    private int endLineX = 0;
    private int endLineY = 0;
    private Line topLine;
    private Line bottomLine;
    private Line leftLine;
    private Line rightLine;
    private ArrayList boxTemperatureNodeMax;
    private ArrayList boxTemperatureNodeMin;

    public BoxTemperature(int startLineX,int startLineY,int endLineX,int endLineY,
                          Line topLine,Line bottomLine,Line leftLine,Line rightLine,
                          ArrayList boxTemperatureNodeMax,ArrayList boxTemperatureNodeMin){
        this.startLineX = startLineX;
        this.startLineY = startLineY;
        this.endLineX = endLineX;
        this.endLineY =endLineY;
        this.topLine = topLine;
        this.bottomLine = bottomLine;
        this.leftLine = leftLine;
        this.rightLine = rightLine;
        this.boxTemperatureNodeMax = boxTemperatureNodeMax;
        this.boxTemperatureNodeMin = boxTemperatureNodeMin;
    }

    public int getStartLineX() {
        return startLineX;
    }

    public void setStartLineX(int startLineX) {
        this.startLineX = startLineX;
    }

    public int getStartLineY() {
        return startLineY;
    }

    public void setStartLineY(int startLineY) {
        this.startLineY = startLineY;
    }

    public int getEndLineX() {
        return endLineX;
    }

    public void setEndLineX(int endLineX) {
        this.endLineX = endLineX;
    }

    public int getEndLineY() {
        return endLineY;
    }

    public void setEndLineY(int endLineY) {
        this.endLineY = endLineY;
    }

    public Line getTopLine() {
        return topLine;
    }

    public void setTopLine(Line topLine) {
        this.topLine = topLine;
    }

    public Line getBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(Line bottomLine) {
        this.bottomLine = bottomLine;
    }

    public Line getLeftLine() {
        return leftLine;
    }

    public void setLeftLine(Line leftLine) {
        this.leftLine = leftLine;
    }

    public Line getRightLine() {
        return rightLine;
    }

    public void setRightLine(Line rightLine) {
        this.rightLine = rightLine;
    }

    public ArrayList getBoxTemperatureNodeMax() {
        return boxTemperatureNodeMax;
    }

    public void setBoxTemperatureNodeMax(ArrayList boxTemperatureNodeMax) {
        this.boxTemperatureNodeMax = boxTemperatureNodeMax;
    }

    public ArrayList getBoxTemperatureNodeMin() {
        return boxTemperatureNodeMin;
    }

    public void setBoxTemperatureNodeMin(ArrayList boxTemperatureNodeMin) {
        this.boxTemperatureNodeMin = boxTemperatureNodeMin;
    }
}
