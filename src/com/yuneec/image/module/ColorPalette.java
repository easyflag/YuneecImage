package com.yuneec.image.module;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.RightPane;
import com.yuneec.image.Utils;
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
//                    System.out.println("initCenterSettingPane ColorPaletteButton MouseClicked ...");
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
        centerSettingColorPalettePane = new FlowPane();
        centerSettingColorPalettePane.setPrefHeight(300);
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

    private ArrayList<Button> colorPaletteButtonList = new ArrayList<Button>();
    private void addColorPaletteButton() {
        Button whiteHotButton = CenterPane.getInstance().creatSettingButton(null,"WhiteHot");
        whiteHotButton.setTranslateY(10);
        centerSettingColorPalettePane.getChildren().add(whiteHotButton);
        colorPaletteButtonList.add(whiteHotButton);
        Button FulguriteButton = CenterPane.getInstance().creatSettingButton(null,"Fulgurite");
        FulguriteButton.setTranslateY(20);
        centerSettingColorPalettePane.getChildren().add(FulguriteButton);
        colorPaletteButtonList.add(FulguriteButton);
        Button IronRedButton = CenterPane.getInstance().creatSettingButton(null,"IronRed");
        IronRedButton.setTranslateY(30);
        centerSettingColorPalettePane.getChildren().add(IronRedButton);
        colorPaletteButtonList.add(IronRedButton);
        Button HotIronButton = CenterPane.getInstance().creatSettingButton(null,"HotIron");
        HotIronButton.setTranslateY(40);
        centerSettingColorPalettePane.getChildren().add(HotIronButton);
        colorPaletteButtonList.add(HotIronButton);
        Button MedicalButton = CenterPane.getInstance().creatSettingButton(null,"Medical");
        MedicalButton.setTranslateY(50);
        centerSettingColorPalettePane.getChildren().add(MedicalButton);
        colorPaletteButtonList.add(MedicalButton);
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
    }

}
