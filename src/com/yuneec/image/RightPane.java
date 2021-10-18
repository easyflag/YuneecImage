package com.yuneec.image;

import com.yuneec.image.module.Language;
import com.yuneec.image.module.RightImageInfo;
import com.yuneec.image.module.colorpalette.ColorPalette;
import com.yuneec.image.module.colorpalette.PaletteParam;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.utils.YLog;
import com.yuneec.image.views.DeveloperView;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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

        enterDeveloperMode(pane1);

        rightPane.getChildren().add(pane1);

        initRightPaneImageInfo();
        rightPane.getChildren().add(rightImageInfoPane);

        initRightPaneImagePreview();
        rightPane.getChildren().add(rightImagePreviewPane);

        Global.hBox.getChildren().add(rightPane);

    }

    private void enterDeveloperMode(Pane pane) {
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Utils.mouseRightClick(event)){
                    if (event.getClickCount() == 10){
                        YLog.I("---> enterDeveloperMode");
                        new DeveloperView().start(new Stage());
                    }
                }
            }
        });
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

    public void showRightImageInfo(){
        showRightImagePreview();
        showImageInfoToRightPane();
    }

    private ImageView imagePreview;
    private void showRightImagePreview(){
        Image image = new Image("file:" + Global.currentLeftSelectImagePath);
        if (imagePreview == null){
            imagePreview = new ImageView(image);
            imagePreview.setFitWidth(640 / 3);
            imagePreview.setFitHeight(512 / 3);
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

    public static Label[] makeLabels;
    public static Label[] modelLabels;
    public static Label[] imageDescriptionLabels;
    public static Label[] sharpnessLabels;
    public static Label[] timeLabels;
    public static Label[] longitudeLabels;
    public static Label[] latitudeLabels;
    public static Label[] fileSizeLabels;
    public static Label[] colorPaletteLabels;
    private void showImageInfoToRightPane() {
        resetImageInfoLabel();
        makeLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.make[1],RightImageInfo.make[2]), RightImageInfo.make[3],0);
        modelLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.model[1],RightImageInfo.model[2]), RightImageInfo.model[3],1);
        imageDescriptionLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.imageDescription[1],RightImageInfo.imageDescription[2]), RightImageInfo.imageDescription[3],2);
        sharpnessLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.sharpness[2],RightImageInfo.sharpness[3]), RightImageInfo.sharpness[4],3);
        timeLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.time[1],RightImageInfo.time[2]), RightImageInfo.time[3],4);
        longitudeLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.longitude[1],RightImageInfo.longitude[2]), RightImageInfo.longitude[3],5);
        latitudeLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.latitude[1],RightImageInfo.latitude[2]), RightImageInfo.latitude[3],6);
        fileSizeLabels = addImageInfoLabelToRightPane(Language.getString(RightImageInfo.fileSize[1],RightImageInfo.fileSize[2]), RightImageInfo.fileSize[3],7);
        colorPaletteLabels = addImageInfoLabelToRightPane(Language.getString(Language.ColorPaletteTip_en,Language.ColorPaletteTip_ch),
                ColorPalette.getInstance().getColorPaletteName(PaletteParam.currentPalette),8);
    }

    private void resetImageInfoLabel() {
        if (colorPaletteLabels == null){
            return;
        }
        makeLabels[0].setText("");makeLabels[1].setText("");
        modelLabels[0].setText("");modelLabels[1].setText("");
        imageDescriptionLabels[0].setText("");imageDescriptionLabels[1].setText("");
        sharpnessLabels[0].setText("");sharpnessLabels[1].setText("");
        timeLabels[0].setText("");timeLabels[1].setText("");
        longitudeLabels[0].setText("");longitudeLabels[1].setText("");
        latitudeLabels[0].setText("");latitudeLabels[1].setText("");
        fileSizeLabels[0].setText("");fileSizeLabels[1].setText("");
        colorPaletteLabels[0].setText("");colorPaletteLabels[1].setText("");
    }

    public void setImageInfoLanguage(){
        if (colorPaletteLabels == null){
            return;
        }
        makeLabels[0].setText(Language.getString(RightImageInfo.make[1],RightImageInfo.make[2]));
        modelLabels[0].setText(Language.getString(RightImageInfo.model[1],RightImageInfo.model[2]));
        imageDescriptionLabels[0].setText(Language.getString(RightImageInfo.imageDescription[1],RightImageInfo.imageDescription[2]));
        sharpnessLabels[0].setText(Language.getString(RightImageInfo.sharpness[2],RightImageInfo.sharpness[3]));
        timeLabels[0].setText(Language.getString(RightImageInfo.time[1],RightImageInfo.time[2]));
        longitudeLabels[0].setText(Language.getString(RightImageInfo.longitude[1],RightImageInfo.longitude[2]));
        latitudeLabels[0].setText(Language.getString(RightImageInfo.latitude[1],RightImageInfo.latitude[2]));
        fileSizeLabels[0].setText(Language.getString(RightImageInfo.fileSize[1],RightImageInfo.fileSize[2]));
        colorPaletteLabels[0].setText(Language.getString(RightImageInfo.colorPalette[0],RightImageInfo.colorPalette[1]));
        colorPaletteLabels[1].setText(ColorPalette.getInstance().getColorPaletteName(PaletteParam.currentPalette));
    }

    public void setColorPaletteInfo(){
        colorPaletteLabels[1].setText(ColorPalette.getInstance().getColorPaletteName(PaletteParam.currentPalette));
    }

    private Label[] addImageInfoLabelToRightPane(String title,String info, int i) {
        Label titlelabel = new Label();
        titlelabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + 5);
        titlelabel.setTranslateY(Configs.RightPaneImageInfo.marginTop + Configs.RightPaneImageInfo.lineHeight * i + 8);
        titlelabel.setText(title);
        titlelabel.setTextFill(Color.WHITE);
        rightImageInfoPane.getChildren().add(titlelabel);
        Label infolabel = new Label();
        infolabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + Configs.RightPaneImageInfo.offsetX + 5);
        infolabel.setTranslateY(Configs.RightPaneImageInfo.marginTop + Configs.RightPaneImageInfo.lineHeight * i + 8);
        infolabel.setText(info);
        infolabel.setTextFill(Color.WHITE);
        rightImageInfoPane.getChildren().add(infolabel);
        return new Label[]{titlelabel,infolabel};
    }

    private void clearImageInfo(){
        resetImageInfoLabel();
    }


}
