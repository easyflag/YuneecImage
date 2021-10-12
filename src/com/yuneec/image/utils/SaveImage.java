package com.yuneec.image.utils;

import com.itextpdf.text.Image;
import com.yuneec.image.CenterPane;
import com.yuneec.image.module.Language;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;

public class SaveImage {

    private static SaveImage instance;
    public static SaveImage I() {
        if (instance == null) {
            instance = new SaveImage();
        }
        return instance;
    }

    public void save(String fileName){
        try {
            WritableImage showImagePane = CenterPane.getInstance().showImagePane.snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(showImagePane, null), "png", new File(fileName));
            Image image = Image.getInstance(fileName);
            image.setAlignment(Image.ALIGN_CENTER);
            image.scalePercent(100);
            ToastUtil.toast(Language.getString("Save Image successfully !","图片保存成功!"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
