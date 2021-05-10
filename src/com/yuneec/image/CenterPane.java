package com.yuneec.image;

import com.yuneec.image.module.ColorPalette;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
        ColorPalette
    }

    private int showImagePaneX, showImagePaneY;
    private float pointTemperature;
    private ImageView imageView;

    public int startLineX = 0;
    public int startLineY = 0;
    public int endLineX = 0;
    public int endLineY = 0;
    private Line topLine, bottomLine, leftLine, rightLine;

    public CenterSettingSelect centerSettingFlag = CenterSettingSelect.NONE;
    public Background centerSettingButtonUnclickBackground;
    public Background centerSettingButtonClickBackground;
    public Button SingleClickButton,BoxChooseButton,ColorPaletteButton,ClearButton;
    public ArrayList centerSettingButtonNodeList = new ArrayList();

    public ArrayList boxTemperatureNodeMax = new ArrayList();
    public ArrayList boxTemperatureNodeMin = new ArrayList();
    public ArrayList pointTemperatureNodeList = new ArrayList();

    private String rightXYlabel;

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
        showImagePaneY = (int) ((Configs.SceneHeight - Configs.MenuHeight - Configs.LineHeight) / 2
                - Global.currentOpenImageHeight / 2);
        showImagePane.setTranslateX(showImagePaneX);
        showImagePane.setTranslateY(showImagePaneY);
    }

    public void showImage() {
        resetShowImageDefault();
        ImageUtil.readImage(Global.currentOpenImagePath);
        RightPane.getInstance().showImageInfoToRightPane();

        Image image = new Image("file:" + Global.currentOpenImagePath);
        imageView = new ImageView(image);
        Global.currentOpenImageWidth = image.getWidth();
        Global.currentOpenImageHeight = image.getHeight();
        getImagePaneOffsetXY();
        showImagePane.getChildren().add(imageView);

//        ScaleImage.getInstance().init(Global.currentOpenImagePath);

        imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                // System.out.println("MouseEvent:" + s);
                pointTemperature = XMPUtil.getInstance().getTempera(x,y);
                rightXYlabel = "x = " + x + " y = " + y + " ,Temperature = ";
                String s = rightXYlabel + Utils.getFormatTemperature(pointTemperature);
                RightPane.getInstance().showXYlabel.setText(s);
            }
        });
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//				String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
//				System.out.println("MouseClicked:" + s);
                if(centerSettingFlag == CenterSettingSelect.POINT && Utils.mouseLeftClick(e)){
                    ArrayList pointNodeList = addLabelInImage((int) e.getX(), (int) e.getY(),pointTemperature,Configs.white_color);
                    pointTemperatureNodeList.add(pointNodeList);
                }
            }
        });

        imageView.setOnMouseDragged(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMouseDragged:" + s);
            if(centerSettingFlag == CenterSettingSelect.BOX){
                addRectangleForImage((int) event.getX(),(int) event.getY());
                showImagePane.getChildren().removeAll(boxTemperatureNodeMax);
                showImagePane.getChildren().removeAll(boxTemperatureNodeMin);
            }
        });

        imageView.setOnMousePressed(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMousePressed:" + s);
            startLineX = (int) event.getX();
            startLineY = (int) event.getY();
        });

        imageView.setOnMouseReleased(event->{
//            String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//            System.out.println("setOnMouseReleased:" + s);
            if (startLineX == (int) event.getX() && startLineY == (int) event.getY()){
                return;
            }
            if(centerSettingFlag == CenterSettingSelect.BOX){
                BoxTemperatureUtil.getInstance().init(startLineX, startLineY, endLineX, endLineY, new BoxTemperatureUtil.MaxMinTemperature() {
                    @Override
                    public void onResust(float maxTemperature, int[] maxTemperatureXY, float minTemperature, int[] minTemperatureXY) {
                        boxTemperatureNodeMax = addLabelInImage(maxTemperatureXY[0],maxTemperatureXY[1],maxTemperature,Configs.red_color);
                        boxTemperatureNodeMin = addLabelInImage(minTemperatureXY[0],minTemperatureXY[1],minTemperature,Configs.blue2_color);
                    }
                });
            }
        });
    }

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
        showImagePane.getChildren().removeAll(topLine, bottomLine, leftLine, rightLine);
        topLine = drawLine(startLineX, startLineY, x, startLineY,Configs.white_color);
        bottomLine = drawLine(startLineX, y, x, y,Configs.white_color);
        leftLine = drawLine(startLineX, startLineY, startLineX, y,Configs.white_color);
        rightLine = drawLine(x, startLineY, x, y,Configs.white_color);
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

    private ArrayList addLabelInImage(int x, int y ,float temperature,String color) {
        ArrayList onePointTemperatureNode = new ArrayList();
        Label label = new Label();
//        int num = new Random().nextInt(100) - 50;
        label.setText(Utils.getFormatTemperature(temperature));
        label.setTextFill(Color.web(Configs.white_color));
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

    private void initCenterPane() {
        centerPane = new FlowPane();
        centerPane.setPrefWidth(Configs.CenterPanelWidth);
        centerPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));

        centerSettingPane = new FlowPane();
        centerSettingPane.setPrefHeight(Configs.LineHeight);
        centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        centerSettingPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
        centerPane.getChildren().add(centerSettingPane);

        initCenterSettingPane(centerSettingPane);

        centerImagePane = new Pane();
        centerImagePane.setPrefWidth(Configs.CenterPanelWidth);
        centerImagePane.setPrefHeight(Configs.SceneHeight - Configs.MenuHeight - Configs.LineHeight - 1);
        centerImagePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        centerPane.getChildren().add(centerImagePane);

        showImagePane = new Pane();
        showImagePane.setPrefWidth(Configs.DefaultImageWidth);
        showImagePane.setPrefHeight(Configs.DefaultImageHeight);
        centerImagePane.getChildren().add(showImagePane);

        Global.hBox.getChildren().add(centerPane);
    }

    private void initCenterSettingPane(Pane centerSettingPane) {
        centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.lightGray_color), new CornerRadii(5), new Insets(1)));
        centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), new CornerRadii(5), new Insets(1)));

        SingleClickButton = creatSettingButton("image/center_click.png",null);
        SingleClickButton.setTranslateX(20);

        BoxChooseButton = creatSettingButton("image/box_choose.png",null);
        BoxChooseButton.setTranslateX(30);

        ColorPaletteButton = creatSettingButton("image/color_palette.png",null);
        ColorPaletteButton.setTranslateX(40);
        ColorPalette.getInstance().init();

        ClearButton = creatSettingButton("image/clear.png",null);
        ClearButton.setTranslateX(50);

        SingleClickButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    System.out.println("initCenterSettingPane SingleClickButton MouseClicked ...");
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
//                    System.out.println("initCenterSettingPane BoxChooseButton MouseClicked ...");
                    setButtonClickBackground(centerSettingButtonNodeList,BoxChooseButton);
                    centerSettingFlag = CenterSettingSelect.BOX;
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

        centerSettingButtonNodeList.clear();
        centerSettingButtonNodeList.add(SingleClickButton);
        centerSettingButtonNodeList.add(BoxChooseButton);
        centerSettingButtonNodeList.add(ColorPaletteButton);
        centerSettingButtonNodeList.add(ClearButton);
        centerSettingPane.getChildren().add(SingleClickButton);
        centerSettingPane.getChildren().add(BoxChooseButton);
        centerSettingPane.getChildren().add(ColorPaletteButton);
        centerSettingPane.getChildren().add(ClearButton);
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
        button.setTranslateY(5);
        button.setPrefWidth(80);
        button.setTextFill(Paint.valueOf(Configs.grey_color));
        if(imagePath != null){
            ImageView imageView = new ImageView(new Image(imagePath));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button.setGraphic(imageView);
        }
        button.setBackground(centerSettingButtonUnclickBackground);
        Border border = new Border(new BorderStroke(Paint.valueOf(Configs.blue_color),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1.5)));
        button.setBorder(border);
        return button;
    }

    private void resetShowImageDefault(){
        setButtonClickBackground(centerSettingButtonNodeList,null);
        centerSettingFlag = CenterSettingSelect.NONE;
        ColorPalette.getInstance().centerSettingColorPalettePaneAdded = false;
        ColorPalette.getInstance().dmissColorPalettePane();
        for (int i =0;i < pointTemperatureNodeList.size();i++){
            ArrayList pointNodeList = (ArrayList)pointTemperatureNodeList.get(i);
            showImagePane.getChildren().removeAll(pointNodeList);
        }
        showImagePane.getChildren().removeAll(topLine, bottomLine, leftLine, rightLine);
        showImagePane.getChildren().removeAll(boxTemperatureNodeMax);
        showImagePane.getChildren().removeAll(boxTemperatureNodeMin);
        pointTemperatureNodeList.clear();
        boxTemperatureNodeMax.clear();
        boxTemperatureNodeMin.clear();
    }

    public void transitionTemperature(){
        for (int i=0;i<pointTemperatureNodeList.size();i++){
            ArrayList pointNodeList = (ArrayList) pointTemperatureNodeList.get(i);
            Label label = (Label) pointNodeList.get(0);
            float temperature = (float) pointNodeList.get(4);
            label.setText(Utils.getFormatTemperature(temperature));
        }
        if (!boxTemperatureNodeMax.isEmpty()){
            ((Label)boxTemperatureNodeMax.get(0)).setText(Utils.getFormatTemperature((float) boxTemperatureNodeMax.get(4)));
        }
        if (!boxTemperatureNodeMin.isEmpty()){
            ((Label)boxTemperatureNodeMin.get(0)).setText(Utils.getFormatTemperature((float) boxTemperatureNodeMin.get(4)));
        }
        if (Global.currentOpenImagePath != null){
            RightPane.getInstance().showXYlabel.setText(rightXYlabel + Utils.getFormatTemperature(pointTemperature));
        }
    }

}
