package com.yuneec.image.demo;

import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.ScaleImage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CenterScrollPane extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	GridPane gridpane;
	String fileName = "F:\\intellijSpace\\YuneecImage\\src\\image\\Yuneec07.jpg";
	String fileName2 = "F:\\intellijSpace\\YuneecImage\\src\\image\\Yuneec08.jpg";

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		AnchorPane root = new AnchorPane();
		FlowPane centerPane = new FlowPane();
		centerPane.setPrefWidth(Configs.CenterPanelWidth);
		centerPane.setPrefHeight(Configs.SceneHeight);
		centerPane.setTranslateX(Configs.LeftPanelWidth);
		centerPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
		root.getChildren().add(centerPane);

		gridpane = new GridPane();
//		gridpane.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));
		gridpane.setAlignment(Pos.CENTER);
		gridpane.setPrefWidth(Configs.CenterPanelWidth);
		gridpane.setPrefHeight(Configs.SceneHeight - Configs.LineHeight);
		gridpane.setHgap(15);
		gridpane.setVgap(20);
//		gridpane.setPadding(new Insets(25, 25, 25, 25));

		addImageToGridPane(fileName);

		centerPane.getChildren().add(gridpane);

		FlowPane linePane = new FlowPane();
		linePane.setPrefHeight(1);
		linePane.setPrefWidth(Configs.CenterPanelWidth);
		// pane1.setStyle("-fx-background-color: gray;");
		linePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.white_color), null, null)));
		centerPane.getChildren().add(linePane);

		FlowPane indicatorPane = new FlowPane();
		indicatorPane.setPrefHeight(Configs.LineHeight);
		indicatorPane.setPrefWidth(Configs.CenterPanelWidth);
		// pane1.setStyle("-fx-background-color: gray;");
		indicatorPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
		centerPane.getChildren().add(indicatorPane);

		addTrunPagePane(indicatorPane);

		Scene scene = new Scene(root, Configs.SceneWidth, Configs.SceneHeight);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void addImageToGridPane(String fileName) {
		gridpane.getChildren().clear();
		for (int i=0;i<4;i++){
			for (int j=0;j<3;j++){
				AnchorPane item = new AnchorPane();
				Button button = creatSettingButton(fileName,"");
				item.setPrefWidth(320/2);
				item.setPrefHeight(256/2);
//				item.setBackground(new Background(new BackgroundFill(Color.web(Configs.grey_color), null, null)));
				Label labelName = new Label("Yuneec07.jpg");
				labelName.setTextFill(Paint.valueOf(Configs.white_color));
				labelName.setPrefWidth(160);
				labelName.setAlignment(Pos.CENTER);
				item.setBottomAnchor(labelName,10.0);
				item.getChildren().addAll(button,labelName);
				gridpane.add(item,i,j);
			}
		}
	}

	private void addTrunPagePane(FlowPane indicatorPane) {
		Button lastButton = new Button("上一页");
		lastButton.setTranslateX(300);
		lastButton.setTranslateY(10);
		Button nextButton = new Button("下一页");
		nextButton.setTranslateX(400);
		nextButton.setTranslateY(10);
		indicatorPane.getChildren().addAll(lastButton,nextButton);
		lastButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				addImageToGridPane(fileName);
			}
		});
		nextButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				addImageToGridPane(fileName2);
			}
		});
	}

	Background centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), new CornerRadii(5), new Insets(1)));
	Background centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.lightGray_color), new CornerRadii(5), new Insets(1)));
	Border ouClickborder = new Border(new BorderStroke(Paint.valueOf(Configs.blue_color),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1.5)));
	Border clickborder = new Border(new BorderStroke(Paint.valueOf(Configs.red_color),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1.5)));
	ArrayList allImageButton = new ArrayList();
	public Button creatSettingButton(String imagePath,String text) {
		Button button = new Button();
		button.setText(text);
		button.setTextFill(Paint.valueOf(Configs.grey_color));
		if(imagePath != null){
			ImageView imageView = new ImageView(new Image("file:"+imagePath));
			imageView.setFitWidth(320/2 - 20);
			imageView.setFitHeight(256/2 - 10);
			button.setGraphic(imageView);
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				setAllBackground(button);
				if (event.getClickCount() == 2) {
					Global.currentOpenImagePath = "F:\\intellijSpace\\YuneecImage\\src\\image\\Yuneec07.jpg";
					new ScaleImage().start(new Stage());
				}
			});
		}
		button.setBackground(centerSettingButtonUnclickBackground);
		button.setBorder(ouClickborder);
		allImageButton.add(button);
		return button;
	}

	private void setAllBackground(Button button){
		for (int i=0;i<allImageButton.size();i++){
			Button b = (Button) allImageButton.get(i);
			b.setBackground(centerSettingButtonUnclickBackground);
			b.setBorder(ouClickborder);
		}
		button.setBackground(centerSettingButtonClickBackground);
		button.setBorder(clickborder);
	}



}
