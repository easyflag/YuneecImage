package com.yuneec.image;

import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.box.BoxTemperatureUtil;
import com.yuneec.image.module.circle.CircleTemperManager;
import com.yuneec.image.module.colorpalette.ColorPalette;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.colorpalette.ColorPaletteManager;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import com.yuneec.image.module.line.LineTemperManager;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CenterPane {

    public FlowPane centerPane;
    public Pane centerSettingPane;
    public Pane centerImagePane;
    public Pane showImagePane; //640*512

    public enum CenterSettingSelect {
        NONE,
        CLEAR,
        POINT,
        BOX,
        CURVE,
        LINE,
        CIRCLE,
        ColorPalette,
        Undo,
        ColorPicker
    }

    private int showImagePaneX, showImagePaneY;
    public float pointTemperature;
    public ImageView imageView;

    private int startLineX = 0;
    private int startLineY = 0;
    private int endLineX = 0;
    private int endLineY = 0;
    private Line topLine, bottomLine, leftLine, rightLine;

    public CenterSettingSelect centerSettingFlag = CenterSettingSelect.NONE;
    public Background centerSettingButtonUnclickBackground;
    public Background centerSettingButtonClickBackground;
    public Button SingleClickButton,BoxChooseButton,CurveChooseButton,LineChooseButton,CircleChooseButton,
            ColorPaletteButton,ClearButton,UndoButton;
    public ColorPicker ColorPickerButton;
    public ArrayList centerSettingButtonNodeList = new ArrayList();

    public String rightXYlabel;

    private static CenterPane instance;
    public static CenterPane getInstance() {
        if (instance == null) {
            instance = new CenterPane();
        }
        return instance;
    }

    public void init() {
        initCenterPane();
    }

    public void getImagePaneOffsetXY(){
        showImagePaneX = (int) (Configs.CenterPanelWidth / 2 - Global.currentOpenImageWidth / 2);
        showImagePaneY = (int) (Configs.CenterPanelHeight / 2 - Global.currentOpenImageHeight / 2);
        showImagePane.setTranslateX(showImagePaneX);
        showImagePane.setTranslateY(showImagePaneY - 10);
//        YLog.I("getImagePaneOffsetXY " +  " showImagePaneX:" + showImagePaneX + "  ,showImagePaneY:" + showImagePaneY);
    }

    public void reset(){
        if (showImagePane != null){
            showImagePane.getChildren().remove(imageView);
            imageView = null;
            resetShowImageDefault();
        }
    }

    public void showImage() {
        resetShowImageDefault();
        try {
            ParseTemperatureBytes.getInstance().init(ImageUtil.readJpgToByte(Global.currentOpenImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!Global.hasTemperatureBytes){
            ToastUtil.toast(Language.getString("No Temperature Info !","该图片无温度数据!"),new int[]{70,0});
            return;
        }

        Image image = new Image("file:" + Global.currentOpenImagePath);
        if (imageView == null){
            imageView = new ImageView(image);
            Global.currentOpenImageWidth = image.getWidth();
            Global.currentOpenImageHeight = image.getHeight();
            getImagePaneOffsetXY();
            showImagePane.getChildren().add(imageView);
        }else {
            imageView.setViewport(new Rectangle2D(0, 0, 640, 512));
            imageView.setImage(image);
        }

        ColorPaletteManager.I().saveColorPaletteRGB();

//        ScaleImage.getInstance().init(Global.currentOpenImagePath);

        imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                // YLog.I("MouseEvent:" + s);
                pointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                String ts = Language.getString(" , "+Language.Temperature_en+" = " , " ,"+Language.Temperature_ch+" = ");
                rightXYlabel = "x : " + x + " , y : " + y + ts;
                String s = rightXYlabel + Utils.getFormatTemperature(pointTemperature);
                RightPane.getInstance().showXYlabel.setText(s);
            }
        });
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//				String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
//				YLog.I("MouseClicked:" + s);
                PointManager.getInstance().setMouseMouseClickdXY(e, CurveManager.MouseStatus.MouseClicked);
            }
        });

        imageView.setOnMouseDragged(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			YLog.I("setOnMouseDragged:" + s);
            if(centerSettingFlag == CenterSettingSelect.BOX){
                addRectangleForImage((int) event.getX(),(int) event.getY());
                //not remove to draw more rectangle
//                showImagePane.getChildren().removeAll(boxTemperatureNodeMax);
//                showImagePane.getChildren().removeAll(boxTemperatureNodeMin);
                MouseReleased = false;
            }
            CurveManager.getInstance().setMouseMousePressedXY((int) event.getX(),(int) event.getY(), CurveManager.MouseStatus.MouseDragged);
            BoxTemperatureManager.getInstance().setMouseMousePressedXY((int) event.getX(),(int) event.getY(), CurveManager.MouseStatus.MouseDragged);
        });

        imageView.setOnMousePressed(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			YLog.I("setOnMousePressed:" + s);
            startLineX = (int) event.getX();
            startLineY = (int) event.getY();
            CurveManager.getInstance().setMouseMousePressedXY((int) event.getX(),(int) event.getY(), CurveManager.MouseStatus.MousePressed);
            BoxTemperatureManager.getInstance().setMouseMousePressedXY((int) event.getX(),(int) event.getY(), CurveManager.MouseStatus.MousePressed);
        });

        imageView.setOnMouseReleased(event->{
//            String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//            YLog.I("setOnMouseReleased:" + s);
            if (startLineX == (int) event.getX() && startLineY == (int) event.getY()){
                return;
            }
            if(centerSettingFlag == CenterSettingSelect.BOX){
                MouseReleased = true;
                if (startLineX == endLineX || startLineY == endLineY){
                    ToastUtil.toast(Language.getString("that isn't a rectangle !","不是矩形 !"),new int[]{70,0});
                    showImagePane.getChildren().removeAll(topLine, bottomLine, leftLine, rightLine);
                    return;
                }
                BoxTemperatureUtil.getInstance().init(startLineX, startLineY, endLineX, endLineY, new BoxTemperatureUtil.MaxMinTemperature() {
                    @Override
                    public void onResust(float maxTemperature, int[] maxTemperatureXY, float minTemperature, int[] minTemperatureXY, float avgTemperature, int[] avgTemperatureXY) {
                        ArrayList boxTemperatureNodeMax = addLabelInImage(maxTemperatureXY[0],maxTemperatureXY[1],maxTemperature,Configs.red_color);
                        ArrayList boxTemperatureNodeMin = addLabelInImage(minTemperatureXY[0],minTemperatureXY[1],minTemperature,Configs.blue2_color);
                        ArrayList boxTemperatureNodeAvg = addAvgLabelInImage(avgTemperatureXY[0],avgTemperatureXY[1],avgTemperature,Configs.white_color);
                        BoxTemperature boxTemperature = new BoxTemperature(startLineX, startLineY, endLineX, endLineY,
                                topLine,bottomLine,leftLine,rightLine, boxTemperatureNodeMax,boxTemperatureNodeMin,boxTemperatureNodeAvg);
                        BoxTemperatureManager.getInstance().addBoxTemperature(boxTemperature);
                    }
                });
            }
            CurveManager.getInstance().setMouseMousePressedXY((int) event.getX(),(int) event.getY(), CurveManager.MouseStatus.MouseReleased);
            BoxTemperatureManager.getInstance().setMouseMousePressedXY((int) event.getX(),(int) event.getY(), CurveManager.MouseStatus.MouseReleased);
        });
    }

    private boolean MouseReleased;

    private void addRectangleForImage(int x, int y) {
        if (x > Global.currentOpenImageWidth){
            x = (int) Global.currentOpenImageWidth;
        }
        if (x < 0){
            x = 0;
        }
        if (y > Global.currentOpenImageHeight){
            y = (int) Global.currentOpenImageHeight;
        }
        if (y < 0){
            y = 0;
        }
        endLineX = x;
        endLineY = y;
        if (!MouseReleased){
            showImagePane.getChildren().removeAll(topLine, bottomLine, leftLine, rightLine);
        }
        topLine = drawLine(startLineX, startLineY, endLineX, startLineY,Configs.temperatureColor);
        bottomLine = drawLine(startLineX, endLineY, endLineX, endLineY,Configs.temperatureColor);
        leftLine = drawLine(startLineX, startLineY, startLineX, endLineY,Configs.temperatureColor);
        rightLine = drawLine(endLineX, startLineY, endLineX, endLineY,Configs.temperatureColor);
        showImagePane.getChildren().addAll(topLine, bottomLine, leftLine, rightLine);
    }

    public Line drawLine(int StartX, int StartY, int EndX, int EndY, String color) {
        Line line = new Line();
        line.setStrokeWidth(1);
        line.setStroke(Paint.valueOf(color));
        line.setStartX(StartX);
        line.setStartY(StartY);
        line.setEndX(EndX);
        line.setEndY(EndY);
        return line;
    }

    public ArrayList addLabelInImage(int x, int y ,float temperature,String color) {
        ArrayList onePointTemperatureNode = new ArrayList();
        Label label = new Label();
//        int num = new Random().nextInt(100) - 50;
        label.setText(Utils.getFormatTemperature(temperature));
        label.setTextFill(Color.web(Configs.temperatureColor));
        label.setTranslateX(x + 7);
        label.setTranslateY(y - 8);
        showImagePane.getChildren().add(label);
        Circle circle = new Circle();
        circle.setStroke(Color.web(color));
        circle.setFill(Color.TRANSPARENT);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(3.5f);
        showImagePane.getChildren().add(circle);
        int lineLEN = 7;
        Line xLine = drawLine(x-lineLEN,y,x+lineLEN,y,color);
        Line yLine = drawLine(x,y-lineLEN,x,y+lineLEN,color);
        showImagePane.getChildren().addAll(xLine,yLine);
        onePointTemperatureNode.add(label);
        onePointTemperatureNode.add(circle);
        onePointTemperatureNode.add(xLine);
        onePointTemperatureNode.add(yLine);
        onePointTemperatureNode.add(temperature);//original temperature ℃
        onePointTemperatureNode.add(x);
        onePointTemperatureNode.add(y);
        return onePointTemperatureNode;
    }

    public ArrayList addAvgLabelInImage(int x, int y ,float temperature,String color) {
        ArrayList onePointTemperatureNode = new ArrayList();
        Label label = new Label();
//        int num = new Random().nextInt(100) - 50;
        label.setText("Avg:"+Utils.getFormatTemperature(temperature));
        label.setTextFill(Color.web(Configs.temperatureColor));
        label.setTranslateX(x);
        y = y-15;
        label.setTranslateY(y);
        showImagePane.getChildren().add(label);
        Circle circle = new Circle();
        circle.setStroke(Color.web(color));
        circle.setFill(Color.TRANSPARENT);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(3.5f);
//        showImagePane.getChildren().add(circle);
        int lineLEN = 7;
        Line xLine = drawLine(x-lineLEN,y,x+lineLEN,y,color);
        Line yLine = drawLine(x,y-lineLEN,x,y+lineLEN,color);
//        showImagePane.getChildren().addAll(xLine,yLine);
        onePointTemperatureNode.add(label);
        onePointTemperatureNode.add(circle);
        onePointTemperatureNode.add(xLine);
        onePointTemperatureNode.add(yLine);
        onePointTemperatureNode.add(temperature);//original temperature ℃
        onePointTemperatureNode.add(x);
        onePointTemperatureNode.add(y);
        return onePointTemperatureNode;
    }

    private void initCenterPane() {
        centerPane = new FlowPane();
        centerPane.setPrefWidth(Configs.CenterPanelWidth);
        centerPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.white_color), null, null)));

        centerSettingPane = new FlowPane();
        centerSettingPane.setPrefHeight(Configs.LineHeight);
        centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        centerSettingPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.light_gray), null, null)));
        centerPane.getChildren().add(centerSettingPane);

        centerImagePane = new Pane();
        centerImagePane.setPrefWidth(Configs.CenterPanelWidth);
        centerImagePane.setPrefHeight(Configs.SceneHeight - Configs.MenuHeight - Configs.LineHeight);
        centerImagePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.white_color), null, null)));
        centerPane.getChildren().add(centerImagePane);
