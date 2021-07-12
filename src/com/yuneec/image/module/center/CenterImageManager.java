package com.yuneec.image.module.center;

import com.yuneec.image.module.Language;

import java.util.ArrayList;

public class CenterImageManager {

    public ArrayList imageItemList = new ArrayList();
    public ArrayList imageItemListNoFolder = new ArrayList();
    public static int rowNum = 4;
    public static int columnNum = 5;
    private double onePageImageNum = rowNum * columnNum;

    public static CenterImageManager instance;

    public static CenterImageManager getInstance() {
        if (instance == null) {
            instance = new CenterImageManager();
        }
        return instance;
    }

    public void addCenterImageItem(ImageItem imageItem) {
        imageItemList.add(imageItem);
        updatePageLabel(1);
        if (imageItem.type != ImageItem.ImageItemType.Folder){
            imageItemListNoFolder.add(imageItem);
        }
    }

    public void removeCenterImageItem(ImageItem imageItem) {
        imageItemList.remove(imageItem);
        imageItemListNoFolder.remove(imageItem);
        updatePageLabel(1);
    }

    public void removeCenterImageItem(String filePath) {
        imageItemList.removeAll(getFolderItemList(imageItemList,filePath));
        imageItemListNoFolder.removeAll(getFolderItemList(imageItemListNoFolder,filePath));
        updatePageLabel(1);
    }

    private ArrayList getFolderItemList(ArrayList imageList,String filePath){
        ArrayList list = new ArrayList();
        for (int i=0;i<imageList.size();i++){
            ImageItem imageItem = (ImageItem) imageList.get(i);
            if (imageItem.folderPath != null && imageItem.folderPath.equals(filePath)){
                list.add(imageItem);
            }
        }
        return list;
    }

    public int getCountImage() {
        int count = 0;
        for (int i = 0; i < imageItemList.size(); i++) {
            ImageItem imageItem = (ImageItem) imageItemList.get(i);
            if (imageItem.type != ImageItem.ImageItemType.Folder) {
                count++;
            }
        }
        return count;
    }

    public int getCountPage() {
//        return 2;
        return (int) Math.ceil(imageItemList.size() / onePageImageNum);
    }

    public void updatePageLabel(int currentPage) {
        if (CenterImageThumbPane.getInstance().pageLabel != null){
            CenterImageThumbPane.getInstance().pageLabel.setText(
                    Language.getString("Total ","总共 ") + CenterImageManager.getInstance().getCountImage() +
                            Language.getString(" pictures, Total "," 张图片, 合计 ")
                            + CenterImageManager.getInstance().getCountPage() +
                            Language.getString(" pages, Current is "," 页, 当前第 ") + currentPage + " page.");
        }
    }

    public ImageItem getImageItemForRowColumn(int row,int column,int page){
        ImageItem imageItem = null;
        int index = 0;
        if (row == 0){
            index = column;
        }else {
            if (column == 0){
                index = row * columnNum;
            }else {
                index = row * columnNum + column;
            }
        }
        if (page > 1){
            index = (int) (index + (page-1)*onePageImageNum);
        }
        if (index < imageItemListNoFolder.size()){
            imageItem = (ImageItem) imageItemListNoFolder.get(index);
        }
        return imageItem;
    }

}
