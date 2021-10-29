package com.yuneec.image.module.custom;

import com.yuneec.image.Configs;
import com.yuneec.image.guide.GuideTemperatureAlgorithm;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.line.LineTemperManager;
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
    double windowWidth = 660;
    double windowHeight = 260;
    FlowPane rootPane;
    Label infoLabel;
    Button changeButton;

    public void start(Stage primaryStage) {

        rootPane = new FlowPane();
        rootPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.light_gray2), null, null)));
        Scene scene = new Scene(rootPane, windowWidth, windowHeight);

        initView();

        primaryStage.setScene(scene);
        Screen screen = Screen.getPrimary();
        Rectangle2D rectangle2D = screen.getBounds();
        screenWidth = rectangle2D.getWidth();
        screenHeight = rectangle2D.getHeight();
        primaryStage.setX(screenWidth/2-windowWidth/2 - 80);
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
        leftLabelList.clear();
        textFieldList.clear();
        VBox vBox = new VBox();
        vBox.setPrefWidth(windowWidth - 50);
        vBox.setPrefHeight(windowHeight - 10);
        vBox.setPadding(new Insets(15, 15, 5, 15));

        HBox hBox1 = getHBox(Language.getString("emissivity","发射率"),""+GuideTemperatureAlgorithm.pParamExt.emiss / 100f, 1); //发射率
        HBox hBox2 = getHBox(Language.getString("relHum","空气湿度"),""+GuideTemperatureAlgorithm.pParamExt.relHum, 2); //（20-100）
        HBox hBox3 = getHBox(Language.getString("distance","距离"),""+GuideTemperatureAlgorithm.pParamExt.distance / 10, 3); //距离 （1-25）
        HBox hBox4 = getHBox(Language.getString("reflectedTemperature","反射温度"),""+GuideTemperatureAlgorithm.pParamExt.reflectedTemper / 10, 4);//反射温度
        HBox hBox5 = getHBox(Language.getString("atmosphericTemperature","空气温度"),""+GuideTemperatureAlgorithm.pParamExt.atmosphericTemper/ 10, 5);//空气温度
        HBox hBox6 = getHBox("modifyK",""+GuideTemperatureAlgorithm.pParamExt.modifyK, 6);
        HBox hBox7 = getHBox("modifyB",""+GuideTemperatureAlgorithm.pParamExt.modifyB, 7);

        changeButton = YButton.getInstance().initButton(null,Language.getString("Change","确认修改"));
        HBox hBox8 = new HBox();
        hBox8.setPrefHeight(50);
        hBox8.setMargin(changeButton,new Insets(0, 10, 0, 0));
        hBox8.setAlignment(Pos.BOTTOM_RIGHT);
        infoLabel = new Label("");
        infoLabel.setTranslateX(-90);
        infoLabel.setFont(Font.font(16));
        infoLabel.setTextFill(Color.web(Configs.red_color));
        hBox8.getChildren().add(infoLabel);
        hBox8.getChildren().add(changeButton);
        changeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Utils.mouseLeftClick(event)) {
                    changeData();
                }
            }
        });

        vBox.getChildren().addAll(hBox1,hBox2,hBox3,hBox4,hBox8);

        rootPane.getChildren().add(vBox);
        rootPane.setAlignment(Pos.BOTTOM_CENTER);

