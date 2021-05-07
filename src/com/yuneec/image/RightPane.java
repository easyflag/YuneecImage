package com.yuneec.image;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class RightPane {

    private static RightPane instance;
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

    private void initRightPane() {
        Pane rightPane = new Pane();
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

        Global.hBox.getChildren().add(rightPane);

    }

    private Pane rightImageInfoPane;
    private void initRightPaneImageInfo() {
        rightImageInfoPane = new Pane();
        rightImageInfoPane.setPrefHeight(500);
        rightImageInfoPane.setPrefWidth(Configs.RightPanelWidth);
        rightImageInfoPane.setTranslateY(Configs.LineHeight+5);
//        rightImageInfoPane.setTranslateY(Configs.Spacing);
        rightImageInfoPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));

        Label titlelabel = new Label();
        titlelabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + 5);
        titlelabel.setTranslateY(Configs.RightPaneImageInfo.marginTop - 30);
        titlelabel.setText("Image Info");
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
        rightImageInfoPane.getChildren().removeAll(imageInfoLabelList);
        imageInfoLabelList.clear();
        int imageInfoListLength = ImageUtil.imageInfoList.size();
//		System.out.println("imageInfoListLength:" + imageInfoListLength);
        if (imageInfoListLength > 0) {
            for (int i = 0; i < imageInfoListLength; i++) {
                ArrayList<String> arrayList = ImageUtil.imageInfoList.get(i);
                String tagName = arrayList.get(0);
                String description = arrayList.get(1);
                addImageInfoLabelToRightPane(tagName,description,i);
            }
        }
    }

    private ArrayList<Label> imageInfoLabelList = new ArrayList<Label>();
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
        imageInfoLabelList.add(tagNamelabel);
        imageInfoLabelList.add(descriptionlabel);
    }


}
