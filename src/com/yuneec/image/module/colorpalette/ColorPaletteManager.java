package com.yuneec.image.module.colorpalette;


import com.yuneec.image.CenterPane;
import com.yuneec.image.Global;
import com.yuneec.image.RightPane;
import com.yuneec.image.dll.Java2cpp;
import com.yuneec.image.utils.ByteUtils;
import com.yuneec.image.utils.ParseTemperatureBytes;
import com.yuneec.image.utils.YLog;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ColorPaletteManager {

    public static ColorPaletteManager instance;

    public static ColorPaletteManager I() {
        if (instance == null) {
            instance = new ColorPaletteManager();
        }
        return instance;
    }

    ArrayList<Color> rgbList = new ArrayList<>();
    public void saveColorPaletteRGB(){
        rgbList.clear();
        Image image = new Image("file:" + Global.currentOpenImagePath);
        PixelReader pixelReader = image.getPixelReader();
        if(image.getWidth()>0 && image.getHeight() >0) {
            WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixelReader.getColor(x, y);
                    rgbList.add(color);
                }
            }
        }
    }

    public Image restoreImageRGB(){
        Image image = new Image("file:" + Global.currentOpenImagePath);
        PixelReader pixelReader = image.getPixelReader();
        if(image.getWidth()>0 && image.getHeight() >0) {
            WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int index = y * 640 + x;
                    pixelWriter.setColor(x, y, rgbList.get(index));
                }
            }
            return wImage;
        }
        return null;
    }

    public void setImageColorPalette(){
        /*
        if (PaletteParam.currentPalette == Global.imagePaletteType){
            CenterPane.getInstance().imageView.setImage(restoreImageRGB());
        }else {
            CenterPane.getInstance().imageView.setImage(pixWithImage(PaletteParam.currentPalette));
        }
        */
        CenterPane.getInstance().imageView.setImage(pixWithImage(PaletteParam.currentPalette));
        RightPane.getInstance().setColorPaletteInfo();
    }

    private byte[] rgb24Data;
    public Image pixWithImage(int type){
        PaletteParam.currentPalette = type;
        rgb24Data = Java2cpp.I().y16torgb24(ParseTemperatureBytes.getInstance().TemperatureBytes,type);
        Image image = new Image("file:" + Global.currentOpenImagePath);
        PixelReader pixelReader = image.getPixelReader();
        if(image.getWidth()>0 && image.getHeight() >0){
            WritableImage wImage = new WritableImage((int)image.getWidth(), (int)image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();
            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
//                    changeColor(type,pixelReader,pixelWriter,x,y);
//                    changeArgb(pixelReader,pixelWriter,x,y);
                    changeColorRGB(pixelReader,pixelWriter,x,y);
                }
            }
            return wImage;
        }
        return null;
    }

    private void waitRgb24DataToSetColor(){
        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,1000);
    }

    private void changeColorRGB(PixelReader pixelReader, PixelWriter pixelWriter, int x, int y) {
        int index = (y*640 + x) * 3;
        int r = rgb24Data[index] & 0XFF;
        int g = rgb24Data[index+1] & 0XFF;
        int b = rgb24Data[index+2] & 0XFF;
        Color color = pixelReader.getColor(x, y);
        color = color.rgb(r,g,b);
        pixelWriter.setColor(x, y, color);

//        Color redColor = Color.rgb(255,0,0);
//        Color greenColor = Color.rgb(0,255,0);
//        Color blueColor = Color.rgb(0,0,255);
//        if (x > 0 && x< 200){
//            pixelWriter.setColor(x, y, redColor);
//        }else if (x > 200 && x< 400){
//            pixelWriter.setColor(x, y, greenColor);
//        }else if (x > 400){
//            pixelWriter.setColor(x, y, blueColor);
//        }
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

    private void changeArgb(PixelReader pixelReader, PixelWriter pixelWriter,int x,int y) {
        int index;
        if(y==0){
            index = x * 3;
        }else if(x==0){
            index = y * 3;
        }else {
            index = y * x * 3;
        }
        byte[] rgbByte = new byte[4];
        rgbByte[3] = rgb24Data[index];
        rgbByte[2] = rgb24Data[index+1];
        rgbByte[1] = rgb24Data[index+2];
        int argb = ByteUtils.getInt(rgbByte);

//        int argb = pixelReader.getArgb(x, y);
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
