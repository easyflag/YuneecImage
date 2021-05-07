package com.yuneec.image;

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

public class CenterPane {

    public FlowPane centerPane;
    public Pane centerSettingPane;
    public Pane centerImagePane;
    public Pane showImagePane; //640*512

    private int showImagePaneX, showImagePaneY;
    private float pointTemperature;
    private ImageView imageView;

    private int startLineX = 0;
    private int startLineY = 0;
    private int endLineX = 0;
    private int endLineY = 0;
    private Line topLine, bottomLine, leftLine, rightLine;

    private int centerSettingFlag ;
    private Pane centerSettingColorPalettePane;
    private boolean centerSettingColorPalettePaneAdded = false;
    private Background centerSettingButtonUnclickBackground;
    private Background centerSettingButtonClickBackground;
    private Button SingleClickButton,BoxChooseButton,ColorPaletteButton;

    private ArrayList oneTemperatureDrawMax = new ArrayList();
    private ArrayList oneTemperatureDrawMin = new ArrayList();
    private ArrayList oneTemperatureDraw;

    public ArrayList<String[]> pointTemperatureDataList = new ArrayList<>();
    public String[] boxTemperatureData = null;

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
        ImageUtil.readImage(Global.currentOpenImagePath);
        RightPane.getInstance().showImageInfoToRightPane();
        resetCenterSetting();
        centerSettingColorPalettePaneAdded = false;
        showImagePane.getChildren().clear();
        pointTemperatureDataList.clear();
        boxTemperatureData = null;

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
                String s = "x = " + x + " y = " + y + " ,Temperature = " + getFormatTemperature(pointTemperature);
                RightPane.getInstance().showXYlabel.setText(s);
            }
        });
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//				String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
//				System.out.println("MouseClicked:" + s);
                if(centerSettingFlag == 1 && Utils.mouseLeftClick(e)){
                    addLabelInImage((int) e.getX(), (int) e.getY(),pointTemperature,Configs.white_color);
                    String[] pointTemperatureData = new String[]{String.valueOf((int) e.getX()), String.valueOf((int) e.getY()), getFormatTemperature(pointTemperature)};
                    pointTemperatureDataList.add(pointTemperatureData);
                }
            }
        });

        imageView.setOnMouseDragged(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMouseDragged:" + s);
            if(centerSettingFlag == 2){
                addRectangleForImage((int) event.getX(),(int) event.getY());
                showImagePane.getChildren().removeAll(oneTemperatureDrawMax);
                showImagePane.getChildren().removeAll(oneTemperatureDrawMin);
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
            if(centerSettingFlag == 2){
                BoxTemperatureUtil.getInstance().init(startLineX, startLineY, endLineX, endLineY, new BoxTemperatureUtil.MaxMinTemperature() {
                    @Override
                    public void onResust(float maxTemperature, int[] maxTemperatureXY, float minTemperature, int[] minTemperatureXY) {
                        oneTemperatureDrawMax = addLabelInImage(maxTemperatureXY[0],maxTemperatureXY[1],maxTemperature,Configs.red_color);
                        oneTemperatureDrawMin = addLabelInImage(minTemperatureXY[0],minTemperatureXY[1],minTemperature,Configs.blue2_color);
                        boxTemperatureData = new String[]{String.valueOf(startLineX), String.valueOf(startLineY),
                                String.valueOf(endLineX), String.valueOf(endLineY),
                                getFormatTemperature(maxTemperature), getFormatTemperature(minTemperature)};
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
        oneTemperatureDraw = new ArrayList();
        Label label = new Label();
//        int num = new Random().nextInt(100) - 50;
        label.setText(getFormatTemperature(temperature));
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
        oneTemperatureDraw.add(label);
        oneTemperatureDraw.add(circle);
        oneTemperatureDraw.add(xLine);
        oneTemperatureDraw.add(yLine);
        return oneTemperatureDraw;
    }

    public String getFormatTemperature(float temperature){
        return String.format("%1.2f", temperature) + "℃";
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
