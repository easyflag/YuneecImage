package com.yuneec.image.module.curve;

import javafx.scene.shape.Line;

public class OneLine {

    private Line line;
    private int StartX;
    private int StartY;
    private int EndX;
    private int EndY;

    public OneLine(Line line,int StartX, int StartY, int EndX, int EndY) {
        this.line = line;
        this.StartX = StartX;
        this.StartY = StartY;
        this.EndX = EndX;
        this.EndY = EndY;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getStartX() {
        return StartX;
    }

    public void setStartX(int startX) {
        StartX = startX;
    }

    public int getStartY() {
        return StartY;
    }

    public void setStartY(int startY) {
        StartY = startY;
    }

    public int getEndX() {
        return EndX;
    }

    public void setEndX(int endX) {
        EndX = endX;
    }

    public int getEndY() {
        return EndY;
    }

    public void setEndY(int endY) {
        EndY = endY;
    }
}
