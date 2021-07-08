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
            }
            return wImage;
        }
        return null;
    }

}
