package com.yuneec.image.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class RightKeyMenu extends ContextMenu {

    private static RightKeyMenu instance;

    public static RightKeyMenu getInstance() {
        if (instance == null) {
            instance = new RightKeyMenu();
        }
        return instance;
    }

    private RightKeyMenu(){
        MenuItem deleteMenuItem = new MenuItem("delete");
        MenuItem helpMenuItem = new MenuItem("help");
        MenuItem aboutMenuItem = new MenuItem("about");
        getItems().add(deleteMenuItem);
        getItems().add(helpMenuItem);
        getItems().add(aboutMenuItem);

        deleteMenuItem.setOnAction(event -> {
            System.out.println("deleteMenuItem...");
        });
        helpMenuItem.setOnAction(event -> {
            System.out.println("helpMenuItem...");
        });
        aboutMenuItem.setOnAction(event -> {
            System.out.println("aboutMenuItem...");
        });
    }

}
