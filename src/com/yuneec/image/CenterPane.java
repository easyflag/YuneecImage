package com.yuneec.image;

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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class CenterPane {

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

    private int imageX, imageY;

    public void getImageOffsetXY(){
        imageX = (int) (Configs.CenterPanelWidth / 2 - Global.currentOpenImageWidth / 2);
        imageY = (int) ((Configs.SceneHeight - Configs.MenuHeight - Configs.LineHeight) / 2
                - Global.currentOpenImageHeight / 2);
        if (imageView != null){
            imageView.setTranslateX(imageX);
            imageView.setTranslateY(imageY);
        }
    }

    double pointTemperature;
    public ImageView imageView;
    public void showImage() {
        ImageUtil.readImage(Global.currentOpenImagePath);
        RightPane.getInstance().showImageInfoToRightPane();
        resetCenterSetting();
        centerSettingColorPalettePaneAdded = false;
        centerImagePane.getChildren().clear();
        Image image = new Image("file:" + Global.currentOpenImagePath);
        imageView = new ImageView(image);
        Global.currentOpenImageWidth = image.getWidth();
        Global.currentOpenImageHeight = image.getHeight();
        getImageOffsetXY();
        centerImagePane.getChildren().add(imageView);

//        ScaleImage.getInstance().init(Global.currentOpenImagePath);

        imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                // System.out.println("MouseEvent:" + s);
                pointTemperature = XMPUtil.getInstance().getTempera(x,y);
                String s = "x = " + x + " y = " + y + " ,Temperature = " + String.format("%1.2f", pointTemperature);
                RightPane.getInstance().showXYlabel.setText(s);
            }
        });
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//				String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
                // System.out.println("MouseClicked:" + s);
                if(centerSettingFlag == 1 && Utils.mouseLeftClick(e)){
                    addLabelInImage((int) e.getX(), (int) e.getY());
                }
            }
        });

        imageView.setOnMouseDragged(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMouseDragged:" + s);
            if(centerSettingFlag == 2){
                addRectangleForImage((int) event.getX(),(int) event.getY());
            }
        });

        imageView.setOnMousePressed(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMousePressed:" + s);
            startLineX = (int) event.getX();
            startLineY = (int) event.getY();

        });

        imageView.setOnMouseReleased(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMouseReleased:" + s);
        });
    }

    private int startLineX = 0;
    private int startLineY = 0;
    private Line topLine, bottomLine, leftLine, rightLine;

    private void addRectangleForImage(int x, int y) {
        centerImagePane.getChildren().removeAll(topLine, bottomLine, leftLine, rightLine);
        topLine = drawLine(startLineX, startLineY, x, startLineY,Configs.white_color,imageX,imageY);
        bottomLine = drawLine(startLineX, y, x, y,Configs.white_color,imageX,imageY);
        leftLine = drawLine(startLineX, startLineY, startLineX, y,Configs.white_color,imageX,imageY);
        rightLine = drawLine(x, startLineY, x, y,Configs.white_color,imageX,imageY);
        centerImagePane.getChildren().addAll(topLine, bottomLine, leftLine, rightLine);
    }

    public Line drawLine(int StartX, int StartY, int EndX, int EndY,
                          String color, double translateX, double translateY) {
        Line line = new Line();
        line.setStrokeWidth(1);
        line.setStroke(Paint.valueOf(color));
        line.setStartX(StartX);
        line.setStartY(StartY);
        line.setEndX(EndX);
        line.setEndY(EndY);
        line.setTranslateX(translateX);
        line.setTranslateY(translateY);
        return line;
    }

    private void addLabelInImage(int x, int y) {
        Label label = new Label();
//        int num = new Random().nextInt(100) - 50;
        label.setText(String.format("%1.2f", pointTemperature) + "℃");
        label.setTextFill(Color.web(Configs.white_color));
        label.setTranslateX(x + imageX + 7);
        label.setTranslateY(y + imageY - 8);
        centerImagePane.getChildren().add(label);
        Circle circle = new Circle();
        circle.setFill(Color.web(Configs.white_color));
        circle.setCenterX(x + imageX);
        circle.setCenterY(y + imageY);
        circle.setRadius(3.0f);
        centerImagePane.getChildren().add(circle);
        int lineLEN = 7;
        Line xLine = drawLine(x-lineLEN,y,x+lineLEN,y,Configs.white_color,imageX,imageY);
        Line yLine = drawLine(x,y-lineLEN,x,y+lineLEN,Configs.white_color,imageX,imageY);
        centerImagePane.getChildren().addAll(xLine,yLine);
    }

    public Pane centerImagePane;
    public FlowPane centerPane;
    public Pane centerSettingPane;
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

        Global.hBox.getChildren().add(centerPane);
    }

    private int centerSettingFlag ;
    private Background centerSettingButtonUnclickBackground;
    private Background centerSettingButtonClickBackground;
    private Button SingleClickButton,BoxChooseButton,ColorPaletteButton;
    private void initCenterSettingPane(Pane centerSettingPane) {
        centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.lightGray_color), new CornerRadii(5), new Insets(1)));
        centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), new CornerRadii(5), new Insets(1)));

        SingleClickButton = creatSettingButton("image/center_click.png",null);
        SingleClickButton.setTranslateX(20);

        BoxChooseButton = creatSettingButton("image/box_choose.png",null);
        BoxChooseButton.setTranslateX(30);

        ColorPaletteButton = creatSettingButton("image/color_palette.png",null);
        ColorPaletteButton.setTranslateX(40);

        SingleClickButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    System.out.println("initCenterSettingPane SingleClickButton MouseClicked ...");
                    SingleClickButton.setBackground(centerSettingButtonClickBackground);
                    BoxChooseButton.setBackground(centerSettingButtonUnclickBackground);
                    ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
                    centerSettingFlag = 1;
                    dmissColorPalettePane();
                }
            }
        });

        BoxChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    System.out.println("initCenterSettingPane BoxChooseButton MouseClicked ...");
                    BoxChooseButton.setBackground(centerSettingButtonClickBackground);
                    SingleClickButton.setBackground(centerSettingButtonUnclickBackground);
                    ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
                    centerSettingFlag = 2;
                    dmissColorPalettePane();
                }
            }
        });

        ColorPaletteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    System.out.println("initCenterSettingPane ColorPaletteButton MouseClicked ...");
                    SingleClickButton.setBackground(centerSettingButtonUnclickBackground);
                    BoxChooseButton.setBackground(centerSettingButtonUnclickBackground);
                    centerSettingFlag = 3;
                    if(centerSettingColorPalettePaneAdded){
                        dmissColorPalettePane();
                        ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
                    }else {
                        showColorPalettePane();
                        ColorPaletteButton.setBackground(centerSettingButtonClickBackground);
                    }
                }
            }
        });

        centerSettingPane.getChildren().add(SingleClickButton);
        centerSettingPane.getChildren().add(BoxChooseButton);
        centerSettingPane.getChildren().add(ColorPaletteButton);
    }

    private Pane centerSettingColorPalettePane;
    private boolean centerSettingColorPalettePaneAdded = false;
    private void showColorPalettePane() {
        if(centerSettingColorPalettePaneAdded){
            return;
        }
        centerSettingColorPalettePane = new FlowPane();
        centerSettingColorPalettePane.setPrefHeight(300);
        centerSettingColorPalettePane.setPrefWidth(100);
        centerSettingColorPalettePane.setTranslateX(10);
        centerSettingColorPalettePane.setTranslateY(10);
        centerSettingColorPalettePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), new CornerRadii(5), null)));
        centerImagePane.getChildren().add(centerSettingColorPalettePane);
        centerSettingColorPalettePaneAdded = true;
        addColorPaletteButton();
    }

    private void dmissColorPalettePane() {
        centerImagePane.getChildren().remove(centerSettingColorPalettePane);
        centerSettingColorPalettePaneAdded = false;
    }

    private ArrayList<Button> colorPaletteButtonList = new ArrayList<Button>();
    private void addColorPaletteButton() {
        Button whiteHotButton = creatSettingButton(null,"WhiteHot");
        whiteHotButton.setTranslateY(10);
        centerSettingColorPalettePane.getChildren().add(whiteHotButton);
        colorPaletteButtonList.add(whiteHotButton);
        Button FulguriteButton = creatSettingButton(null,"Fulgurite");
        FulguriteButton.setTranslateY(20);
        centerSettingColorPalettePane.getChildren().add(FulguriteButton);
        colorPaletteButtonList.add(FulguriteButton);
        Button IronRedButton = creatSettingButton(null,"IronRed");
        IronRedButton.setTranslateY(30);
        centerSettingColorPalettePane.getChildren().add(IronRedButton);
        colorPaletteButtonList.add(IronRedButton);
        Button HotIronButton = creatSettingButton(null,"HotIron");
        HotIronButton.setTranslateY(40);
        centerSettingColorPalettePane.getChildren().add(HotIronButton);
        colorPaletteButtonList.add(HotIronButton);
        Button MedicalButton = creatSettingButton(null,"Medical");
        MedicalButton.setTranslateY(50);
        centerSettingColorPalettePane.getChildren().add(MedicalButton);
        colorPaletteButtonList.add(MedicalButton);
        whiteHotButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setButtonClickBackground(colorPaletteButtonList,whiteHotButton);
            }
        });
        FulguriteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setButtonClickBackground(colorPaletteButtonList,FulguriteButton);
            }
        });
        IronRedButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setButtonClickBackground(colorPaletteButtonList,IronRedButton);
            }
        });
        HotIronButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setButtonClickBackground(colorPaletteButtonList,HotIronButton);
            }
        });
        MedicalButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setButtonClickBackground(colorPaletteButtonList,MedicalButton);
//                new ScaleImage().start(new Stage());
            }
        });
    }

    private void setButtonClickBackground(ArrayList<Button> colorPaletteButtonList,Button clickButton) {
        for(Button button:colorPaletteButtonList){
            if(button == clickButton){
                button.setBackground(centerSettingButtonClickBackground);
            }else{
                button.setBackground(centerSettingButtonUnclickBackground);
            }
        }
    }

    private Button creatSettingButton(String imagePath,String text) {
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

    private void resetCenterSetting() {
        SingleClickButton.setBackground(centerSettingButtonUnclickBackground);
        BoxChooseButton.setBackground(centerSettingButtonUnclickBackground);
        ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
        centerSettingFlag = 0;
    }


}
