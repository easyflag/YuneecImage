package com.yuneec.image.leftpane;

import com.yuneec.image.CenterPane;
import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.RightPane;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.utils.YLog;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
    private TreeView treeView;
    private void initLeftImagePathPane() {
        leftImagePathPane = new Pane();
        leftImagePathPane.setPrefWidth(Configs.LeftPanelWidth);
        leftImagePathPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        Global.hBox.getChildren().add(leftImagePathPane);

        treeImageFile = new TreeItem<String>("Yuneec Image Files:");
        treeImageFile.setExpanded(true);

        treeView = new TreeView(treeImageFile);
        treeView.setPrefHeight(672);
        treeView.getStylesheets().add(getClass().getResource("folder-tree.css").toExternalForm());
        leftImagePathPane.setPadding(new Insets(5, 5, 5, 50));
        leftImagePathPane.getChildren().addAll(treeView);
        addDragFileToLeftPane();

        ContextMenu menu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e ->{
            TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            YLog.I("deleteItem Click : " + item.getValue());
        });
        menu.getItems().add(deleteItem);
//        treeView.setContextMenu(menu);

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (Utils.mouseLeftClick(mouseEvent)){
                    TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
                    String iamgeName = item.getValue();
                    String filePath = getImagePath(iamgeName);
                    if(mouseEvent.getClickCount() == 2) {
//                        YLog.I("Double Click : " + iamgeName);
                        if (iamgeName.endsWith(".jpg")){
                            Global.currentOpenImagePath = filePath.replace("\\", "\\\\");
                            YLog.I("Double Click ,Global.currentOpenImagePath :" + Global.currentOpenImagePath);
                            CenterPane.getInstance().showImage();
                        }
                    }
                    if(mouseEvent.getClickCount() == 1){
//                        YLog.I("Node click: " + item.getValue());
                        if (iamgeName.endsWith(".jpg")) {
                            Global.currentOpenImagePath = filePath.replace("\\", "\\\\");
                            RightPane.getInstance().showRightImagePreview();
                            ImageUtil.readImage(Global.currentOpenImagePath);
                            RightPane.getInstance().showImageInfoToRightPane();
                        }
                    }
                }
            }
        });

    }

    class ImageItem{
        TreeItem item;
        String fileName;
        String filePath;
        ImageItem(TreeItem item, String fileName, String filePath){
            this.item = item;
            this.fileName = fileName;
            this.filePath = filePath;
        }
    }

    ArrayList iamgeItemList = new ArrayList();

    public void addImageItem(String fileName,String filePath){
        if (fileName.endsWith(".jpg")){
            TreeItem itemImage = new TreeItem(fileName);
            treeImageFile.getChildren().add(itemImage);
            iamgeItemList.add(new ImageItem(itemImage,fileName,filePath));
            select(iamgeItemList.size());
        }
    }

    public void addImagePath(String filePath){
        String[] fileArr = filePath.split("\\\\");
        String filePathName = fileArr[fileArr.length-1];
        TreeItem itemImage = new TreeItem(filePathName);
        treeImageFile.getChildren().add(itemImage);
        iamgeItemList.add(new ImageItem(itemImage,filePathName,filePath));
        select(iamgeItemList.size());
        List<String> imageList = getAllFile(filePath,false,false);
        for (String namePath:imageList){
//            YLog.I("--- " + namePath);
            String[] nameArr = namePath.split("\\\\");
            String fileName = nameArr[nameArr.length-1];
            if (fileName.endsWith(".jpg")){
                TreeItem item = new TreeItem(fileName);
                itemImage.getChildren().add(item);
                iamgeItemList.add(new ImageItem(item,fileName,namePath));
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
