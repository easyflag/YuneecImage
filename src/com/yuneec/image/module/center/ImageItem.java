package com.yuneec.image.module.center;

import javafx.scene.control.TreeItem;

public class ImageItem {

    public enum ImageItemType {
        JPG,
        Folder,
        JPG_IN_Folder
    }

    public TreeItem item;
    public String fileName;
    public String filePath;
    public String folderPath;
    public ImageItemType type;

    public ImageItem(TreeItem item, String fileName, String filePath, String folderPath, ImageItemType type){
        this.item = item;
        this.fileName = fileName;
        this.filePath = filePath;
        this.folderPath = folderPath;
        this.type = type;
    }
}
