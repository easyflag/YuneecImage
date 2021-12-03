package com.yuneec.image.utils;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.guide.GuideTemperatureAlgorithm;
import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.circle.CircleTemperManager;
import com.yuneec.image.module.circle.CircleTemperature;
import com.yuneec.image.module.circle.OneCircle;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import com.yuneec.image.module.curve.OneLine;
import com.yuneec.image.module.line.LineTemperManager;
import com.yuneec.image.module.line.LineTemperature;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.module.point.PointTemperature;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WindowChange {

    private static WindowChange instance;
    public static boolean maxWindow = false;
    public static final double defaultImageZoomRatio = 1;
    public static double maxImageZoomRatio;
    public static double imageZoomRatio = defaultImageZoomRatio;
    public static double maxToMinRatio;

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
                maxWindow = max;
                double width = Global.primaryStage.getWidth();
                double height = Global.primaryStage.getHeight();

                Configs.SceneHeight = (int) height;
                Configs.CenterPanelHeight = (int) (height - Configs.SystemBarHeight - Configs.MenuHeight - Configs.LineHeight);
                Configs.CenterPanelWidth = (int) (width - Configs.LeftPanelWidth - Configs.RightPanelWidth);

                CenterPane.getInstance().centerPane.setPrefWidth(Configs.CenterPanelWidth);
                CenterPane.getInstance().centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
                CenterPane.getInstance().centerImagePane.setPrefWidth(Configs.CenterPanelWidth);

                double wRatio = Configs.CenterPanelWidth / Configs.DefaultImageWidth;
                double hRatio = Configs.CenterPanelHeight / Configs.DefaultImageHeight;
                if (wRatio < hRatio) {
                    imageZoomRatio = wRatio;
                } else {
                    imageZoomRatio = hRatio;
                }
                imageZoomRatio-=0.1;
                maxImageZoomRatio = imageZoomRatio;
                MouseWheelScroll.I().setMax(max);
//                YLog.I("WindowChange   width:" + width + " , height:" + height + "  ,max :" + max +
//                        " ,wRatio:" + wRatio + " ,hRatio:" + hRatio + " ,imageZoomRatio:" + imageZoomRatio);
                reLoadImage();
            }
        }, 100);
    }

    public void reLoadImage() {
        if (maxWindow) {
            GuideTemperatureAlgorithm.SupportScale = true;
            Global.currentOpenImageWidth = Configs.DefaultImageWidth * imageZoomRatio;
            Global.currentOpenImageHeight = Configs.DefaultImageHeight * imageZoomRatio;
            maxToMinRatio = Configs.DefaultImageWidth / Global.currentOpenImageWidth;
            if (CenterPane.getInstance().imageView != null){
                CenterPane.getInstance().imageView.setFitWidth(Configs.DefaultImageWidth * imageZoomRatio);
                CenterPane.getInstance().imageView.setFitHeight(Configs.DefaultImageHeight * imageZoomRatio);
                CenterPane.getInstance().showImagePane.setPrefWidth(Configs.DefaultImageWidth * imageZoomRatio);
                CenterPane.getInstance().showImagePane.setPrefHeight(Configs.DefaultImageHeight * imageZoomRatio);
            }
        } else {
            GuideTemperatureAlgorithm.SupportScale = false;
            imageZoomRatio = defaultImageZoomRatio;
            Global.currentOpenImageWidth = Configs.DefaultImageWidth;
            Global.currentOpenImageHeight = Configs.DefaultImageHeight;
            if (CenterPane.getInstance().imageView != null){
                CenterPane.getInstance().imageView.setFitWidth(Configs.DefaultImageWidth);
                CenterPane.getInstance().imageView.setFitHeight(Configs.DefaultImageHeight);
                CenterPane.getInstance().showImagePane.setPrefWidth(Configs.DefaultImageWidth);
                CenterPane.getInstance().showImagePane.setPrefHeight(Configs.DefaultImageHeight);
            }
        }
        CenterPane.getInstance().getImagePaneOffsetXY();
        reLayoutTemperature();
    }

    public void reLayoutTemperature() {
        reLayoutPointTemperatureLocation();
        reLayoutBoxTemperatureLocation();
        reLayoutCurveTemperatureLocation();
        reLayoutLineTemperatureLocation();
        reLayoutCircleTemperatureLocation();
    }

    private void reLayoutPointTemperatureLocation() {
        for (int i = 0; i < PointManager.getInstance().pointTemperatureNodeList.size(); i++) {
            PointTemperature pointTemperature = (PointTemperature) PointManager.getInstance().pointTemperatureNodeList.get(i);
            ArrayList pointNodeList = pointTemperature.getPointTemperatureNode();
            boolean maxClickPoint = pointTemperature.isMaxWindowDraw();
            reLayoutPoint(pointNodeList,maxClickPoint);
        }
    }

    private void reLayoutPoint(ArrayList pointNodeList,boolean maxClickPoint){
        Label label = (Label) pointNodeList.get(0);
        Circle circle = (Circle) pointNodeList.get(1);
        Line xLine = (Line) pointNodeList.get(2);
        Line yLine = (Line) pointNodeList.get(3);
        float temperature = (float) pointNodeList.get(4);
        int x = (int) pointNodeList.get(5);
        int y = (int) pointNodeList.get(6);
        if (maxClickPoint){
            if (maxWindow){
                imageZoomRatio = defaultImageZoomRatio;
            }else {
                imageZoomRatio = maxToMinRatio;
            }
        }else {
            if (maxWindow){
                imageZoomRatio = maxImageZoomRatio;
            }else {
                imageZoomRatio = defaultImageZoomRatio;
            }
        }
//        YLog.I("------> imageZoomRatio :" +imageZoomRatio);
        label.setTranslateX((x*imageZoomRatio+7));
        label.setTranslateY((y*imageZoomRatio-8));
        circle.setCenterX(x*imageZoomRatio);
        circle.setCenterY(y*imageZoomRatio);
        double lineLEN = 7;
        reLayoutLine(x*imageZoomRatio-lineLEN,y*imageZoomRatio,x*imageZoomRatio+lineLEN,y*imageZoomRatio,xLine);
        reLayoutLine(x*imageZoomRatio,y*imageZoomRatio-lineLEN,x*imageZoomRatio,y*imageZoomRatio+lineLEN,yLine);
    }

    private void reLayoutLine(double StartX, double StartY, double EndX, double EndY, Line line) {
        line.setStartX(StartX);
        line.setStartY(StartY);
        line.setEndX(EndX);
        line.setEndY(EndY);
    }

    private void reLayoutBoxTemperatureLocation() {
        for (int i = 0; i < BoxTemperatureManager.getInstance().boxTemperatureList.size(); i++) {
            BoxTemperature boxTemperature = (BoxTemperature) BoxTemperatureManager.getInstance().boxTemperatureList.get(i);
            boolean maxClickPoint = boxTemperature.isMaxWindowDraw();
//            YLog.I("reLayoutBoxTemperatureLocation maxClickPoint :" + maxClickPoint +
//                    " size:" + BoxTemperatureManager.getInstance().boxTemperatureList.size() +
//                    " isMaxWindowDraw:" + boxTemperature.isMaxWindowDraw());
            if (!boxTemperature.getBoxTemperatureNodeMax().isEmpty()){
                ArrayList boxTemperatureNodeMaxList = boxTemperature.getBoxTemperatureNodeMax();
                reLayoutPoint(boxTemperatureNodeMaxList,maxClickPoint);
            }
            if (!boxTemperature.getBoxTemperatureNodeMin().isEmpty()){
                ArrayList boxTemperatureNodeMinList = boxTemperature.getBoxTemperatureNodeMin();
                reLayoutPoint(boxTemperatureNodeMinList,maxClickPoint);
            }
            if (!boxTemperature.getBoxTemperatureNodeAvg().isEmpty()){
                ArrayList boxTemperatureNodeAvgList = boxTemperature.getBoxTemperatureNodeAvg();
                Label label = (Label) boxTemperatureNodeAvgList.get(0);
                label.setTranslateX((boxTemperature.getStartLineX()*imageZoomRatio));
                label.setTranslateY((boxTemperature.getStartLineY()*imageZoomRatio-15));
            }
//            topLine = drawLine(startLineX, startLineY, endLineX, startLineY,Configs.white_color);
            reLayoutLine(boxTemperature.getStartLineX()*imageZoomRatio,boxTemperature.getStartLineY()*imageZoomRatio,
                    boxTemperature.getEndLineX()*imageZoomRatio,boxTemperature.getStartLineY()*imageZoomRatio,
                    boxTemperature.getTopLine());
//            bottomLine = drawLine(startLineX, endLineY, endLineX, endLineY,Configs.white_color);
            reLayoutLine(boxTemperature.getStartLineX()*imageZoomRatio,boxTemperature.getEndLineY()*imageZoomRatio,
                    boxTemperature.getEndLineX()*imageZoomRatio,boxTemperature.getEndLineY()*imageZoomRatio,
                    boxTemperature.getBottomLine());
//            leftLine = drawLine(startLineX, startLineY, startLineX, endLineY,Configs.white_color);
            reLayoutLine(boxTemperature.getStartLineX()*imageZoomRatio,boxTemperature.getStartLineY()*imageZoomRatio,
                    boxTemperature.getStartLineX()*imageZoomRatio,boxTemperature.getEndLineY()*imageZoomRatio,
                    boxTemperature.getLeftLine());
//            rightLine = drawLine(endLineX, startLineY, endLineX, endLineY,Configs.white_color);
            reLayoutLine(boxTemperature.getEndLineX()*imageZoomRatio,boxTemperature.getStartLineY()*imageZoomRatio,
                    boxTemperature.getEndLineX()*imageZoomRatio,boxTemperature.getEndLineY()*imageZoomRatio,
                    boxTemperature.getRightLine());



        }
    }

    public void reLayoutCurveTemperatureLocation(){
        for (int i = 0; i < CurveManager.getInstance().curveTemperatureList.size(); i++) {
            CurveTemperature curveTemperature = (CurveTemperature) CurveManager.getInstance().curveTemperatureList.get(i);
            boolean maxClickPoint = curveTemperature.isMaxWindowDraw();
            if (!curveTemperature.getCurveTemperatureNodeMax().isEmpty()){
                ArrayList curveTemperatureNodeMax = curveTemperature.getCurveTemperatureNodeMax();
                reLayoutPoint(curveTemperatureNodeMax,maxClickPoint);
            }
            if (!curveTemperature.getCurveTemperatureNodeMin().isEmpty()){
                ArrayList curveTemperatureNodeMin = curveTemperature.getCurveTemperatureNodeMin();
                reLayoutPoint(curveTemperatureNodeMin,maxClickPoint);
            }
            if (!curveTemperature.getCurveTemperatureNodeAvg().isEmpty()){
                ArrayList curveTemperatureNodeAvgList = curveTemperature.getCurveTemperatureNodeAvg();
                Label label = (Label) curveTemperatureNodeAvgList.get(0);
                label.setTranslateX((((OneLine)curveTemperature.getAllLine().get(0)).getStartX()*imageZoomRatio));
                label.setTranslateY((((OneLine)curveTemperature.getAllLine().get(0)).getStartY()*imageZoomRatio-15));
            }
            ArrayList allLine = curveTemperature.getAllLine();
            for (int j=0;j<allLine.size();j++){
                OneLine oneLine = (OneLine) allLine.get(j);
                reLayoutLine(oneLine.getStartX()*imageZoomRatio,oneLine.getStartY()*imageZoomRatio,
                        oneLine.getEndX()*imageZoomRatio,oneLine.getEndY()*imageZoomRatio,
                        oneLine.getLine());
            }
        }
    }

    public void reLayoutLineTemperatureLocation() {
        for (int i = 0; i < LineTemperManager.getInstance().lineTemperatureList.size(); i++) {
            LineTemperature lineTemperature = (LineTemperature) LineTemperManager.getInstance().lineTemperatureList.get(i);
            boolean maxClickPoint = lineTemperature.isMaxWindowDraw();
            if (!lineTemperature.getLineTemperatureNodeMax().isEmpty()) {
                ArrayList lineTemperatureNodeMax = lineTemperature.getLineTemperatureNodeMax();
                reLayoutPoint(lineTemperatureNodeMax, maxClickPoint);
            }
            if (!lineTemperature.getLineTemperatureNodeMin().isEmpty()) {
                ArrayList lineTemperatureNodeMin = lineTemperature.getLineTemperatureNodeMin();
                reLayoutPoint(lineTemperatureNodeMin, maxClickPoint);
            }
            OneLine oneLine = lineTemperature.getOneLine();
            reLayoutLine(oneLine.getStartX()*imageZoomRatio,oneLine.getStartY()*imageZoomRatio,
                    oneLine.getEndX()*imageZoomRatio,oneLine.getEndY()*imageZoomRatio,
                    oneLine.getLine());
        }
    }

    public void reLayoutCircleTemperatureLocation() {
        for (int i = 0; i < CircleTemperManager.getInstance().circleTemperatureList.size(); i++) {
            CircleTemperature circleTemperature = (CircleTemperature) CircleTemperManager.getInstance().circleTemperatureList.get(i);
            boolean maxClickPoint = circleTemperature.isMaxWindowDraw();
            if (!circleTemperature.getCircleTemperatureNodeMax().isEmpty()) {
                ArrayList circleTemperatureNodeMax = circleTemperature.getCircleTemperatureNodeMax();
                reLayoutPoint(circleTemperatureNodeMax, maxClickPoint);
            }
            if (!circleTemperature.getCircleTemperatureNodeMin().isEmpty()) {
                ArrayList circleTemperatureNodeMin = circleTemperature.getCircleTemperatureNodeMin();
                reLayoutPoint(circleTemperatureNodeMin, maxClickPoint);
            }
            OneCircle oneCircle = circleTemperature.getOneCircle();
            reLayoutCircle(oneCircle.getStartX()*imageZoomRatio,oneCircle.getStartY()*imageZoomRatio,
                    oneCircle.getRadius()*imageZoomRatio,oneCircle.getCircle());
        }
    }

    private void reLayoutCircle(double x, double y, double radius, Circle circle) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
    }


}
