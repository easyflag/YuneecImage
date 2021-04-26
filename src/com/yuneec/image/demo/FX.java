package com.yuneec.image.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class FX extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				primaryStage.setTitle("FX...");
				
			}
		});
		
		Button b1 = new Button();
		b1.setText("1111");
		b1.setLayoutX(150);
		b1.setLayoutY(20);
		b1.setPrefWidth(100);
//		b1.setFont(Font.font("sans.serif",20));
//		b1.setGraphic(new ImageView(new Image("image/center_click.png")));
//		Background background = new Background(new BackgroundFill(Paint.valueOf("#8FBC8F55"), new CornerRadii(5), new Insets(1)));
//		b1.setBackground(background);
//		Border border = new Border(new BorderStroke(Paint.valueOf("#234F91"),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(2)));
//		b1.setBorder(border);
		
		b1.setStyle("-fx-background-color:#7CCDFC;" +
				"-fx-background-radius:20;" +
				"-fx-text-fill:#5CACEE;");
		
		b1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				 System.out.println("initCenterSettingPane MouseClicked ..." + e.getButton().name());
			}
		});
		b1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			@Override
			public void handle(Event e) {
				// TODO Auto-generated method stub
				System.out.println("MOUSE_CLICKED ..." + ((MouseEvent) e).getButton().name());
			}
		});
		
		Group root = new Group();
		
		root.getChildren().add(b1);
		
		Scene sence = new Scene(root);
		Screen screen = Screen.getPrimary();
		Rectangle2D r1 = screen.getBounds();
		Rectangle2D r2 = screen.getVisualBounds();
		pf("" + r1.getHeight() + "," + r1.getWidth());
		pf("" + r2.getHeight() + "," + r2.getWidth());
		primaryStage.setWidth(r2.getWidth()/2);
		primaryStage.setHeight(r2.getHeight()/2);
		primaryStage.setScene(sence);
		primaryStage.show();
	}
	
	
	
	public void pf(String s){
		System.out.println(s);
	}
	
	

}
