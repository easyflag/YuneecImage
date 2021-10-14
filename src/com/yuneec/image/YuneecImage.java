package com.yuneec.image;

import com.yuneec.image.guide.GuiDe;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.center.CenterImageThumbPane;
import com.yuneec.image.module.leftpane.LeftImagePathPane;
import com.yuneec.image.utils.SaveSettings;
import com.yuneec.image.utils.WindowChange;
import com.yuneec.image.utils.YDialog;
import com.yuneec.image.utils.YLog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class YuneecImage extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Global.primaryStage = primaryStage;
		primaryStage.setTitle(" Yuneec Image Tool ");
		BorderPane root = new BorderPane();
		Global.hBox = new HBox();
//		Global.hBox.setPadding(new Insets(Configs.Spacing, 0, Configs.Spacing, 0));
//		Global.hBox.setSpacing(1);
		root.setCenter(Global.hBox);
		Scene scene = new Scene(root);
		root.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
		primaryStage.setScene(scene);
//		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("image/launcher.png"));
		primaryStage.setWidth(Configs.DefaultSceneWidth);
		primaryStage.setHeight(Configs.DefaultSceneHeight);
		primaryStage.setMinWidth(Configs.DefaultSceneWidth);
		primaryStage.setMinHeight(Configs.DefaultSceneHeight);
		primaryStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
		primaryStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
		primaryStage.show();

//		LeftPane.getInstance().init();
		LeftImagePathPane.getInstance().init();
		Line leftLine = CenterPane.getInstance().drawLine(Configs.LeftPanelWidth,0,Configs.LeftPanelWidth,Configs.SceneWidth,Configs.white_color);
		Global.hBox.getChildren().add(leftLine);
		CenterPane.getInstance().init();
//		CenterImageThumbPane.getInstance().init();
		Line rightLine = CenterPane.getInstance().drawLine(0,0,0,Configs.SceneWidth,Configs.white_color);
		Global.hBox.getChildren().add(rightLine);
		RightPane.getInstance().init();
		TopMenuBar.getInstance().init(root);
		GuiDe.init();

		primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				YLog.I("maximizedProperty  oldValue:" + oldValue + " ,newValue:" + newValue);
//				WindowChange.I().setWindowMax(newValue);
			}
		});

		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width = primaryStage.getWidth();
//				YLog.I("widthProperty oldValue:" + oldValue + " ,newValue:" + newValue + " ,width:" + width);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Configs.CenterPanelWidth = (int) (width - Configs.LeftPanelWidth - Configs.RightPanelWidth);
						CenterPane.getInstance().centerPane.setPrefWidth(Configs.CenterPanelWidth);
						CenterPane.getInstance().centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
						CenterPane.getInstance().centerImagePane.setPrefWidth(Configs.CenterPanelWidth);
						CenterPane.getInstance().getImagePaneOffsetXY();
					}
				});
			}
		});
		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double height = primaryStage.getHeight();
//				YLog.I("heightProperty oldValue:" + oldValue + " ,newValue:" + newValue + " ,height:" + height);
				Configs.SceneHeight = (int) height;
				Configs.CenterPanelHeight = (int) (height - Configs.SystemBarHeight - Configs.MenuHeight - Configs.LineHeight);
				CenterPane.getInstance().getImagePaneOffsetXY();
				LeftImagePathPane.getInstance().treeView.setPrefHeight(height-66);
			}
		});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				event.consume();
				TopMenuBar.getInstance().exitSoft();
			}
		});

	}

}
