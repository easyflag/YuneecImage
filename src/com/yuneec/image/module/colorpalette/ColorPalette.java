package com.yuneec.image.module.colorpalette;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.module.Language;
import com.yuneec.image.utils.Utils;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
        if(centerSettingColorPalettePaneAdded || !Global.hasTemperatureBytes){
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
        CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,colorPaletteButtonList.get(PaletteParam.currentPalette));
    }

    public void dmissColorPalettePane() {
        if (CenterPane.getInstance().centerImagePane != null){
            CenterPane.getInstance().centerImagePane.getChildren().remove(centerSettingColorPalettePane);
            centerSettingColorPalettePaneAdded = false;
        }
    }

    public static String[] colorPaletteNameList = {"White Hot","Fulgurite","Iron Red","Hot Iron","Medical","Arctic","Rainbow1","Rainbow2","Tint","Black Hot"};
    public static String[] colorPaletteNameList_ch = {"白热","熔岩","铁红","热铁","医疗","北极","彩虹1","彩虹2","描红","黑热"};

    public String getColorPaletteName(int index) {
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
        Button FulguriteButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(1));
        FulguriteButton.setTranslateY(20);
        centerSettingColorPalettePane.getChildren().add(FulguriteButton);
        Button IronRedButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(2));
        IronRedButton.setTranslateY(30);
        centerSettingColorPalettePane.getChildren().add(IronRedButton);
        Button HotIronButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(3));
        HotIronButton.setTranslateY(40);
        centerSettingColorPalettePane.getChildren().add(HotIronButton);
        Button MedicalButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(4));
        MedicalButton.setTranslateY(50);
        centerSettingColorPalettePane.getChildren().add(MedicalButton);
        Button ArcticButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(5));
        ArcticButton.setTranslateY(60);
        centerSettingColorPalettePane.getChildren().add(ArcticButton);
        Button Rainbow1Button = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(6));
        Rainbow1Button.setTranslateY(70);
        centerSettingColorPalettePane.getChildren().add(Rainbow1Button);
        Button Rainbow2Button = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(7));
        Rainbow2Button.setTranslateY(80);
        centerSettingColorPalettePane.getChildren().add(Rainbow2Button);
        Button TintButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(8));
        TintButton.setTranslateY(90);
        centerSettingColorPalettePane.getChildren().add(TintButton);
        Button BlackHotButton = CenterPane.getInstance().creatSettingButton(null,getColorPaletteName(9));
        BlackHotButton.setTranslateY(100);
        centerSettingColorPalettePane.getChildren().add(BlackHotButton);
        colorPaletteButtonList.add(whiteHotButton);
        colorPaletteButtonList.add(FulguriteButton);
        colorPaletteButtonList.add(IronRedButton);
        colorPaletteButtonList.add(HotIronButton);
        colorPaletteButtonList.add(MedicalButton);
        colorPaletteButtonList.add(ArcticButton);
        colorPaletteButtonList.add(Rainbow1Button);
        colorPaletteButtonList.add(Rainbow2Button);
        colorPaletteButtonList.add(TintButton);
        colorPaletteButtonList.add(BlackHotButton);

        whiteHotButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
//                boolean flag = YDialog.showConfirmDialog("Change To WhiteHot ...");
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,whiteHotButton);
                PaletteParam.currentPalette = PaletteParam.WhiteHot;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        FulguriteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,FulguriteButton);
                PaletteParam.currentPalette = PaletteParam.Fulgurite;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        IronRedButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,IronRedButton);
                PaletteParam.currentPalette = PaletteParam.IronRed;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        HotIronButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,HotIronButton);
                PaletteParam.currentPalette = PaletteParam.HotIron;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        MedicalButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,MedicalButton);
                PaletteParam.currentPalette = PaletteParam.Medical;
                ColorPaletteManager.I().setImageColorPalette();
//                new ScaleImage().start(new Stage());
            }
        });
        ArcticButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,ArcticButton);
                PaletteParam.currentPalette = PaletteParam.Arctic;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        Rainbow1Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,Rainbow1Button);
                PaletteParam.currentPalette = PaletteParam.Rainbow1;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        Rainbow2Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,Rainbow2Button);
                PaletteParam.currentPalette = PaletteParam.Rainbow2;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        TintButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,TintButton);
                PaletteParam.currentPalette = PaletteParam.Tint;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
        BlackHotButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                CenterPane.getInstance().setButtonClickBackground(colorPaletteButtonList,BlackHotButton);
                PaletteParam.currentPalette = PaletteParam.BlackHot;
                ColorPaletteManager.I().setImageColorPalette();
            }
        });
    }


}
