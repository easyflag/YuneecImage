package com.yuneec.image.module.custom;

import com.yuneec.image.Configs;
import com.yuneec.image.guide.GuideTemperatureAlgorithm;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.utils.ToastUtil;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.utils.YLog;
import com.yuneec.image.views.YButton;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class CustomParameters extends Application {
    double screenWidth,screenHeight;
    double windowWidth = 700;
    double windowHeight = 260;
    FlowPane rootPane;

    public void start(Stage primaryStage) {

        rootPane = new FlowPane();
        rootPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
        Scene scene = new Scene(rootPane, windowWidth, windowHeight);

        initView();

        primaryStage.setScene(scene);
        Screen screen = Screen.getPrimary();
        Rectangle2D rectangle2D = screen.getBounds();
        screenWidth = rectangle2D.getWidth();
        screenHeight = rectangle2D.getHeight();
        primaryStage.setX(screenWidth/2-windowWidth/2 - 60);
        primaryStage.setY(screenHeight/2-Configs.SceneHeight/2 + 100);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setAlwaysOnTop(true);
//        primaryStage.setX();
        primaryStage.setTitle(Language.getString(Language.CustomParameters_en,Language.CustomParameters_ch));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    private void initView() {
        textFieldList.clear();
        VBox vBox = new VBox();
        vBox.setPrefWidth(windowWidth - 50);
        vBox.setPrefHeight(windowHeight - 10);
        vBox.setPadding(new Insets(15, 15, 5, 15));

        HBox hBox1 = getHBox(Language.getString("emissivity","发射率"),""+GuideTemperatureAlgorithm.pParamExt.emiss / 100f); //发射率
        HBox hBox2 = getHBox("relHum",""+GuideTemperatureAlgorithm.pParamExt.relHum);
        HBox hBox3 = getHBox(Language.getString("distance","距离"),""+GuideTemperatureAlgorithm.pParamExt.distance / 10); //距离
        HBox hBox4 = getHBox(Language.getString("reflectedTemperature","反射温度"),""+GuideTemperatureAlgorithm.pParamExt.reflectedTemper / 10);//反射温度
        HBox hBox5 = getHBox(Language.getString("atmosphericTemperature","空气温度"),""+GuideTemperatureAlgorithm.pParamExt.atmosphericTemper/ 10);//空气温度
        HBox hBox6 = getHBox("modifyK",""+GuideTemperatureAlgorithm.pParamExt.modifyK);
        HBox hBox7 = getHBox("modifyB",""+GuideTemperatureAlgorithm.pParamExt.modifyB);

        Button changeButton = YButton.getInstance().initButton(null,Language.getString("Change","确认修改"));
        HBox hBox8 = new HBox(changeButton);
        hBox8.setPrefHeight(50);
        hBox8.setMargin(changeButton,new Insets(0, 30, 0, 0));
        hBox8.setAlignment(Pos.BOTTOM_RIGHT);
        changeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Utils.mouseLeftClick(event)) {
                    changeData();
                }
            }
        });

        vBox.getChildren().addAll(hBox1,hBox3,hBox4,hBox5,hBox8);

        rootPane.getChildren().add(vBox);
        rootPane.setAlignment(Pos.BOTTOM_CENTER);

//        hBox1.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));
//        vBox.setBackground(new Background(new BackgroundFill(Color.web(Configs.red_color), null, null)));
    }

    ArrayList textFieldList = new ArrayList();
    private void changeData() {
        try {
            GuideTemperatureAlgorithm.pParamExt.emiss = (int) (Float.parseFloat(((TextField) textFieldList.get(0)).getText()) * 100);
            GuideTemperatureAlgorithm.pParamExt.relHum = Integer.parseInt(((TextField) textFieldList.get(1)).getText());
            GuideTemperatureAlgorithm.pParamExt.distance = Integer.parseInt(((TextField) textFieldList.get(2)).getText()) * 10;
            GuideTemperatureAlgorithm.pParamExt.reflectedTemper = (short) (Short.parseShort(((TextField) textFieldList.get(3)).getText()) * 10);
            GuideTemperatureAlgorithm.pParamExt.atmosphericTemper = (short) (Short.parseShort(((TextField) textFieldList.get(4)).getText()) * 10);
            GuideTemperatureAlgorithm.pParamExt.modifyK = Integer.parseInt(((TextField) textFieldList.get(5)).getText());
            GuideTemperatureAlgorithm.pParamExt.modifyB = Short.parseShort(((TextField) textFieldList.get(6)).getText());
            YLog.I("changeData after -->" + "emiss:" + GuideTemperatureAlgorithm.pParamExt.emiss
                    + " ,relHum:" + GuideTemperatureAlgorithm.pParamExt.relHum
                    + " ,distance:" + GuideTemperatureAlgorithm.pParamExt.distance
                    + " ,reflectedTemper:" + GuideTemperatureAlgorithm.pParamExt.reflectedTemper
                    + " ,atmosphericTemper:" + GuideTemperatureAlgorithm.pParamExt.atmosphericTemper
                    + " ,modifyK:" + GuideTemperatureAlgorithm.pParamExt.modifyK
                    + " ,modifyB:" + GuideTemperatureAlgorithm.pParamExt.modifyB
            );
            PointManager.getInstance().recalculate();
            BoxTemperatureManager.getInstance().recalculate();
            CurveManager.getInstance().recalculate();
            ToastUtil.toast("Change Succeed !!!",new int[]{90,0});
//            DownLoad.I().dowanload(DownLoad.url,DownLoad.downloadPath);
//            String result = DownLoad.I().sendGet(DownLoad.getUrl);
//            String result = DownLoad.I().sendPost(DownLoad.getUrl,null);
//            System.out.println("" + result);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.toast("Change Failed !!!",new int[]{90,0});
        }
    }

    private HBox getHBox(String name,String data) {
        HBox hBox1 = new HBox();
        hBox1.setPrefWidth(windowWidth - 100);
        hBox1.setPrefHeight(30);
        hBox1.setPadding(new Insets(5,5,5,5));

        Label text1 = new Label(name + " : " + data);
        text1.setPrefWidth(220);
        text1.setTextFill(Color.web(Configs.white_color));
        text1.setFont(Font.font(14));
        hBox1.getChildren().add(text1);

        Label label = new Label("--->   " + Language.getString("new ","新的 ") + name + " : ");
        label.setFont(Font.font(14));
        label.setPrefWidth(265);
        label.setTextFill(Color.WHITE);
        TextField field = new TextField();
        field.setPrefSize(80, 25);
        field.setEditable(true);
        field.setText(data);
        field.setAlignment(Pos.CENTER_LEFT);
        hBox1.getChildren().addAll(label, field);
        hBox1.setMargin(label,new Insets(0, 0, 0, 30));
        textFieldList.add(field);
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length()>5){
                    field.setText(newValue.substring(0,5));
                }
//                if (!newValue.matches("\\d*")) {
//                    field.setText(newValue.replaceAll("[^\\d]", ""));
//                }
            }
        });
        return hBox1;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