//        hBox1.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));
//        vBox.setBackground(new Background(new BackgroundFill(Color.web(Configs.red_color), null, null)));
    }

    ArrayList textFieldList = new ArrayList();
    ArrayList leftLabelList = new ArrayList();
    private boolean allDataTrue = false;
    private void changeData() {
        try {
            GuideTemperatureAlgorithm.pParamExt.emiss = (int) (Float.parseFloat(((TextField) textFieldList.get(0)).getText()) * 100);
            GuideTemperatureAlgorithm.pParamExt.relHum = Integer.parseInt(((TextField) textFieldList.get(1)).getText());
            GuideTemperatureAlgorithm.pParamExt.distance = Integer.parseInt(((TextField) textFieldList.get(2)).getText()) * 10;
            GuideTemperatureAlgorithm.pParamExt.reflectedTemper = (short) (Short.parseShort(((TextField) textFieldList.get(3)).getText()) * 10);;
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
            LineTemperManager.getInstance().recalculate();
            ToastUtil.toast("Change Succeed !!!",new int[]{90,0});
            updateLeftData();
//            DownLoad.I().dowanload(DownLoad.url,DownLoad.downloadPath);
//            String result = DownLoad.I().sendGet(DownLoad.getUrl);
//            String result = DownLoad.I().sendPost(DownLoad.getUrl,null);
//            System.out.println("" + result);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.toast("Change Failed !!!",new int[]{90,0});
        }
    }

    private void updateLeftData() {
        ((Label)leftLabelList.get(0)).setText(Language.getString("emissivity","发射率")+ " : " +GuideTemperatureAlgorithm.pParamExt.emiss / 100f);
        ((Label)leftLabelList.get(1)).setText(Language.getString("relHum","空气湿度")+ " : " +GuideTemperatureAlgorithm.pParamExt.relHum);
        ((Label)leftLabelList.get(2)).setText(Language.getString("distance","距离")+ " : " +GuideTemperatureAlgorithm.pParamExt.distance / 10);
        ((Label)leftLabelList.get(3)).setText(Language.getString("reflectedTemperature","反射温度")+ " : " +GuideTemperatureAlgorithm.pParamExt.reflectedTemper / 10);
    }

    private HBox getHBox(String name,String data,int flag) {
        HBox hBox1 = new HBox();
        hBox1.setPrefWidth(windowWidth - 100);
        hBox1.setPrefHeight(30);
        hBox1.setPadding(new Insets(5,5,5,5));

        Label text1 = new Label(name + " : " + data);
        text1.setPrefWidth(220);
        text1.setTextFill(Color.web(Configs.black));
        text1.setFont(Font.font(14));
        hBox1.getChildren().add(text1);
        leftLabelList.add(text1);

        Label label = new Label("--->   " + Language.getString("new ","新的 ") + name + " : ");
        label.setFont(Font.font(14));
        label.setPrefWidth(265);
        label.setTextFill(Color.web(Configs.black));
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

                verify(newValue,flag);

            }
        });
        return hBox1;
    }

    private boolean verify(String newValue, int flag) {
//        YLog.I("newValue:" + newValue + "  flag:" + flag);
        changeButtonOnOff();
        if (flag == 1){  //1-100
            return verifyEmiss(newValue);
        }
        if (flag == 2){  //（20-100）
            return verifyrelHum(newValue);
        }
        if (flag == 3){  //（1-25）
            return verifyDistance(newValue);
        }
        if (flag == 4){  //（-40-500）
            return verifyreflectedTemperature(newValue);
        }
        return true;
    }

    private boolean verifyEmiss(String newValue){
        try {
            int emiss = (int) (Float.parseFloat(newValue) * 100);
            if (emiss < 1 || emiss > 100){
                infoLabel.setText(Language.getString("Emissivity Out of range. Range is (0.1-1.0) !","发射率超出范围，范围是(0.1-1.0) !"));
                return false;
            }else {
                infoLabel.setText("");
                return true;
            }
        }catch (Exception e){
            infoLabel.setText(Language.getString("Emissivity Invalid input !","发射率无效输入!"));
            return false;
        }
    }

    private boolean verifyrelHum(String newValue){
        try {
            int relHum =  Integer.parseInt(newValue);
            if (relHum < 20 || relHum > 100){
                infoLabel.setText(Language.getString("relHum Out of range. Range is (20-100) !","空气湿度超出范围，范围是(20-100) !"));
                return false;
            }else {
                infoLabel.setText("");
                return true;
            }
        }catch (Exception e){
            infoLabel.setText(Language.getString("relHum Invalid input !","空气湿度无效输入!"));
            return false;
        }
    }

    private boolean verifyDistance(String newValue){
        try {
            int distance =  Integer.parseInt(newValue);
            if (distance < 1 || distance > 25){
                infoLabel.setText(Language.getString("Distance Out of range. Range is (1-25) !","距离超出范围，范围是(1-25) !"));
                return false;
            }else {
                infoLabel.setText("");
                return true;
            }
        }catch (Exception e){
            infoLabel.setText(Language.getString("Distance Invalid input !","距离无效输入!"));
            return false;
        }
    }

    private boolean verifyreflectedTemperature(String newValue){
        try {
            int reflectedTemperature =  Integer.parseInt(newValue);
            if (reflectedTemperature < -40 || reflectedTemperature > 500){
                infoLabel.setText(Language.getString("ReflectedTemperature Out of range. Range is (-40-500) !","反射温度超出范围，范围是(-40-500) !"));
                return false;
            }else {
                infoLabel.setText("");
                return true;
            }
        }catch (Exception e){
            infoLabel.setText(Language.getString("ReflectedTemperature Invalid input !","反射温度无效输入!"));
            return false;
        }
    }

    private void changeButtonOnOff() {
        boolean emissFlag = verifyEmiss(((TextField) textFieldList.get(0)).getText());
        boolean relHumFlag = verifyrelHum(((TextField) textFieldList.get(1)).getText());
        boolean distanceFlag = verifyDistance(((TextField) textFieldList.get(2)).getText());
        boolean reflectedTemperFlag = verifyreflectedTemperature(((TextField) textFieldList.get(3)).getText());
        if ( emissFlag && relHumFlag && distanceFlag && reflectedTemperFlag){
            changeButton.setDisable(false);
        }else {
            changeButton.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
