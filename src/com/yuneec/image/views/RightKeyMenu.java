package com.yuneec.image.views;

import com.yuneec.image.utils.YLog;
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
            YLog.I("deleteMenuItem...");
        });
        helpMenuItem.setOnAction(event -> {
            YLog.I("helpMenuItem...");
        });
        aboutMenuItem.setOnAction(event -> {
            YLog.I("aboutMenuItem...");
        });
    }

}
