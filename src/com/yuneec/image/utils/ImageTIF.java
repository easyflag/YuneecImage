package com.yuneec.image.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTIF {
	
	public static void getTIF(String filename){
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filename));
            if (bufferedImage == null) {
                YLog.I("image read null");
            } else {
                YLog.I("read success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
