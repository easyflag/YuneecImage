package com.yuneec.image.module;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.utils.Utils;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ColorPalette {

    public Pane centerSettingColorPalettePane;
    public boolean centerSettingColorPalettePaneAdded = false;

    private static ColorPalette instance;
    public static ColorPalette getInstance() {
        if (instance == null) {
            instance = new ColorPalette();
        }
        return instance;
    }

    public void init(){
        CenterPane.getInstance().ColorPaletteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Utils.mouseLeftClick(e)) {
//                    YLog.I("initCenterSettingPane ColorPaletteButton MouseClicked ...");
                    CenterPane.getInstance().setButtonClickBackground(CenterPane.getInstance().centerSettingButtonNodeList,CenterPane.getInstance().ColorPaletteButton);
                    CenterPane.getInstance().centerSettingFlag = CenterPane.CenterSettingSelect.ColorPalette;
                    if(centerSettingColorPalettePaneAdded){
                        dmissColorPalettePane();
                        CenterPane.getInstance().ColorPaletteButton.setBackground(CenterPane.getInstance().centerSettingButtonUnclickBackground);
                    }else {
                        showColorPalettePane();
                        CenterPane.getInstance().ColorPaletteButton.setBackground(CenterPane.getInstance().centerSettingButtonClickBackground);
                    }
                }
            }
        });
    }

    public void showColorPalettePane() {
        if(centerSettingColorPalettePaneAdded){
            return;
        }
        colorPaletteButtonList.clear();
        centerSettingColorPalettePane = new FlowPane();
        centerSettingColorPalettePane.setPrefHeight(380);
        centerSettingColorPalettePane.setPrefWidth(100);
        centerSettingColorPalettePane.setTranslateX(10);
        centerSettingColorPalettePane.setTranslateY(10);
        centerSettingColorPalettePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), new CornerRadii(5), null)));
        CenterPane.getInstance().centerImagePane.getChildren().add(centerSettingColorPalettePane);
        centerSettingColorPalettePaneAdded = true;
        addColorPaletteButton();
    }

    public void dmissColorPalettePane() {
        CenterPane.getInstance().centerImagePane.getChildren().remove(centerSettingColorPalettePane);
        centerSettingColorPalettePaneAdded = false;
    }

    public static String[] colorPaletteNameList = {"White Hot","Fulgurite","Iron Red","Hot Iron","Medical","Arctic","Rainbow1","Rainbow2","Tint","Black Hot"};
    public static String[] colorPaletteNameList_ch = {"白热","熔岩","铁红","热铁","医疗","北极","彩虹1","彩虹2","描红","黑热"};

    private String getColorPaletteName(int index) {
        return Language.getString(colorPaletteNameList[index],colorPaletteNameList_ch[index]);
    }

    public void changeColorPaletteNameLanguage(){
        for (int i=0;i<colorPaletteButtonList.size();i++){
            Button button = colorPaletteButtonList.get(i);
            button.setText(getColorPaletteName(i));
        }
    }

    private ArrayList<Button> colorPaletteButtonList = new ArrayList<Button>();
    private void addColorPaletteButton() {
        Button whiteHotButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(0));
        whiteHotButton.setTranslateY(10);
        centerSettingColorPalettePane.getChildren().add(whiteHotButton);
        colorPaletteButtonList.add(whiteHotButton);
        Button FulguriteButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(1));
        FulguriteButton.setTranslateY(20);
        centerSettingColorPalettePane.getChildren().add(FulguriteButton);
        colorPaletteButtonList.add(FulguriteButton);
        Button IronRedButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(2));
        IronRedButton.setTranslateY(30);
        centerSettingColorPalettePane.getChildren().add(IronRedButton);
        colorPaletteButtonList.add(IronRedButton);
        Button HotIronButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(3));
        HotIronButton.setTranslateY(40);
        centerSettingColorPalettePane.getChildren().add(HotIronButton);
        colorPaletteButtonList.add(HotIronButton);
        Button MedicalButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(4));
        MedicalButton.setTranslateY(50);
        centerSettingColorPalettePane.getChildren().add(MedicalButton);
        colorPaletteButtonList.add(MedicalButton);
        Button ArcticButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(5));
        ArcticButton.setTranslateY(60);
        centerSettingColorPalettePane.getChildren().add(ArcticButton);
        colorPaletteButtonList.add(ArcticButton);
        Button Rainbow1Button = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(6));
        Rainbow1Button.setTranslateY(70);
        centerSettingColorPalettePane.getChildren().add(Rainbow1Button);
        colorPaletteButtonList.add(Rainbow1Button);
        Button Rainbow2Button = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(7));
        Rainbow2Button.setTranslateY(80);
        centerSettingColorPalettePane.getChildren().add(Rainbow2Button);
        colorPaletteButtonList.add(Rainbow2Button);
        Button TintButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(8));
        TintButton.setTranslateY(90);
        centerSettingColorPalettePane.getChildren().add(TintButton);
        colorPaletteButtonList.add(TintButton);
        Button BlackHotButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(9));
        BlackHotButton.setTranslateY(100);
        centerSettingColorPalettePane.getChildren().add(BlackHotButton);
        colorPaletteButtonList.add(BlackHotButton);

        whiteHotButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//                boolean flag = YDialog.showConfirmDialog("Change To WhiteHot ...");
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,whiteHotButton);
            }
        });
        FulguriteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,FulguriteButton);
            }
        });
        IronRedButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,IronRedButton);
            }
        });
        HotIronButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,HotIronButton);
            }
        });
        MedicalButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,MedicalButton);
//                new ScaleImage().start(new Stage());
            }
        });
        ArcticButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,ArcticButton);
            }
        });
        Rainbow1Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,Rainbow1Button);
            }
        });
        Rainbow2Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,Rainbow2Button);
            }
        });
        TintButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,TintButton);
            }
        });
        BlackHotButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,BlackHotButton);
            }
        });
    }


}
