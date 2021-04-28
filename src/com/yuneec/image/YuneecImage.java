package com.yuneec.image;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class YuneecImage extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Global.primaryStage = primaryStage;
		primaryStage.setTitle(" Yuneec Image");

		BorderPane root = new BorderPane();

		TopMenuBar.getInstance().init(root);

		Global.hBox = new HBox();
		Global.hBox.setPadding(new Insets(Configs.Spacing, 0, Configs.Spacing, 0));
		Global.hBox.setSpacing(1);

		LeftPane.getInstance().init();
		CenterPane.getInstance().init();
		RightPane.getInstance().init();

		root.setCenter(Global.hBox);
		Scene scene = new Scene(root, Configs.SceneWidth, Configs.SceneHeight);
		primaryStage.setScene(scene);
		// primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("image/launcher.png"));
		primaryStage.show();
	}

}
