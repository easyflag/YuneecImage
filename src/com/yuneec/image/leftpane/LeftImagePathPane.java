package com.yuneec.image.leftpane;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.RightPane;
import com.yuneec.image.module.Language;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.utils.YLog;
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

public class LeftImagePathPane{

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
                TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
                String iamgeName = item.getValue();
                String filePath = getImagePath(iamgeName);
//                YLog.I("Double Click : " + iamgeName + " ,filePath:" + filePath);
                if (iamgeName.startsWith(Language.isEnglish()?Language.Left_All_Image_Title_en:Language.Left_All_Image_Title_ch)){
                    return;
                }
                if (Utils.mouseLeftClick(mouseEvent)){
                    if(mouseEvent.getClickCount() == 2) {
                        if (iamgeName.endsWith(".jpg")){
                            Global.currentOpenImagePath = filePath.replace("\\", "\\\\");
                            YLog.I("Double Click ,Global.currentOpenImagePath :" + Global.currentOpenImagePath);
                            CenterPane.getInstance().showImage();
                        }
                    }
                    if(mouseEvent.getClickCount() == 1){
//                        YLog.I("Node click: " + item.getValue());
                        if (iamgeName.endsWith(".jpg")) {
                            Global.currentLeftSelectImagePath = filePath.replace("\\", "\\\\");
                            RightPane.getInstance().showRightImagePreview();
                            ImageUtil.readImage(Global.currentLeftSelectImagePath);
                            RightPane.getInstance().showImageInfoToRightPane();
                        }
                    }
                    if (delButton != null){
                        leftImagePathPane.getChildren().remove(delButton);
                    }
                }else if (Utils.mouseRightClick(mouseEvent)){
                    if(mouseEvent.getClickCount() == 1){
                        ImageItem imageItem = getItemType(filePath);
                        if (imageItem != null || iamgeItemList.size() > 0){
//                            YLog.I("Node click type: " + imageItem.type + "  , " + imageItem.filePath);
                            if (imageItem.type == 1 || imageItem.type == 2){
                                leftImagePathPane.getChildren().remove(delButton);
                                delButton = CenterPane.getInstance().creatSettingButton(null,
                                        Language.isEnglish()?Language.Delete_en:Language.Delete_ch);
                                delButton.setLayoutX(mouseEvent.getX());
                                delButton.setLayoutY(mouseEvent.getY());
                                leftImagePathPane.getChildren().add(delButton);
                                delButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (imageItem.type == 2){
                                            iamgeItemList.removeAll(getFolderItemList(filePath));
                                        }
                                        iamgeItemList.remove(imageItem);
                                        treeImageFile.getChildren().remove(item);
                                        leftImagePathPane.getChildren().remove(delButton);
                                        if (iamgeItemList.isEmpty()){
                                            CenterPane.getInstance().reset();
                                            RightPane.getInstance().reset();
                                        }
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

    class ImageItem{
        TreeItem item;
        String fileName;
        String filePath;
        String folderPath;
        int type; // 1:jpg 2:Folder 3:jpg in folder
        ImageItem(TreeItem item, String fileName, String filePath, String folderPath, int type){
            this.item = item;
            this.fileName = fileName;
            this.filePath = filePath;
            this.folderPath = folderPath;
            this.type = type;
        }
    }

    ArrayList iamgeItemList = new ArrayList();

    private ImageItem getItemType(String filePath){
        ImageItem imageItem = null;
        for (int i=0;i<iamgeItemList.size();i++){
            imageItem = (ImageItem) iamgeItemList.get(i);
            if (imageItem.filePath.equals(filePath)){
                break;
            }
        }
        return imageItem;
    }

    private ArrayList getFolderItemList(String filePath){
        ArrayList list = new ArrayList();
        for (int i=0;i<iamgeItemList.size();i++){
            ImageItem imageItem = (ImageItem) iamgeItemList.get(i);
            if (imageItem.folderPath != null && imageItem.folderPath.equals(filePath)){
                list.add(imageItem);
            }
        }
        return list;
    }

    public void addImageItem(String fileName,String filePath){
        if (fileName.endsWith(".jpg")){
            TreeItem itemImage = new TreeItem(fileName);
            itemImage.setGraphic(new ImageView("image/picture.png"));
            treeImageFile.getChildren().add(itemImage);
            iamgeItemList.add(new ImageItem(itemImage,fileName,filePath,null,1));
            select(iamgeItemList.size());
        }
    }

    public void addImagePath(String filePath){
        String[] fileArr = filePath.split("\\\\");
        String filePathName = fileArr[fileArr.length-1];
        TreeItem itemImage = new TreeItem(filePathName);
        itemImage.setGraphic(new ImageView("image/folder.png"));
        treeImageFile.getChildren().add(itemImage);
        iamgeItemList.add(new ImageItem(itemImage,filePathName,filePath,null,2));
        select(iamgeItemList.size());
        List<String> imageList = getAllFile(filePath,false,false);
        for (String namePath:imageList){
//            YLog.I("--- " + namePath);
            String[] nameArr = namePath.split("\\\\");
            String fileName = nameArr[nameArr.length-1];
            if (fileName.endsWith(".jpg")){
                TreeItem item = new TreeItem(fileName);
                ImageView imageView = new ImageView("image/picture.png");
                imageView.setFitWidth(12);
                imageView.setFitHeight(12);
                item.setGraphic(imageView);
                itemImage.getChildren().add(item);
                iamgeItemList.add(new ImageItem(item,fileName,namePath,filePath,3));
            }
        }
        itemImage.setExpanded(true);
    }

    public void select(int index){
        treeView.getSelectionModel().select(index);
    }

    private String getImagePath(String fileName){
        String filePath = null;
        for (int i=0;i<iamgeItemList.size();i++){
            String name = ((ImageItem) iamgeItemList.get(i)).fileName;
            filePath = ((ImageItem) iamgeItemList.get(i)).filePath;
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
