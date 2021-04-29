package com.yuneec.image;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class YuneecImage extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Global.primaryStage = primaryStage;
		primaryStage.setTitle(" Yuneec Image Tool");

		BorderPane root = new BorderPane();

		TopMenuBar.getInstance().init(root);

		Global.hBox = new HBox();
//		Global.hBox.setPadding(new Insets(Configs.Spacing, 0, Configs.Spacing, 0));
//		Global.hBox.setSpacing(1);

		LeftPane.getInstance().init();
		Line leftLine = CenterPane.getInstance().drawLine(Configs.LeftPanelWidth,0,Configs.LeftPanelWidth,Configs.SceneWidth,Configs.white_color,0,0);
		Global.hBox.getChildren().add(leftLine);
		CenterPane.getInstance().init();
		Line rightLine = CenterPane.getInstance().drawLine(0,0,0,Configs.SceneWidth,Configs.white_color,0,0);
		Global.hBox.getChildren().add(rightLine);
		RightPane.getInstance().init();

		root.setCenter(Global.hBox);
		Scene scene = new Scene(root, Configs.SceneWidth, Configs.SceneHeight);
		root.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
		primaryStage.setScene(scene);
//		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("image/launcher.png"));
		primaryStage.show();

		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width = primaryStage.getWidth();
//				System.out.println("widthProperty oldValue:" + oldValue + " ,newValue:" + newValue + " ,width:" + width);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Configs.CenterPanelWidth = (int) (width - Configs.LeftPanelWidth - Configs.RightPanelWidth);
						CenterPane.getInstance().centerPane.setPrefWidth(Configs.CenterPanelWidth);
						CenterPane.getInstance().centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
//						CenterPane.getInstance().centerImagePane.setPrefWidth(Configs.CenterPanelWidth);
//						CenterPane.getInstance().getImageOffsetXY();
					}
				});
			}
		});
		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double height = primaryStage.getHeight();
//				System.out.println("heightProperty oldValue:" + oldValue + " ,newValue:" + newValue + " ,height:" + height);
				Configs.SceneHeight = (int) height;
//				CenterPane.getInstance().getImageOffsetXY();
			}
		});

	}

}
