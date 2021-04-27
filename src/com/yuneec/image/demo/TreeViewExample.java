package com.yuneec.image.demo;

import com.yuneec.image.Configs;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class TreeViewExample extends Application {
    public void start(Stage stage) {
        //创建树项
        TreeItem root1 = new TreeItem("Programming Languages",new ImageView(
                new Image(getClass().getResourceAsStream("folder.png"))));
        TreeItem item1 = new TreeItem("Java");
        TreeItem item2 = new TreeItem("Python");
        TreeItem item3 = new TreeItem("C++");
        root1.setExpanded(true);
        //将元素添加到root1-
        root1.getChildren().addAll(item1, item2, item3);

        TreeItem root2 = new TreeItem("NoSQL Databases",new ImageView(
                new Image(getClass().getResourceAsStream("folder.png"))));
        TreeItem item4 = new TreeItem("Neo4j");
        TreeItem item5 = new TreeItem("HBase");
        TreeItem item6 = new TreeItem("Cassandra");
        //将元素添加到root2-
        root2.getChildren().addAll(item4, item5, item6);

        TreeItem root3 = new TreeItem("Bigdata & Analytics",new ImageView(new Image(getClass().getResourceAsStream("folder.png"))));
        TreeItem item7 = new TreeItem("Hadoop");
        TreeItem item8 = new TreeItem("Mahout");
        TreeItem item9 = new TreeItem("Hive");
        //将元素添加到root2-
        root3.getChildren().addAll(item7, item8, item9);

        //列表查看学历
        TreeItem<String> base = new TreeItem<String>("Folder:");
        base.setExpanded(true);
        base.getChildren().addAll(root1, root2, root3);
        //创建一个TreeView项目
        TreeView treeView = new TreeView(base);
        treeView.setPrefHeight(300);
        VBox pane = new VBox(10);
//        treeView.setStyle("-fx-background-color: #234F91;");
//        treeView.setBackground(new Background(new BackgroundFill(Paint.valueOf(Configs.backgroundColor), null, null)));
        pane.setPadding(new Insets(5, 5, 5, 50));
        pane.getChildren().addAll(treeView);
        //设置舞台
        Group node = new Group(pane);
        Scene scene = new Scene(node, 1000, 700, Color.BEIGE);
        treeView.getStylesheets().add(getClass().getResource("folder-tree-view.css").toExternalForm());
        stage.setTitle("List View Example");
        stage.setScene(scene);
        stage.show();


        treeView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                Node node = event.getPickResult().getIntersectedNode();

                if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                    String name = (String) ((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue();
                    System.out.println("Node click: " + name);
                    if (name.equals("Java")){
                        TreeItem root1 = new TreeItem("Programming Languages",new ImageView(
                                new Image(getClass().getResourceAsStream("folder.png"))));
                        TreeItem item1 = new TreeItem("Java");
                        root1.getChildren().addAll(item1, item2, item3);
                        base.getChildren().add(root1);
                    }
                }
            }
        });

    }
    public static void main(String args[]){
        launch(args);
    }
}