//        MouseWheelScroll.I().init();

        showImagePane = new Pane();
        showImagePane.setPrefWidth(Configs.DefaultImageWidth);
        showImagePane.setPrefHeight(Configs.DefaultImageHeight);
//        showImagePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.green_color), null, null)));
        centerImagePane.getChildren().add(showImagePane);
        initCenterSettingPane(centerSettingPane);

        Global.hBox.getChildren().add(centerPane);
    }

    private void initCenterSettingPane(Pane centerSettingPane) {
        centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.white_color), new CornerRadii(5), new Insets(1)));
        centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.light_gray), new CornerRadii(5), new Insets(1)));

        int translateX = 20;
        SingleClickButton = creatSettingButton("image/center_click.png",null);
        SingleClickButton.setTranslateX(translateX);
        translateX +=10;
        BoxChooseButton = creatSettingButton("image/box_choose.png",null);
        BoxChooseButton.setTranslateX(translateX);
        translateX +=10;
        CurveChooseButton = creatSettingButton("image/center_curve.png",null);
        CurveChooseButton.setTranslateX(translateX);
//        translateX +=10;
        LineChooseButton = creatSettingButton("image/center_line.png",null);
        LineChooseButton.setTranslateX(translateX);
//        translateX +=10;
        CircleChooseButton = creatSettingButton("image/center_circle.png",null);
        CircleChooseButton.setTranslateX(translateX);
        translateX +=10;
        ColorPaletteButton = creatSettingButton("image/color_palette.png",null);
        ColorPaletteButton.setTranslateX(translateX);
        ColorPalette.getInstance().init();
        translateX +=10;
        ClearButton = creatSettingButton("image/clear.png",null);
        ClearButton.setTranslateX(translateX);
        translateX +=10;
        UndoButton = creatSettingButton("image/undo.png",null);
        UndoButton.setTranslateX(translateX);
        translateX +=12;
        ColorPickerButton = new ColorPicker();
        ColorPickerButton.setTranslateX(translateX);
        ColorPickerButton.setBackground(centerSettingButtonUnclickBackground);
        ColorPickerButton.setPrefWidth(50);
        ColorPickerButton.setPrefHeight(30);
        ColorPickerButton.setTranslateY(4);
        Border border = new Border(new BorderStroke(Paint.valueOf(Configs.light_black),BorderStrokeStyle.SOLID,new CornerRadii(3),new BorderWidths(1)));
        ColorPickerButton.setBorder(border);
        ColorPickerButton.setOnAction((ActionEvent t) -> {
            ColorPickerManager.I().setColor(ColorPickerButton.getValue().toString());
        });

        setTooltip();
        SingleClickButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    YLog.I("initCenterSettingPane SingleClickButton MouseClicked ...");
                    setButtonClickBackground(centerSettingButtonNodeList,SingleClickButton);
                    centerSettingFlag = CenterSettingSelect.POINT;
                    ColorPalette.getInstance().dmissColorPalettePane();
                }
            }
        });

        BoxChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    YLog.I("initCenterSettingPane BoxChooseButton MouseClicked ...");
                    setButtonClickBackground(centerSettingButtonNodeList,BoxChooseButton);
                    centerSettingFlag = CenterSettingSelect.BOX;
                    ColorPalette.getInstance().dmissColorPalettePane();
                }
            }
        });

        CurveChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
                    setButtonClickBackground(centerSettingButtonNodeList,CurveChooseButton);
                    centerSettingFlag = CenterSettingSelect.CURVE;
                    ColorPalette.getInstance().dmissColorPalettePane();
                }
            }
        });

        LineChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
                    setButtonClickBackground(centerSettingButtonNodeList,LineChooseButton);
                    centerSettingFlag = CenterSettingSelect.LINE;
                    ColorPalette.getInstance().dmissColorPalettePane();
                }
            }
        });

        CircleChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
                    setButtonClickBackground(centerSettingButtonNodeList,CircleChooseButton);
                    centerSettingFlag = CenterSettingSelect.CIRCLE;
                    ColorPalette.getInstance().dmissColorPalettePane();
                }
            }
        });

        ClearButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
                    centerSettingFlag = CenterSettingSelect.CLEAR;
                    setButtonClickBackground(centerSettingButtonNodeList,ClearButton);
                    TimerTask task= new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    resetShowImageDefault();
                                }
                            });
                        }
                    };
                    Timer timer=new Timer();
                    timer.schedule(task,120);
                }
            }
        });

        UndoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
                    centerSettingFlag = CenterSettingSelect.Undo;
                    ColorPalette.getInstance().dmissColorPalettePane();
                    setButtonClickBackground(centerSettingButtonNodeList,UndoButton);
                    TimerTask task= new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    UndoButton.setBackground(centerSettingButtonUnclickBackground);
                                    BackStepManager.getInstance().backStep();
                                }
                            });
                        }
                    };
                    Timer timer=new Timer();
                    timer.schedule(task,120);
                }
            }
        });

        ColorPickerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
                    setButtonClickBackground(centerSettingButtonNodeList,null);
                    centerSettingFlag = CenterSettingSelect.ColorPicker;
                    ColorPalette.getInstance().dmissColorPalettePane();
                }
            }
        });

        centerSettingButtonNodeList.clear();
        centerSettingButtonNodeList.add(SingleClickButton);
        centerSettingButtonNodeList.add(BoxChooseButton);
        centerSettingButtonNodeList.add(CurveChooseButton);
        centerSettingButtonNodeList.add(LineChooseButton);
        centerSettingButtonNodeList.add(CircleChooseButton);
        centerSettingButtonNodeList.add(ColorPaletteButton);
        centerSettingButtonNodeList.add(ClearButton);
        centerSettingButtonNodeList.add(UndoButton);
