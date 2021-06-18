package com.yuneec.image;

import com.yuneec.image.module.Language;
import com.yuneec.image.utils.ImageUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class RightPane {

    private static RightPane instance;
    public Label titlelabel;
    public static RightPane getInstance() {
        if (instance == null) {
            instance = new RightPane();
        }
        return instance;
    }

    public void init() {
        initRightPane();
    }

    public Label showXYlabel;

    private Pane rightPane;
    private void initRightPane() {
        rightPane = new FlowPane();
        rightPane.setPrefWidth(Configs.RightPanelWidth);
        rightPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));

        Pane pane1 = new Pane();
        pane1.setPrefHeight(Configs.LineHeight);
        pane1.setPrefWidth(Configs.RightPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        pane1.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
        pane1.setTranslateX(1);

        showXYlabel = new Label();
        showXYlabel.setTranslateX(10);
        showXYlabel.setTranslateY(12);
        showXYlabel.setTextFill(Color.WHITE);
        pane1.getChildren().add(showXYlabel);

        rightPane.getChildren().add(pane1);

        initRightPaneImageInfo();
        rightPane.getChildren().add(rightImageInfoPane);

        initRightPaneImagePreview();
        rightPane.getChildren().add(rightImagePreviewPane);

        Global.hBox.getChildren().add(rightPane);

    }

    public void reset(){
        rightImagePreviewPane.getChildren().remove(imagePreview);
        imagePreview = null;
        clearImageInfo();
        showXYlabel.setText("");
    }

    private StackPane rightImagePreviewPane;
    private void initRightPaneImagePreview() {
        rightImagePreviewPane = new StackPane();
        rightImagePreviewPane.setPrefHeight(210);
        rightImagePreviewPane.setPrefWidth(Configs.RightPanelWidth);
        rightImagePreviewPane.setTranslateY(10);
        rightImagePreviewPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
    }

    private ImageView imagePreview;
    public void showRightImagePreview(){
        Image image = new Image("file:" + Global.currentLeftSelectImagePath);
        if (imagePreview == null){
            imagePreview = new ImageView(image);
            imagePreview.setFitWidth(280);
            imagePreview.setFitHeight(180);
            rightImagePreviewPane.setAlignment(Pos.CENTER);
            rightImagePreviewPane.getChildren().add(imagePreview);
        }else {
            imagePreview.setImage(image);
        }
    }

    private Pane rightImageInfoPane;
    private void initRightPaneImageInfo() {
        rightImageInfoPane = new Pane();
        rightImageInfoPane.setPrefHeight(405);
        rightImageInfoPane.setPrefWidth(Configs.RightPanelWidth);
        rightImageInfoPane.setTranslateY(5);
//        rightImageInfoPane.setTranslateY(Configs.Spacing);
        rightImageInfoPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));

        titlelabel = new Label();
        titlelabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + 5);
        titlelabel.setTranslateY(Configs.RightPaneImageInfo.marginTop - 30);
        titlelabel.setText(Language.getString(Language.Image_Info_en,Language.Image_Info_ch));
        titlelabel.setFont(Font.font(null, FontWeight.BOLD, 14));
        titlelabel.setTextFill(Color.WHITE);
        rightImageInfoPane.getChildren().add(titlelabel);

        for(int i=0;i<Configs.RightPaneImageInfo.row;i++){
            Line line = CenterPane.getInstance().drawLine(Configs.RightPaneImageInfo.startX, Configs.RightPaneImageInfo.startY +
                            Configs.RightPaneImageInfo.offsetY*i, Configs.RightPaneImageInfo.endX,
                    Configs.RightPaneImageInfo.startY + Configs.RightPaneImageInfo.offsetY*i, Configs.grey_color);
            rightImageInfoPane.getChildren().add(line);
        }

        for(int i=0;i<3;i++){
            Line line = CenterPane.getInstance().drawLine(Configs.RightPaneImageInfo.startX + Configs.RightPaneImageInfo.offsetX*i,
                    Configs.RightPaneImageInfo.startY, Configs.RightPaneImageInfo.startX +
                            Configs.RightPaneImageInfo.offsetX*i, Configs.RightPaneImageInfo.endY, Configs.grey_color);
            rightImageInfoPane.getChildren().add(line);
        }
    }

    public void showImageInfoToRightPane() {
        rightImageInfoPane.getChildren().removeAll(imageInfoTagNameLabelList);
        imageInfoTagNameLabelList.clear();
        rightImageInfoPane.getChildren().removeAll(imageInfoDescriptionLabelList);
        imageInfoDescriptionLabelList.clear();
        int imageInfoListLength = ImageUtil.imageInfoSortList.size();
//		YLog.I("imageInfoListLength:" + imageInfoListLength);
        if (imageInfoListLength > 0) {
            for (int i = 0; i < imageInfoListLength; i++) {
                ArrayList<String> arrayList = ImageUtil.imageInfoSortList.get(i);
                String tagName = arrayList.get(0);
                String description = arrayList.get(1);
                addImageInfoLabelToRightPane(tagName,description,i);
            }
        }
    }

    public ArrayList<Label> imageInfoTagNameLabelList = new ArrayList<Label>();
    public ArrayList<Label> imageInfoDescriptionLabelList = new ArrayList<Label>();
    private void addImageInfoLabelToRightPane(String tagName,String description, int i) {
        Label tagNamelabel = new Label();
        tagNamelabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + 5);
        tagNamelabel.setTranslateY(Configs.RightPaneImageInfo.marginTop + Configs.RightPaneImageInfo.lineHeight * i + 8);
        tagNamelabel.setText(tagName);
        tagNamelabel.setTextFill(Color.WHITE);
        rightImageInfoPane.getChildren().add(tagNamelabel);
        Label descriptionlabel = new Label();
        descriptionlabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + Configs.RightPaneImageInfo.offsetX + 5);
        descriptionlabel.setTranslateY(Configs.RightPaneImageInfo.marginTop + Configs.RightPaneImageInfo.lineHeight * i + 8);
        descriptionlabel.setText(description);
        descriptionlabel.setTextFill(Color.WHITE);
        rightImageInfoPane.getChildren().add(descriptionlabel);
        imageInfoTagNameLabelList.add(tagNamelabel);
        imageInfoDescriptionLabelList.add(descriptionlabel);
    }

    private void clearImageInfo(){
        for (int i=0;i<imageInfoTagNameLabelList.size();i++){
            rightImageInfoPane.getChildren().remove(imageInfoTagNameLabelList.get(i));
        }
        for (int i=0;i<imageInfoDescriptionLabelList.size();i++){
            rightImageInfoPane.getChildren().remove(imageInfoDescriptionLabelList.get(i));
        }
    }


}
