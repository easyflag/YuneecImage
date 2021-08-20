package com.yuneec.image.module.custom;

import com.yuneec.image.Configs;
import com.yuneec.image.guide.GuideTemperatureAlgorithm;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class CustomParameters extends Application {
    double screenWidth,screenHeight;
    double windowWidth = 620;
    double windowHeight = 350;
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
        primaryStage.setX(screenWidth/2-windowWidth/2 - 50);
        primaryStage.setY(screenHeight/2-Configs.SceneHeight/2 + 50);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setAlwaysOnTop(true);
//        primaryStage.setX();
        primaryStage.setTitle("Custom Parameters");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    private void initView() {
        textFieldList.clear();
        VBox vBox = new VBox();
        vBox.setPrefWidth(windowWidth - 50);
        vBox.setPrefHeight(windowHeight - 10);
        vBox.setPadding(new Insets(15, 15, 5, 15));

        HBox hBox1 = getHBox("emiss",""+GuideTemperatureAlgorithm.pParamExt.emiss);
        HBox hBox2 = getHBox("relHum",""+GuideTemperatureAlgorithm.pParamExt.relHum);
        HBox hBox3 = getHBox("distance",""+GuideTemperatureAlgorithm.pParamExt.distance);
        HBox hBox4 = getHBox("reflectedTemper",""+GuideTemperatureAlgorithm.pParamExt.reflectedTemper);
        HBox hBox5 = getHBox("atmosphericTemper",""+GuideTemperatureAlgorithm.pParamExt.atmosphericTemper);
        HBox hBox6 = getHBox("modifyK",""+GuideTemperatureAlgorithm.pParamExt.modifyK);
        HBox hBox7 = getHBox("modifyB",""+GuideTemperatureAlgorithm.pParamExt.modifyB);

        Button changeButton = YButton.getInstance().initButton(null,"Change");
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

        vBox.getChildren().addAll(hBox1,hBox2,hBox3,hBox4,hBox5,hBox6,hBox7,hBox8);

        rootPane.getChildren().add(vBox);
        rootPane.setAlignment(Pos.BOTTOM_CENTER);

//        hBox1.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));
//        vBox.setBackground(new Background(new BackgroundFill(Color.web(Configs.red_color), null, null)));
    }

    ArrayList textFieldList = new ArrayList();
    private void changeData() {
        try {
            GuideTemperatureAlgorithm.pParamExt.emiss = Integer.parseInt(((TextField) textFieldList.get(0)).getText());
            GuideTemperatureAlgorithm.pParamExt.relHum = Integer.parseInt(((TextField) textFieldList.get(1)).getText());
            GuideTemperatureAlgorithm.pParamExt.distance = Integer.parseInt(((TextField) textFieldList.get(2)).getText());
            GuideTemperatureAlgorithm.pParamExt.reflectedTemper = Short.parseShort(((TextField) textFieldList.get(3)).getText());
            GuideTemperatureAlgorithm.pParamExt.atmosphericTemper = Short.parseShort(((TextField) textFieldList.get(4)).getText());
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
            ToastUtil.toast("Change Succeed !!!",new int[]{90,0});
        }catch (Exception e){
            ToastUtil.toast("Change Failed !!!",new int[]{90,0});
        }
    }

    private HBox getHBox(String name,String data) {
        HBox hBox1 = new HBox();
        hBox1.setPrefWidth(windowWidth - 100);
        hBox1.setPrefHeight(30);
        hBox1.setPadding(new Insets(5,5,5,5));

        Label text1 = new Label(name + " : " + data);
        text1.setPrefWidth(180);
        text1.setTextFill(Color.web(Configs.white_color));
        text1.setFont(Font.font(14));
        hBox1.getChildren().add(text1);

        Label label = new Label("--->  new " + name + " : ");
        label.setFont(Font.font(14));
        label.setPrefWidth(225);
        label.setTextFill(Color.WHITE);
        TextField field = new TextField();
        field.setPrefSize(80, 25);
        field.setEditable(true);
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
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        return hBox1;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
