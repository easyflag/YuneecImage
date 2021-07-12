package com.yuneec.image.module.center;

import com.yuneec.image.Configs;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.colorpalette.ColorPalette;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.views.YButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CenterTopSettingPane {

    public FlowPane centerTopSettingPane;

    public CenterSettingSelect centerSettingFlag = CenterSettingSelect.NONE;
    public enum CenterSettingSelect {
        NONE,
        CLEAR,
        POINT,
        BOX,
        ColorPalette,
        Undo
    }
    public Background centerSettingButtonUnclickBackground;
    public Background centerSettingButtonClickBackground;
    public Button SingleClickButton,BoxChooseButton,ColorPaletteButton,ClearButton,UndoButton;
    public ArrayList centerSettingButtonNodeList = new ArrayList();

    private static CenterTopSettingPane instance;
    public static CenterTopSettingPane getInstance() {
        if (instance == null) {
            instance = new CenterTopSettingPane();
        }
        return instance;
    }

    public FlowPane init() {
        initCenterTopSettingPane();
        return centerTopSettingPane;
    }

    private void initCenterTopSettingPane() {
        centerTopSettingPane = new FlowPane();
        centerTopSettingPane.setPrefHeight(Configs.LineHeight);
        centerTopSettingPane.setPrefWidth(Configs.CenterPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        centerTopSettingPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
        initTopButton();
    }

    private void initTopButton() {
        centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.lightGray_color), new CornerRadii(5), new Insets(1)));
        centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), new CornerRadii(5), new Insets(1)));

        SingleClickButton = YButton.getInstance().creatSettingButton("image/center_click.png",null);
        SingleClickButton.setTranslateX(20);

        BoxChooseButton = YButton.getInstance().creatSettingButton("image/box_choose.png",null);
        BoxChooseButton.setTranslateX(30);

        ColorPaletteButton = YButton.getInstance().creatSettingButton("image/color_palette.png",null);
        ColorPaletteButton.setTranslateX(40);
//        ColorPalette.getInstance().init();

        ClearButton = YButton.getInstance().creatSettingButton("image/clear.png",null);
        ClearButton.setTranslateX(50);

        UndoButton = YButton.getInstance().creatSettingButton("image/undo.png",null);
        UndoButton.setTranslateX(60);

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
                                    ClearButton.setBackground(centerSettingButtonUnclickBackground);
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
                                    BoxTemperatureManager.getInstance().backStep();
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
        centerSettingButtonNodeList.add(UndoButton);
        centerTopSettingPane.getChildren().add(SingleClickButton);
        centerTopSettingPane.getChildren().add(BoxChooseButton);
        centerTopSettingPane.getChildren().add(ColorPaletteButton);
        centerTopSettingPane.getChildren().add(ClearButton);
        centerTopSettingPane.getChildren().add(UndoButton);
    }

    public void setTooltip(){
        SingleClickButton.setTooltip(getTooltip(Language.getString(Language.SinglePointTemperature_en,Language.SinglePointTemperature_ch)));
        BoxChooseButton.setTooltip(getTooltip(Language.getString(Language.BoxTemperature_en,Language.BoxTemperature_ch)));
        ColorPaletteButton.setTooltip(getTooltip(Language.getString(Language.ColorPaletteTip_en,Language.ColorPaletteTip_ch)));
        ClearButton.setTooltip(getTooltip(Language.getString(Language.ClearTip_en,Language.ClearTip_ch)));
        UndoButton.setTooltip(getTooltip(Language.getString(Language.UndoTip_en,Language.UndoTip_ch)));
    }

    private Tooltip getTooltip(String info) {
        Tooltip tooltip = new Tooltip(info);
        tooltip.setFont(new Font(12));
        tooltip.setWrapText(true);
        tooltip.setOpacity(0.8);
        return tooltip;
    }

    private void setButtonClickBackground(ArrayList<Button> buttonNodeList,Button clickButton) {
        for(Button button:buttonNodeList){
            if(button == clickButton){
                button.setBackground(centerSettingButtonClickBackground);
            }else{
                button.setBackground(centerSettingButtonUnclickBackground);
            }
        }
    }


}
