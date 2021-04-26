package com.yuneec.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTIF {
	
	public static void getTIF(String filename){
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filename));
            if (bufferedImage == null) {
                System.out.println("image read null");
            } else {
                System.out.println("read success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
