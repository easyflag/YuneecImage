package com.yuneec.image.module.center;

import com.yuneec.image.Configs;
import com.yuneec.image.Global;
import com.yuneec.image.RightPane;
import com.yuneec.image.ScaleImage;
import com.yuneec.image.module.Language;
import com.yuneec.image.utils.ImageUtil;
import com.yuneec.image.utils.ParseTemperatureBytes;
import com.yuneec.image.utils.TemperatureAlgorithm;
import com.yuneec.image.utils.Utils;
import com.yuneec.image.views.YButton;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CenterImageThumbPane {

    public GridPane gridpane;
    private double itemWidth = 640 / 6;
    private double itemHeight = 512 / 6;
    public Label pageLabel;

    private static CenterImageThumbPane instance;
    public static CenterImageThumbPane getInstance() {
        if (instance == null) {
            instance = new CenterImageThumbPane();
        }
        return instance;
    }

    public void init() {
        TemperatureAlgorithm.SupportScale = true;
        initCenterImageThumbPane();
    }

    private void initCenterImageThumbPane() {
        FlowPane centerPane = new FlowPane();
        centerPane.setPrefWidth(Configs.CenterPanelWidth);
        centerPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
        Global.hBox.getChildren().add(centerPane);

        centerPane.getChildren().add(CenterTopSettingPane.getInstance().init());

        gridpane = new GridPane();
//		gridpane.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setPrefWidth(Configs.CenterPanelWidth);
        gridpane.setPrefHeight(Configs.SceneHeight - Configs.LineHeight - Configs.LineHeight - Configs.MenuHeight - 15);
        gridpane.setHgap(10);
        gridpane.setVgap(15);
//		gridpane.setPadding(new Insets(25, 25, 25, 25));

        centerPane.getChildren().add(gridpane);

        FlowPane linePane = new FlowPane();
        linePane.setPrefHeight(1);
        linePane.setPrefWidth(Configs.CenterPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        linePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.blue_color), null, null)));
        centerPane.getChildren().add(linePane);

        FlowPane indicatorPane = new FlowPane();
        indicatorPane.setPrefHeight(Configs.LineHeight + 15);
        indicatorPane.setPrefWidth(Configs.CenterPanelWidth);
        // pane1.setStyle("-fx-background-color: gray;");
        indicatorPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.lightGray_color), null, null)));
        centerPane.getChildren().add(indicatorPane);

        addTrunPagePane(indicatorPane);

    }

    public void addImageToGridPane() {
        if (gridpane == null){
            return;
        }
        gridpane.getChildren().clear();
        for (int i=0;i<CenterImageManager.columnNum;i++){
            for (int j=0;j<CenterImageManager.rowNum;j++){
                ImageItem imageItem = CenterImageManager.getInstance().getImageItemForRowColumn(j,i,currentPage);
                if (imageItem != null){
                    AnchorPane item = new AnchorPane();
                    Button button = creatImageViewButton(imageItem,"");
                    item.setPrefWidth(itemWidth);
                    item.setPrefHeight(itemHeight);
//				    item.setBackground(new Background(new BackgroundFill(Color.web(Configs.grey_color), null, null)));
                    Label labelName = new Label(imageItem.fileName);
                    labelName.setTextFill(Paint.valueOf(Configs.white_color));
                    labelName.setPrefWidth(itemWidth + 30);
                    labelName.setAlignment(Pos.CENTER);
                    item.setBottomAnchor(labelName,0.0);
                    item.setBottomAnchor(button,20.0);
                    item.getChildren().addAll(button,labelName);
                    gridpane.add(item,i,j);
                }
            }
        }
    }

    public void switchLanguage(){
        if (lastButton != null){
            lastButton.setText(Language.getString(Language.Back_Page_en,Language.Back_Page_ch));
        }
        if (nextButton != null){
            nextButton.setText(Language.getString(Language.Next_Page_en,Language.Next_Page_ch));
        }
        CenterImageManager.getInstance().updatePageLabel(currentPage);
    }

    private int currentPage = 1;
    private Button lastButton,nextButton;
    private void addTrunPagePane(FlowPane indicatorPane) {
        pageLabel = new Label();
        pageLabel.setTranslateX(10);
        pageLabel.setTranslateY(12);
        pageLabel.setTextFill(Color.WHITE);
        CenterImageManager.getInstance().updatePageLabel(currentPage);
        lastButton = YButton.getInstance().initButton(null,
                Language.getString(Language.Back_Page_en,Language.Back_Page_ch));
        lastButton.setTranslateX(70);
        lastButton.setTranslateY(12);
        nextButton = YButton.getInstance().initButton(null,
                Language.getString(Language.Next_Page_en,Language.Next_Page_ch));
        nextButton.setTranslateX(120);
        nextButton.setTranslateY(12);
        indicatorPane.getChildren().addAll(pageLabel,lastButton,nextButton);
        lastButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Utils.mouseLeftClick(event)) {
                    if (currentPage > 1){
                        currentPage--;
                        CenterImageManager.getInstance().updatePageLabel(currentPage);
                    }
                    addImageToGridPane();
                }
            }
        });
        nextButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Utils.mouseLeftClick(event)) {
                    if (currentPage < CenterImageManager.getInstance().getCountPage()){
                        currentPage++;
                        CenterImageManager.getInstance().updatePageLabel(currentPage);
                    }
                    addImageToGridPane();
                }
            }
        });
    }

    Background centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), new CornerRadii(5), new Insets(1)));
    Background centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.lightGray_color), new CornerRadii(5), new Insets(1)));
    Border ouClickborder = new Border(new BorderStroke(Paint.valueOf(Configs.blue_color),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1.5)));
    Border clickborder = new Border(new BorderStroke(Paint.valueOf(Configs.red_color),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1.5)));
    ArrayList allImageButton = new ArrayList();
    public Button creatImageViewButton(ImageItem imageItem,String text) {
        Button button = new Button();
        button.setText(text);
        button.setTextFill(Paint.valueOf(Configs.grey_color));
        if(imageItem != null){
            ImageView imageView = new ImageView(new Image("file:"+imageItem.filePath));
            imageView.setFitWidth(itemWidth - 0);
            imageView.setFitHeight(itemHeight - 0);
            button.setGraphic(imageView);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                setAllBackground(button);
                Global.currentLeftSelectImagePath = imageItem.filePath.replace("\\", "\\\\");
                ImageUtil.readImageExif(Global.currentLeftSelectImagePath);
                RightPane.getInstance().showRightImageInfo();
                if (event.getClickCount() == 2) {
                    Global.currentOpenImagePath = imageItem.filePath;
                    try {
                        ParseTemperatureBytes.getInstance().init(ImageUtil.readJpgToByte(Global.currentOpenImagePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new ScaleImage().start(new Stage());
                }
            });
        }
        button.setBackground(centerSettingButtonUnclickBackground);
        button.setBorder(ouClickborder);
        allImageButton.add(button);
        return button;
    }

    private void setAllBackground(Button button){
        for (int i=0;i<allImageButton.size();i++){
            Button b = (Button) allImageButton.get(i);
            b.setBackground(centerSettingButtonUnclickBackground);
            b.setBorder(ouClickborder);
        }
        button.setBackground(centerSettingButtonClickBackground);
        button.setBorder(clickborder);
    }


    public void reset() {
        if (gridpane != null){
            gridpane.getChildren().clear();
        }
    }
}
