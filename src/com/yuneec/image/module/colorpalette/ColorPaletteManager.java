package com.yuneec.image.module.colorpalette;


import com.yuneec.image.Global;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ColorPaletteManager {

    public static ColorPaletteManager instance;

    public static ColorPaletteManager getInstance() {
        if (instance == null) {
            instance = new ColorPaletteManager();
        }
        return instance;
    }

    public Image pixWithImage(int type){
        Image image = new Image("file:" + Global.currentOpenImagePath);
        PixelReader pixelReader = image.getPixelReader();
        if(image.getWidth()>0 && image.getHeight() >0){
            WritableImage wImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();
            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
//                    changeColor(type,pixelReader,pixelWriter,x,y);
                    changeArgb(type,pixelReader,pixelWriter,x,y);
                }
            }
            return wImage;
        }
        return null;
    }

    private void changeColor(int type, PixelReader pixelReader, PixelWriter pixelWriter, int x, int y) {
        Color color = pixelReader.getColor(x, y);
        switch (type) {
            case 1:
                color = color.brighter();
                break;
            case 2:
                color = color.darker();
                break;
            case 3:
                color = color.grayscale();
                break;
            case 4:
                color = color.invert();
                break;
            case 5:
                color = color.saturate();
                break;
            case 6:
                color = color.desaturate();
                break;
            case 7:
                color = color.grayscale();
                color = color.invert();
                break;
            case 8:
                color = Color.rgb(100,100,100);
                break;
            default:
                break;
        }
        pixelWriter.setColor(x, y, color);
    }

    private void changeArgb(int type, PixelReader pixelReader, PixelWriter pixelWriter,int x,int y) {
        int argb = pixelReader.getArgb(x, y);
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >>  8) & 0xFF;
        int b =  argb        & 0xFF;

        r = r & 0xC0;
        g = g & 0xC0;
        b = b & 0xC0;
        argb = (a << 24) | (r << 16) | (g << 8) | b;

        pixelWriter.setArgb(x, y, argb);
    }

}
