package com.yuneec.image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class YuneecImage extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	Stage primaryStage;
	HBox hBox;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle(" Yuneec Image");

		BorderPane root = new BorderPane();

		initMenu(root);

		// root.setBackground(new Background(new
		// BackgroundFill(Color.web(Configs.color2),null,null)));
		hBox = new HBox();
		hBox.setPadding(new Insets(Configs.Spacing, 0, Configs.Spacing, 0));
		hBox.setSpacing(1);

		initLeftPanel();
		initCenterPanel();
		initRightPanel();

		root.setCenter(hBox);
		Scene scene = new Scene(root, Configs.SceneWidth, Configs.SceneHeight);
		primaryStage.setScene(scene);
		// primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("image/launcher.png"));
		primaryStage.show();
	}

	private void initMenu(BorderPane root) {
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefHeight(Configs.MenuHeight);
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.setTop(menuBar);
		Menu fileMenu = new Menu("File");
		MenuItem openFileMenuItem = new MenuItem("Open");
		openFileMenuItem.setOnAction(actionEvent -> openFile());
		MenuItem saveMenuItem = new MenuItem("Save");
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());
		fileMenu.getItems().addAll(openFileMenuItem, new SeparatorMenuItem(),exitMenuItem);
		Menu settingsMenu = new Menu("Settings");
		MenuItem reportMenuItem = new MenuItem("Create Report");
		reportMenuItem.setOnAction(actionEvent -> createReport());
		settingsMenu.getItems().add(reportMenuItem);
		Menu helpMenu = new Menu("Help");
		RadioMenuItem helpItem = new RadioMenuItem("Help");
		helpMenu.getItems().addAll(helpItem, new SeparatorMenuItem());
		menuBar.getMenus().addAll(fileMenu, settingsMenu);
	}

	private void createReport() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("pdf", "*.pdf"),
				new FileChooser.ExtensionFilter("All", "*.*"),
				new FileChooser.ExtensionFilter("txt", "*.txt"));
		File file = fileChooser.showSaveDialog(primaryStage);
		System.out.println(file);
		if (file == null) {
			return;
		}
		try {
			PdfReport.creat(file.toString().replace("\\", "\\\\"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Label fileNameLable;

	private void initLeftPanel() {
		FlowPane leftPane = new FlowPane();
		leftPane.setPrefWidth(Configs.LeftPanelWidth);
		// leftPane.setStyle("-fx-background-color: lightGray;");
		leftPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
		hBox.getChildren().add(leftPane);

		// Buttons
		Button openFile = new Button();
		openFile.setTranslateX(10);
		openFile.setTranslateY(10);
		openFile.setText("Open");
		// leftPane.getChildren().add(openFile);

		openFile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openFile();
			}
		});

		Pane pane1 = new Pane();
		pane1.setPrefHeight(Configs.LineHeight);
		pane1.setPrefWidth(Configs.LeftPanelWidth);
		// pane1.setStyle("-fx-background-color: gray;");
		pane1.setBackground(new Background(new BackgroundFill(Color.web(Configs.color1), null, null)));

		fileNameLable = new Label();
		fileNameLable.setTranslateX(10);
		fileNameLable.setTranslateY(10);
		fileNameLable.setTextFill(Color.WHITE);
		pane1.getChildren().add(fileNameLable);

		pane1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
				System.out.println("fileNameLable MouseClicked:" + s);
			}
		});

		leftPane.getChildren().add(pane1);
	}

	protected void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose File");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All", "*.*"));
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			String path = file.getAbsolutePath();
			System.out.println(path);
			fileNameLable.setText(file.getName());
			openImagePath = path.replace("\\", "\\\\");
			openImage();
		}
	}

	int imageX, imageY;
	String openImagePath;
	protected void openImage() {
		ImageUtil.readImage(openImagePath);
		showImageInfoToRightPane();
		resetCenterSetting();
		centerSettingColorPalettePaneAdded = false;
		centerImagePane.getChildren().clear();
		Image image = new Image("file:" + openImagePath);
		ImageView imageView = new ImageView(image);
		imageX = (int) (Configs.CenterPanelWidth / 2 - image.getWidth() / 2);
		imageY = (int) ((Configs.SceneHeight - Configs.MenuHeight - Configs.LineHeight - Configs.Spacing) / 2 
				- image.getHeight() / 2);
		imageView.setTranslateX(imageX);
		imageView.setTranslateY(imageY);
		centerImagePane.getChildren().add(imageView);
		
		imageView.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				String s = "x = " + (int) e.getX() + " y = " + (int) e.getY();
				// System.out.println("MouseEvent:" + s);
				showXYlabel.setText(s);
			}
		});
		imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
