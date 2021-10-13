package com.yuneec.image.module.leftpane;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.RightPane;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.center.CenterImageManager;
import com.yuneec.image.module.center.CenterImageThumbPane;
import com.yuneec.image.module.center.ImageItem;
import com.yuneec.image.module.center.ImageItem.ImageItemType;
import com.yuneec.image.utils.*;
import com.yuneec.image.views.YButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LeftImagePathPane{

//    private String imageSuffix = ".jpg";
    private String[] imageSuffix = {".jpg",".JPG"};
    private static LeftImagePathPane instance;
    public static LeftImagePathPane getInstance() {
        if (instance == null) {
            instance = new LeftImagePathPane();
        }
        return instance;
    }

    public void init() {
        initLeftImagePathPane();
    }

    private Pane leftImagePathPane;
    public TreeItem<String> treeImageFile;
    public TreeView treeView;
    private void initLeftImagePathPane() {
        leftImagePathPane = new Pane();
        leftImagePathPane.setPrefWidth(Configs.LeftPanelWidth);
        leftImagePathPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        Global.hBox.getChildren().add(leftImagePathPane);

        treeImageFile = new TreeItem<String>("Yuneec Image Files:");
        treeImageFile.setExpanded(true);

        treeView = new TreeView(treeImageFile);
        treeView.setPrefHeight(672);
        treeView.setPrefWidth(Configs.LeftPanelWidth);
        treeView.getStylesheets().add(getClass().getResource("folder-tree.css").toExternalForm());
        leftImagePathPane.setPadding(new Insets(5, 5, 5, 50));
        leftImagePathPane.getChildren().addAll(treeView);
        addDragFileToLeftPane();

        ContextMenu menu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e ->{
            TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            YLog.I("deleteItem Click : " + item.getValue());
//            treeImageFile.getChildren().remove(item);

        });
        menu.getItems().add(deleteItem);
//        treeView.setContextMenu(menu);

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (delButton != null){
                    leftImagePathPane.getChildren().remove(delButton);
                }
                TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
                if (item == null){
                    return;
                }
                String iamgeName = item.getValue();
                String filePath = getImagePath(iamgeName);
//                YLog.I("Double Click : " + iamgeName + " ,filePath:" + filePath);
                if (iamgeName.startsWith(Language.isEnglish()?Language.Left_All_Image_Title_en:Language.Left_All_Image_Title_ch)){
                    return;
                }
                if (Utils.mouseLeftClick(mouseEvent)){
                    if(mouseEvent.getClickCount() == 2) {
                        if (Global.currentOpenImagePath != null && (isTrueImagePathEnding(filePath))){
                            boolean result = YDialog.showConfirmDialog(Language.getString(
                                    "Make sure to open the new image and \nyour previous action will be cleared !",
                                    "确认打开新图片，您上次的操作会被清除 !"));
                            if (!result){
                                return;
                            }
                        }
                        if (!(Global.cameraMode.startsWith(Global.cameraE20TMode) || Global.cameraMode.startsWith(Global.cameraE10TMode))){
                            ToastUtil.toast(Language.getString("Not Yuneec product image !","不是Yuneec产品图片 !"),new int[]{50,0});
                            return;
                        }
                        if (isTrueImagePathEnding(iamgeName)){
                            Global.currentOpenImagePath = filePath.replace("\\", "\\\\");
                            YLog.I("Double Click ,Global.currentOpenImagePath :" + Global.currentOpenImagePath);
                            CenterPane.getInstance().showImage();
                        }
                    }
                    if(mouseEvent.getClickCount() == 1){
                        YLog.I("Node click: " + item.getValue());
                        if (isTrueImagePathEnding(iamgeName)) {
                            Global.currentLeftSelectImagePath = filePath.replace("\\", "\\\\");
                            ImageUtil.readImageExifAndXmp(Global.currentLeftSelectImagePath);
                            RightPane.getInstance().showRightImageInfo();
                        }
                    }
                }else if (Utils.mouseRightClick(mouseEvent)){
                    if(mouseEvent.getClickCount() == 1){
                        if (((ImageView)item.getGraphic()).getFitWidth() == 12){
//                            YLog.I("JPG_IN_Folder");
                        }else {
                            if (isTrueImagePathEnding(filePath)) {
                                imageItemSelect = getImageItem(filePath, ImageItemType.JPG);
                            } else {
                                imageItemSelect = getImageItem(filePath, ImageItemType.Folder);
                            }
                            if (imageItemSelect != null || CenterImageManager.getInstance().imageItemList.size() > 0) {
//                            YLog.I("Node click type: " + imageItem.type + "  , " + imageItem.filePath);
                                leftImagePathPane.getChildren().remove(delButton);
                                delButton = YButton.getInstance().creatSettingButton(null,
                                        Language.isEnglish() ? Language.Delete_en : Language.Delete_ch);
                                delButton.setLayoutX(mouseEvent.getX());
                                delButton.setLayoutY(mouseEvent.getY());
                                leftImagePathPane.getChildren().add(delButton);
                                delButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (imageItemSelect.type == ImageItemType.Folder) {
                                            CenterImageManager.getInstance().removeCenterImageItem(filePath);
                                        }
                                        CenterImageManager.getInstance().removeCenterImageItem(imageItemSelect);
                                        treeImageFile.getChildren().remove(item);
                                        leftImagePathPane.getChildren().remove(delButton);
                                        if (CenterImageManager.getInstance().imageItemList.isEmpty()) {
                                            CenterPane.getInstance().reset();
                                            RightPane.getInstance().reset();
                                            CenterImageThumbPane.getInstance().reset();
                                        }
                                        CenterImageThumbPane.getInstance().addImageToGridPane();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

    }
    Button delButton;
    ImageItem imageItemSelect = null;

    private boolean isTrueImagePathEnding(String path){
        boolean isTrue = false;
        for (int i=0;i<imageSuffix.length;i++){
            if (path.endsWith(imageSuffix[i])) {
                isTrue = true;
                break;
            }
        }
        return isTrue;
    }

    private ImageItem getImageItem(String filePath, ImageItemType tpye){
        ImageItem imageItem = null;
        for (int i=0;i<CenterImageManager.getInstance().imageItemList.size();i++){
            imageItem = (ImageItem) CenterImageManager.getInstance().imageItemList.get(i);
            if (imageItem.filePath.equals(filePath)){
                if (tpye == imageItem.type){
                    break;
                }
            }
        }
        return imageItem;
    }

    public void addImageItem(String fileName,String filePath){
        if (isTrueImagePathEnding(fileName)){
            TreeItem itemImage = new TreeItem(fileName);
            itemImage.setGraphic(new ImageView("image/picture.png"));
            treeImageFile.getChildren().add(itemImage);
            CenterImageManager.getInstance().addCenterImageItem(new ImageItem(itemImage,fileName,filePath,null,ImageItemType.JPG));
            select(CenterImageManager.getInstance().imageItemList.size());
            CenterImageThumbPane.getInstance().addImageToGridPane();
        }
    }

    public void addImagePath(String filePath){
        String[] fileArr = filePath.split("\\\\");
        String filePathName = fileArr[fileArr.length-1];
        TreeItem itemImage = new TreeItem(filePathName);
        itemImage.setGraphic(new ImageView("image/folder.png"));
        treeImageFile.getChildren().add(itemImage);
        CenterImageManager.getInstance().addCenterImageItem(new ImageItem(itemImage,filePathName,filePath,null,ImageItemType.Folder));
        select(CenterImageManager.getInstance().imageItemList.size());
        List<String> imageList = getAllFile(filePath,false,false);
        for (String namePath:imageList){
//            YLog.I("--- " + namePath);
            String[] nameArr = namePath.split("\\\\");
            String fileName = nameArr[nameArr.length-1];
            if (isTrueImagePathEnding(fileName)){
                TreeItem item = new TreeItem(fileName);
                ImageView imageView = new ImageView("image/picture.png");
                imageView.setFitWidth(12);
                imageView.setFitHeight(12);
                item.setGraphic(imageView);
                itemImage.getChildren().add(item);
                CenterImageManager.getInstance().addCenterImageItem(new ImageItem(item,fileName,namePath,filePath,ImageItemType.JPG_IN_Folder));
            }
        }
        itemImage.setExpanded(true);
        CenterImageThumbPane.getInstance().addImageToGridPane();
    }

    public void select(int index){
        treeView.getSelectionModel().select(index);
    }

    private String getImagePath(String fileName){
        String filePath = null;
        for (int i=0;i<CenterImageManager.getInstance().imageItemList.size();i++){
            String name = ((ImageItem) CenterImageManager.getInstance().imageItemList.get(i)).fileName;
            filePath = ((ImageItem) CenterImageManager.getInstance().imageItemList.get(i)).filePath;
            if (fileName.equals(name)){
                break;
            }
        }
        return filePath;
    }

    private List<String> getAllFile(String directoryPath, boolean isAddDirectory, boolean listChildren) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(isAddDirectory){
                    list.add(file.getAbsolutePath());
                }
                if (listChildren){
                    list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory,listChildren));
                }
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    private void addDragFileToLeftPane() {
        leftImagePathPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != leftImagePathPane && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        leftImagePathPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
//                    YLog.I("addDragFileToLeftPane:"+db.getFiles());
                    dragImageFile(db.getFiles());
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    private void dragImageFile(List<File> files) {
        for (File file : files) {
            String path = file.getAbsolutePath();
            String[] pathArr = path.split("\\\\");
            String fileName = pathArr[pathArr.length-1];
            if (file.isDirectory()) {
//                YLog.I("isDirectory:"+path);
                addImagePath(path);
            }else {
//                YLog.I("image:"+path);
                addImageItem(fileName,path);
            }
        }
    }
}