//        centerSettingButtonNodeList.add(ColorPickerButton);
        centerSettingPane.getChildren().add(SingleClickButton);
        centerSettingPane.getChildren().add(BoxChooseButton);
        centerSettingPane.getChildren().add(CurveChooseButton);
//        centerSettingPane.getChildren().add(LineChooseButton);
//        centerSettingPane.getChildren().add(CircleChooseButton);
        centerSettingPane.getChildren().add(ColorPaletteButton);
        centerSettingPane.getChildren().add(ClearButton);
        centerSettingPane.getChildren().add(UndoButton);
        centerSettingPane.getChildren().add(ColorPickerButton);
    }

    private Tooltip getTooltip(String info) {
        Tooltip tooltip = new Tooltip(info);
        tooltip.setFont(new Font(12));
        tooltip.setWrapText(true);
        tooltip.setOpacity(0.8);
        return tooltip;
    }

    public void setTooltip(){
        if (showImagePane == null){
            return;
        }
        SingleClickButton.setTooltip(getTooltip(Language.getString(Language.SinglePointTemperature_en,Language.SinglePointTemperature_ch)));
        BoxChooseButton.setTooltip(getTooltip(Language.getString(Language.BoxTemperature_en,Language.BoxTemperature_ch)));
        CurveChooseButton.setTooltip(getTooltip(Language.getString(Language.CurveTemperature_en,Language.CurveTemperature_ch)));
        LineChooseButton.setTooltip(getTooltip(Language.getString(Language.CurveTemperature_en,Language.CurveTemperature_ch)));
        CircleChooseButton.setTooltip(getTooltip(Language.getString(Language.CircleTemperature_en,Language.CircleTemperature_ch)));
        ColorPaletteButton.setTooltip(getTooltip(Language.getString(Language.ColorPaletteTip_en,Language.ColorPaletteTip_ch)));
        ClearButton.setTooltip(getTooltip(Language.getString(Language.ClearTip_en,Language.ClearTip_ch)));
        UndoButton.setTooltip(getTooltip(Language.getString(Language.UndoTip_en,Language.UndoTip_ch)));
    }

    public void setButtonClickBackground(ArrayList<Button> buttonNodeList,Button clickButton) {
        for(Button button:buttonNodeList){
            if(button == clickButton){
                button.setBackground(centerSettingButtonClickBackground);
            }else{
                button.setBackground(centerSettingButtonUnclickBackground);
            }
        }
    }

    public Button creatSettingButton(String imagePath,String text) {
        Button button = new Button();
        button.setText(text);
        button.setTranslateX(10);
        button.setTranslateY(4);
        button.setPrefWidth(80);
        button.setTextFill(Paint.valueOf(Configs.black));
        if(imagePath != null){
            ImageView imageView = new ImageView(new Image(imagePath));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button.setGraphic(imageView);
        }
        button.setBackground(centerSettingButtonUnclickBackground);
        Border border = new Border(new BorderStroke(Paint.valueOf(Configs.light_black),BorderStrokeStyle.SOLID,new CornerRadii(3),new BorderWidths(1.0)));
        button.setBorder(border);
        return button;
    }

    private void resetShowImageDefault(){
        setButtonClickBackground(centerSettingButtonNodeList,null);
        centerSettingFlag = CenterSettingSelect.NONE;
        ColorPalette.getInstance().centerSettingColorPalettePaneAdded = false;
        ColorPalette.getInstance().dmissColorPalettePane();
        PointManager.getInstance().pointTemperatureNodeList.clear();
        BoxTemperatureManager.getInstance().boxTemperatureList.clear();
        CurveManager.getInstance().curveTemperatureList.clear();
        LineTemperManager.getInstance().lineTemperatureList.clear();
        LineTemperManager.getInstance().lineList.clear();
        CircleTemperManager.getInstance().circleTemperatureList.clear();
        CircleTemperManager.getInstance().circleList.clear();
        BackStepManager.getInstance().temperatureInfoList.clear();
        showImagePane.getChildren().clear();
        if (imageView != null){
            showImagePane.getChildren().add(imageView);
        }
    }

}