//				String s = "x=" + (int) e.getX() + " y=" + (int) e.getY();
				// System.out.println("MouseClicked:" + s);
				if(centerSettingFlag == 1 && Utils.mouseLeftClick(e)){
					addLabelInImage((int) e.getX(), (int) e.getY());
				}
			}
		});
		
		imageView.setOnMouseDragged(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMouseDragged:" + s);
			if(centerSettingFlag == 2){
				addRectangleForImage((int) event.getX(),(int) event.getY());
			}
		});
		
		imageView.setOnMousePressed(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMousePressed:" + s);
			startLineX = (int) event.getX();
			startLineY = (int) event.getY();
			
		});
		 
		imageView.setOnMouseReleased(event->{
//			String s = "x=" + (int) event.getX() + " y=" + (int) event.getY();
//			System.out.println("setOnMouseReleased:" + s);
		});
	}

	int startLineX = 0;
	int startLineY = 0;
	Line topLine, bottomLine, leftLine, rightLine;

	private void addRectangleForImage(int x, int y) {
		centerImagePane.getChildren().removeAll(topLine, bottomLine, leftLine, rightLine);
		topLine = drawLine(startLineX, startLineY, x, startLineY,Configs.red_color,imageX,imageY); 
		bottomLine = drawLine(startLineX, y, x, y,Configs.red_color,imageX,imageY); 
		leftLine = drawLine(startLineX, startLineY, startLineX, y,Configs.red_color,imageX,imageY); 
		rightLine = drawLine(x, startLineY, x, y,Configs.red_color,imageX,imageY); 
		centerImagePane.getChildren().addAll(topLine, bottomLine, leftLine, rightLine);
	}

	private Line drawLine(int StartX, int StartY, int EndX, int EndY,
			String color, double translateX, double translateY) {
		Line line = new Line();
		line.setStrokeWidth(1);
		line.setStroke(Paint.valueOf(color));
        line.setStartX(StartX);
        line.setStartY(StartY);
        line.setEndX(EndX);
        line.setEndY(EndY);
        line.setTranslateX(translateX);
        line.setTranslateY(translateY);
		return line;
	}

	protected void addLabelInImage(int x, int y) {
		Label label = new Label();
		label.setText(new Random().nextInt(100) - 50 + "℃");
		label.setTextFill(Color.BLUE);
		label.setTranslateX(x + imageX + 5);
		label.setTranslateY(y + imageY - 8);
		centerImagePane.getChildren().add(label);
		Circle circle = new Circle();
		circle.setFill(Color.RED);
		circle.setCenterX(x + imageX);
		circle.setCenterY(y + imageY);
		circle.setRadius(3.0f);
		centerImagePane.getChildren().add(circle);
	}

	Pane centerImagePane;
	private void initCenterPanel() {
		FlowPane centerPane = new FlowPane();
		centerPane.setPrefWidth(Configs.CenterPanelWidth);
		centerPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));

		Pane centerSettingPane = new FlowPane();
		centerSettingPane.setPrefHeight(Configs.LineHeight);
		centerSettingPane.setPrefWidth(Configs.CenterPanelWidth);
		// pane1.setStyle("-fx-background-color: gray;");
		centerSettingPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.color2), null, null)));
		centerPane.getChildren().add(centerSettingPane);
		
		initCenterSettingPane(centerSettingPane);

		centerImagePane = new Pane();
		centerImagePane.setPrefWidth(Configs.CenterPanelWidth);
		centerImagePane.setPrefHeight(Configs.SceneHeight - Configs.MenuHeight - Configs.LineHeight - 1);
		centerImagePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));
		centerPane.getChildren().add(centerImagePane);

		hBox.getChildren().add(centerPane);
	}

	private int centerSettingFlag ;
	private Background centerSettingButtonUnclickBackground;
	private Background centerSettingButtonClickBackground;
	private Button SingleClickButton,BoxChooseButton,ColorPaletteButton;
	private void initCenterSettingPane(Pane centerSettingPane) {
		centerSettingButtonUnclickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.color2), new CornerRadii(5), new Insets(1)));
		centerSettingButtonClickBackground = new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), new CornerRadii(5), new Insets(1)));
		
		SingleClickButton = creatSettingButton("image/center_click.png",null);
		SingleClickButton.setTranslateX(20);
		
		BoxChooseButton = creatSettingButton("image/box_choose.png",null);
		BoxChooseButton.setTranslateX(30);
		
		ColorPaletteButton = creatSettingButton("image/color_palette.png",null);
		ColorPaletteButton.setTranslateX(40);
		
		SingleClickButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (Utils.mouseLeftClick(e)) {
					System.out.println("initCenterSettingPane SingleClickButton MouseClicked ...");
					SingleClickButton.setBackground(centerSettingButtonClickBackground);
					BoxChooseButton.setBackground(centerSettingButtonUnclickBackground);
					ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
					centerSettingFlag = 1;
					dmissColorPalettePane();
				}
			}
		});

		BoxChooseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (Utils.mouseLeftClick(e)) {
					System.out.println("initCenterSettingPane BoxChooseButton MouseClicked ...");
					BoxChooseButton.setBackground(centerSettingButtonClickBackground);
					SingleClickButton.setBackground(centerSettingButtonUnclickBackground);
					ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
					centerSettingFlag = 2;
					dmissColorPalettePane();
				}
			}
		});

		ColorPaletteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (Utils.mouseLeftClick(e)) {
					System.out.println("initCenterSettingPane ColorPaletteButton MouseClicked ...");
					SingleClickButton.setBackground(centerSettingButtonUnclickBackground);
					BoxChooseButton.setBackground(centerSettingButtonUnclickBackground);
					centerSettingFlag = 3;
					if(centerSettingColorPalettePaneAdded){
						dmissColorPalettePane();
						ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
					}else {
						showColorPalettePane();
						ColorPaletteButton.setBackground(centerSettingButtonClickBackground);
					}
				}
			}
		});
		
		centerSettingPane.getChildren().add(SingleClickButton);
		centerSettingPane.getChildren().add(BoxChooseButton);
		centerSettingPane.getChildren().add(ColorPaletteButton);
	}
	
	Pane centerSettingColorPalettePane;
	boolean centerSettingColorPalettePaneAdded = false;
	protected void showColorPalettePane() {
		if(centerSettingColorPalettePaneAdded){
			return;
		}
		centerSettingColorPalettePane = new FlowPane();
		centerSettingColorPalettePane.setPrefHeight(300);
		centerSettingColorPalettePane.setPrefWidth(100);
		centerSettingColorPalettePane.setTranslateX(10);
		centerSettingColorPalettePane.setTranslateY(10);
		centerSettingColorPalettePane.setBackground(new Background(new BackgroundFill(Color.web(Configs.color2), new CornerRadii(5), null)));
		centerImagePane.getChildren().add(centerSettingColorPalettePane);
		centerSettingColorPalettePaneAdded = true;
		addColorPaletteButton();
	}
	
	protected void dmissColorPalettePane() {
		centerImagePane.getChildren().remove(centerSettingColorPalettePane);
		centerSettingColorPalettePaneAdded = false;
	}
	
	ArrayList<Button> colorPaletteButtonList = new ArrayList<Button>();
	private void addColorPaletteButton() {
		Button whiteHotButton = creatSettingButton(null,"WhiteHot");
		whiteHotButton.setTranslateY(10);
		centerSettingColorPalettePane.getChildren().add(whiteHotButton);
		colorPaletteButtonList.add(whiteHotButton);
		Button FulguriteButton = creatSettingButton(null,"Fulgurite");
		FulguriteButton.setTranslateY(20);
		centerSettingColorPalettePane.getChildren().add(FulguriteButton);
		colorPaletteButtonList.add(FulguriteButton);
		Button IronRedButton = creatSettingButton(null,"IronRed");
		IronRedButton.setTranslateY(30);
		centerSettingColorPalettePane.getChildren().add(IronRedButton);
		colorPaletteButtonList.add(IronRedButton);
		Button HotIronButton = creatSettingButton(null,"HotIron");
		HotIronButton.setTranslateY(40);
		centerSettingColorPalettePane.getChildren().add(HotIronButton);
		colorPaletteButtonList.add(HotIronButton);
		Button MedicalButton = creatSettingButton(null,"Medical");
		MedicalButton.setTranslateY(50);
		centerSettingColorPalettePane.getChildren().add(MedicalButton);
		colorPaletteButtonList.add(MedicalButton);
		whiteHotButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setButtonClickBackground(colorPaletteButtonList,whiteHotButton);
			}
		});
		FulguriteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setButtonClickBackground(colorPaletteButtonList,FulguriteButton);
			}
		});
		IronRedButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setButtonClickBackground(colorPaletteButtonList,IronRedButton);
			}
		});
		HotIronButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setButtonClickBackground(colorPaletteButtonList,HotIronButton);
			}
		});
		MedicalButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setButtonClickBackground(colorPaletteButtonList,MedicalButton);
			}
		});
	}
	
	private void setButtonClickBackground(ArrayList<Button> colorPaletteButtonList,Button clickButton) {
		for(Button button:colorPaletteButtonList){
			if(button == clickButton){
				button.setBackground(centerSettingButtonClickBackground);
			}else{
				button.setBackground(centerSettingButtonUnclickBackground);
			}
		}
	}
	
	private Button creatSettingButton(String imagePath,String text) {
		Button button = new Button();
		button.setText(text);
		button.setTranslateX(10);
		button.setTranslateY(5);
		button.setPrefWidth(80);
		button.setTextFill(Paint.valueOf(Configs.grey_color));
		if(imagePath != null){
			ImageView imageView = new ImageView(new Image(imagePath));
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button.setGraphic(imageView);
		}
		button.setBackground(centerSettingButtonUnclickBackground);
		Border border = new Border(new BorderStroke(Paint.valueOf(Configs.color1),BorderStrokeStyle.SOLID,new CornerRadii(5),new BorderWidths(1.5)));
		button.setBorder(border);
		return button;
	}

	private void resetCenterSetting() {
		SingleClickButton.setBackground(centerSettingButtonUnclickBackground);
		BoxChooseButton.setBackground(centerSettingButtonUnclickBackground);
		ColorPaletteButton.setBackground(centerSettingButtonUnclickBackground);
		centerSettingFlag = 0;
	}

	Label showXYlabel;

	private void initRightPanel() {
		FlowPane rightPane = new FlowPane();
		rightPane.setPrefWidth(Configs.RightPanelWidth);
		rightPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.backgroundColor), null, null)));

		Pane pane1 = new Pane();
		pane1.setPrefHeight(Configs.LineHeight);
		pane1.setPrefWidth(Configs.RightPanelWidth);
		// pane1.setStyle("-fx-background-color: gray;");
		pane1.setBackground(new Background(new BackgroundFill(Color.web(Configs.color2), null, null)));

		showXYlabel = new Label();
		showXYlabel.setTranslateX(10);
		showXYlabel.setTranslateY(10);
		showXYlabel.setTextFill(Color.WHITE);
		pane1.getChildren().add(showXYlabel);

		rightPane.getChildren().add(pane1);

		initRightPaneImageInfo();
		rightPane.getChildren().add(rightImageInfoPane);

		Button reportButton = new Button();
		reportButton.setTranslateX(120);
		reportButton.setTranslateY(50);
		reportButton.setText("Report");
		// rightPane.getChildren().add(reportButton);

		hBox.getChildren().add(rightPane);

	}

	Pane rightImageInfoPane;
	private void initRightPaneImageInfo() {
		rightImageInfoPane = new Pane();
		rightImageInfoPane.setPrefHeight(500);
		rightImageInfoPane.setPrefWidth(Configs.RightPanelWidth);
		rightImageInfoPane.setTranslateY(Configs.Spacing);
		rightImageInfoPane.setBackground(new Background(new BackgroundFill(Color.web(Configs.color2), null, null)));
		
		Label titlelabel = new Label();
		titlelabel.setTranslateX(Configs.RightPaneImageInfo.marginLeft + 5);
		titlelabel.setTranslateY(Configs.RightPaneImageInfo.marginTop - 30);
		titlelabel.setText("Image Info");
		titlelabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		titlelabel.setTextFill(Color.WHITE);
		rightImageInfoPane.getChildren().add(titlelabel);
		
		for(int i=0;i<Configs.RightPaneImageInfo.row;i++){
			Line line = drawLine(Configs.RightPaneImageInfo.startX, Configs.RightPaneImageInfo.startY + 
					Configs.RightPaneImageInfo.offsetY*i, Configs.RightPaneImageInfo.endX, 
					Configs.RightPaneImageInfo.startY + Configs.RightPaneImageInfo.offsetY*i, Configs.grey_color, 0, 0);
			rightImageInfoPane.getChildren().add(line);
		}
		
		for(int i=0;i<3;i++){
			Line line = drawLine(Configs.RightPaneImageInfo.startX + Configs.RightPaneImageInfo.offsetX*i, 
					Configs.RightPaneImageInfo.startY, Configs.RightPaneImageInfo.startX + 
					Configs.RightPaneImageInfo.offsetX*i, Configs.RightPaneImageInfo.endY, Configs.grey_color, 0, 0);
			rightImageInfoPane.getChildren().add(line);
		}
	}

	private void showImageInfoToRightPane() {
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
	}

}
