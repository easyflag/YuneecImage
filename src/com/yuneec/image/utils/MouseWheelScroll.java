package com.yuneec.image.utils;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import javafx.scene.input.ScrollEvent;

public class MouseWheelScroll {

    private static MouseWheelScroll instance;
    private double zoomRatio = WindowChange.defaultImageZoomRatio;
    public static boolean isZoom = false;

    public static MouseWheelScroll I() {
        if (instance == null) {
            instance = new MouseWheelScroll();
        }
        return instance;
    }

    public void setMax(boolean max){
        if (max){
            zoomRatio = WindowChange.maxImageZoomRatio;
        }else {
            zoomRatio = WindowChange.defaultImageZoomRatio;
        }
    }

    public void init() {
        CenterPane.getInstance().centerImagePane.addEventFilter(ScrollEvent.SCROLL, event -> {
            isZoom = true;
            double rate = 0;
            if (event.getDeltaY() > 0) {
                rate = 0.05;
            } else {
                rate = -0.05;
            }
            zoomRatio = zoomRatio + rate;
            if (zoomRatio < WindowChange.defaultImageZoomRatio){
                zoomRatio = WindowChange.defaultImageZoomRatio;
            }
            if (zoomRatio > WindowChange.maxImageZoomRatio){
                zoomRatio = WindowChange.maxImageZoomRatio;
            }
            if (WindowChange.maxWindow){
                calculateWH();
            }
        });
    }

    private double lastImageWidth;
    public void calculateWH() {
        Global.currentOpenImageWidth = Configs.DefaultImageWidth * zoomRatio;
        Global.currentOpenImageHeight = Configs.DefaultImageHeight * zoomRatio;
        if (CenterPane.getInstance().imageView != null){
            CenterPane.getInstance().imageView.setFitWidth(Configs.DefaultImageWidth * zoomRatio);
            CenterPane.getInstance().imageView.setFitHeight(Configs.DefaultImageHeight * zoomRatio);
            CenterPane.getInstance().showImagePane.setPrefWidth(Configs.DefaultImageWidth * zoomRatio);
            CenterPane.getInstance().showImagePane.setPrefHeight(Configs.DefaultImageHeight * zoomRatio);
        }
        CenterPane.getInstance().getImagePaneOffsetXY();

        WindowChange.imageZoomRatio = zoomRatio;
        WindowChange.maxToMinRatio = lastImageWidth / Global.currentOpenImageWidth;
        YLog.I("MouseWheelScroll  imageZoomRatio : " + WindowChange.imageZoomRatio);
        WindowChange.I().reLayoutTemperature();

        lastImageWidth = Global.currentOpenImageWidth;
    }

}
