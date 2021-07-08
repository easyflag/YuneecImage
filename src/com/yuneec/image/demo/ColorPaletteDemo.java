package com.yuneec.image.demo;

import com.yuneec.image.Global;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ColorPaletteDemo extends Application {

    Image imageSrc;

    Slider sliderHueShift;
    Slider sliderSaturationFactor;
    Slider sliderBrightnessFactor;
    Slider sliderOpacityFactor;
    ImageView imageViewDest;

    @Override
    public void start(Stage primaryStage) {
        String fileName = "F:\\intellijSpace\\YuneecImage\\src\\image\\Yuneec07.jpg";
        imageSrc = new Image("file:" + fileName);

        sliderHueShift = new Slider(0, 360, 0);
        sliderSaturationFactor = new Slider(0, 1, 1);
        sliderBrightnessFactor = new Slider(0, 1, 1);
        sliderOpacityFactor = new Slider(0, 1, 1);
        sliderHueShift.valueProperty().addListener(listener);
        sliderSaturationFactor.valueProperty().addListener(listener);
        sliderBrightnessFactor.valueProperty().addListener(listener);
        sliderOpacityFactor.valueProperty().addListener(listener);

        imageViewDest = new ImageView();

        WritableImage imageDest = copyImage(imageSrc,
                sliderHueShift.getValue(),
                sliderSaturationFactor.getValue(),
                sliderBrightnessFactor.getValue(),
                sliderOpacityFactor.getValue());
        imageViewDest.setImage(imageDest);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                new Label("hueShift"),
                sliderHueShift,
                new Label("saturationFactor"),
                sliderSaturationFactor,
                new Label("brightnessFactor"),
                sliderBrightnessFactor,
                new Label("opacityFactor"),
                sliderOpacityFactor,
                imageViewDest);

        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);

        Scene scene = new Scene(root, 800, 800);

        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    ChangeListener<Number> listener = new ChangeListener<Number>(){

        @Override
        public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
            WritableImage imageDest = copyImage(imageSrc,
                    sliderHueShift.getValue(),
                    sliderSaturationFactor.getValue(),
                    sliderBrightnessFactor.getValue(),
                    sliderOpacityFactor.getValue());
            imageViewDest.setImage(imageDest);
        }
    };

    private WritableImage copyImage(
            Image src,
            double hueShift,
            double saturationFactor,
            double brightnessFactor,
            double opacityFactor) {

        PixelReader pixelReader = src.getPixelReader();
        WritableImage dest
                = new WritableImage(
                (int) src.getWidth(),
                (int) src.getHeight());
        PixelWriter pixelWriter = dest.getPixelWriter();

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);

                color = color.deriveColor(
                        hueShift,
                        saturationFactor,
                        brightnessFactor,
                        opacityFactor);

                pixelWriter.setColor(x, y, color);
            }
        }

        return dest;
    }

}
