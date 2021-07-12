package com.yuneec.image;

import com.yuneec.image.module.Language;
import com.yuneec.image.utils.TemperatureAlgorithm;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.utils.YLog;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ScaleImage extends Application {
    double windowWidth = 640;
    double windowHeight = 512;
    double rate = 0;
    ImageView imageView;
    double imageW;
    double imageH;
    double offsetX,offsetY;
    double newImageWidth = windowWidth;
    double newImageHeight = windowHeight;

    public void start(Stage primaryStage) {

        String file = Global.currentOpenImagePath;
        imageView = new ImageView();
        YLog.I(file);
        Image image = new Image("file:" + file);
        imageView.setImage(image);

        imageW = image.getWidth();
        imageH = image.getHeight();
        YLog.I("image w:" + imageW + ",h:"+imageH);
        final double max = Math.max(imageW, imageH);
        final int width = (int) (windowWidth * imageW / max);
        final int heigth = (int) (windowHeight * imageH / max);
        imageView.setFitHeight(imageH);
        imageView.setFitWidth(imageW);

        AnchorPane imagePane = new AnchorPane();

        imagePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.grey_color), null, null)));
        StackPane stackPane = new StackPane(imagePane);
        stackPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        Scene scene = new Scene(stackPane, windowWidth, windowHeight);
        imagePane.getChildren().add(imageView);

        double pointPaneX = 200;
        double pointPaneY = 100;
        AnchorPane pointPane = getPointPane();
        pointPane.setLayoutX(pointPaneX);
        pointPane.setLayoutY(pointPaneY);
        imagePane.getChildren().add(pointPane);

        imagePane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                imageView.setY((newValue.doubleValue() - imageView.getFitHeight()) / 2);
            }
        });

        imagePane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                imageView.setX((newValue.doubleValue() - imageView.getFitWidth()) / 2);
            }
        });

        final double scale = 10;
        stackPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() > 0) {
                rate = 0.05 ;
            } else {
                rate = -0.05;
            }
            newImageWidth = (imageView.getFitWidth() + imageW * rate);
            newImageHeight = (imageView.getFitHeight() + imageH * rate);
            if (newImageWidth <= width || newImageWidth > scale * width) {
                return;
            }

            Point2D eventPoint = new Point2D(event.getSceneX(), event.getSceneY());
            Point2D imagePoint = imagePane.localToScene(new Point2D(imageView.getX(), imageView.getY()));
            Rectangle2D imageRect = new Rectangle2D(imagePoint.getX(), imagePoint.getY(), imageView.getFitWidth(), imageView.getFitHeight());
            Point2D ratePoint;
            Point2D eventPointDistance;
            if (newImageWidth > scale / 4 * width && imageRect.contains(eventPoint)) {
                ratePoint = eventPoint.subtract(imagePoint);
                ratePoint = new Point2D(ratePoint.getX() / imageView.getFitWidth(), ratePoint.getY() / imageView.getFitHeight());
                eventPointDistance = imagePane.sceneToLocal(eventPoint);
            } else {
                ratePoint = new Point2D(0.5, 0.5);
                eventPointDistance = new Point2D(imagePane.getWidth() / 2, imagePane.getHeight() / 2);
            }

            offsetX = eventPointDistance.getX() - newImageWidth * ratePoint.getX();
            offsetY = eventPointDistance.getY() - newImageHeight * ratePoint.getY();
//            imageView.setX(offsetX);
//            imageView.setY(offsetY);
            imageView.setFitWidth(newImageWidth);
            imageView.setFitHeight(newImageHeight);
            String s = "eventPointDistance.getX():" + eventPointDistance.getX() + " eventPointDistance.getY():" + eventPointDistance.getY()
                    + " offsetX:" + offsetX + " offsetY:" + offsetY
                    + " newImageWidth:" + newImageWidth + " newImageHeight:" + newImageHeight
                    + " ,rate:" + rate + " ,w_rate:" + newImageWidth/imageW + " ,h_rate:" + newImageHeight/imageH;
            YLog.I("ScaleImage ScrollEvent:" + s);

            double newlabelx =  ((newImageWidth * pointPaneX)/ imageW);
            double newlabely =  ((newImageHeight * pointPaneY)/ imageH);
            pointPane.setLayoutX(newlabelx);
            pointPane.setLayoutY(newlabely);
//            scaleNode(pointPane);
        });

        draggable(stackPane);
        imageViewDraw();

        scene.setFill(Paint.valueOf(Configs.backgroundColor));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    private void imageViewDraw() {
        imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int x = (int) (e.getX() - offsetX);
                int y = (int) (e.getY() - offsetY);
                String s = "x = " + x + " y = " + y + " offsetX:" + offsetX + " offsetY:" + offsetY
                        + " newImageWidth:" + newImageWidth + " newImageHeight:" + newImageHeight +
                        " ,rate:" + rate + " ,w_rate:" + newImageWidth/imageW + " ,h_rate:" + newImageHeight/imageH;
                YLog.I("ScaleImage MouseEvent:" + s);
                Global.currentOpenImageWidth = newImageWidth;
                Global.currentOpenImageHeight = newImageHeight;
                float pointTemperature = TemperatureAlgorithm.getInstance().getTemperature(x,y);
                String ts = Language.getString(" ,"+Language.Temperature_en+" = " , " ,"+Language.Temperature_ch+" = ");
                String rightXYlabel = "x = " + x + " y = " + y + ts;
                String sTemperature = rightXYlabel + Utils.getFormatTemperature(pointTemperature);
                RightPane.getInstance().showXYlabel.setText(sTemperature);
            }
        });
    }

    private AnchorPane getPointPane(){
        AnchorPane anchorPane = new AnchorPane();
        Label label = new Label();
        label.setText("19.98℃");
        label.setTextFill(Color.web(Configs.white_color));
        label.setLayoutX(7);
        label.setLayoutY(-8);
        int lineLEN = 7;
        Line xLine = CenterPane.getInstance().drawLine(-lineLEN,0,lineLEN,0,Configs.red_color);
        Line yLine = CenterPane.getInstance().drawLine(0,-lineLEN,0,lineLEN,Configs.red_color);
        Circle circle = new Circle();
        circle.setStroke(Color.web(Configs.white_color));
        circle.setFill(Color.TRANSPARENT);
        circle.setCenterX(0);
        circle.setCenterY(0);
        circle.setRadius(3.5f);
        anchorPane.getChildren().add(circle);
        anchorPane.getChildren().add(label);
        anchorPane.getChildren().add(xLine);
        anchorPane.getChildren().add(yLine);
        return anchorPane;
    }

    private void scaleNode(Node node) {
        node.setScaleX(newImageWidth/imageW);
        node.setScaleY(newImageHeight/imageH);
    }

    private static class Position {
        double x;
        double y;
    }

    private void draggable(Node node) {
        final Position pos = new Position();
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> node.setCursor(Cursor.HAND));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, event -> node.setCursor(Cursor.DEFAULT));
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            node.setCursor(Cursor.MOVE);
            pos.x = event.getX();
            pos.y = event.getY();

        });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> node.setCursor(Cursor.DEFAULT));
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double distanceX = event.getX() - pos.x;
            double distanceY = event.getY() - pos.y;
            double x = node.getLayoutX() + distanceX;
            double y = node.getLayoutY() + distanceY;
            node.relocate(x, y);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
