package com.yuneec.image.utils;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.util.Timer;
import java.util.TimerTask;

public class WindowChange {

    private static WindowChange instance;

    public static WindowChange I() {
        if (instance == null) {
            instance = new WindowChange();
        }
        return instance;
    }

    public void init() {
        Screen screen = Screen.getPrimary();
        Rectangle2D r1 = screen.getBounds();
        Rectangle2D r2 = screen.getVisualBounds();
        YLog.I("WindowChange " + r1.getHeight() + "," + r1.getWidth());
        YLog.I("WindowChange " + r2.getHeight() + "," + r2.getWidth());
    }

    public void setWindowMax(boolean max) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                double width = Global.primaryStage.getWidth();
                double height = Global.primaryStage.getHeight();

                Configs.SceneHeight = (int) height;
                Configs.CenterPanelHeight = (int) (height - Configs.MenuHeight - Configs.LineHeight);
                Configs.CenterPanelWidth = (int) (width - Configs.LeftPanelWidth - Configs.RightPanelWidth);

                CenterPane.getInstance().centerPane.setPrefWidth(Configs.CenterPanelWidth);
                CenterPane.getInstance().centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
                CenterPane.getInstance().centerImagePane.setPrefWidth(Configs.CenterPanelWidth);

                double wRatio = Configs.CenterPanelWidth / Configs.DefaultImageWidth;
                double hRatio = Configs.CenterPanelHeight / Configs.DefaultImageHeight;
                YLog.I("WindowChange   width:" + width + " , height:" + height + "  ,max :" + max + " ,wRatio:" + wRatio + " ,hRatio:" + hRatio);
                if (wRatio < hRatio) {
                    ratio = wRatio;
                } else {
                    ratio = hRatio;
                }
                ratio-=0.15;

                reLoadImage(max);
            }
        }, 100);
    }

    double ratio = 1.5;

    private void reLoadImage(boolean max) {
        if (max) {
            Global.currentOpenImageWidth = Configs.DefaultImageWidth * ratio;
            Global.currentOpenImageHeight = Configs.DefaultImageHeight * ratio;
            CenterPane.getInstance().imageView.setFitWidth(Configs.DefaultImageWidth * ratio);
            CenterPane.getInstance().imageView.setFitHeight(Configs.DefaultImageHeight * ratio);
            CenterPane.getInstance().showImagePane.setPrefWidth(Configs.DefaultImageWidth * ratio);
            CenterPane.getInstance().showImagePane.setPrefHeight(Configs.DefaultImageHeight * ratio);
        } else {
            Global.currentOpenImageWidth = Configs.DefaultImageWidth;
            Global.currentOpenImageHeight = Configs.DefaultImageHeight;
            CenterPane.getInstance().imageView.setFitWidth(Configs.DefaultImageWidth);
            CenterPane.getInstance().imageView.setFitHeight(Configs.DefaultImageHeight);
            CenterPane.getInstance().showImagePane.setPrefWidth(Configs.DefaultImageWidth);
            CenterPane.getInstance().showImagePane.setPrefHeight(Configs.DefaultImageHeight);
        }
        CenterPane.getInstance().getImagePaneOffsetXY();
    }


}
