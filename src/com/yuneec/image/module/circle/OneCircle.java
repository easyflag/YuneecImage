package com.yuneec.image.module.circle;

import javafx.scene.shape.Circle;

public class OneCircle {

    private Circle circle;
    private int StartX;
    private int StartY;
    private double radius;

    public OneCircle(Circle circle, int StartX, int StartY,double radius) {
        this.circle = circle;
        this.StartX = StartX;
        this.StartY = StartY;
        this.radius = radius;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
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


    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
