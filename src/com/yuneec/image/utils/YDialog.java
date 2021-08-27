package com.yuneec.image.utils;

import com.yuneec.image.Global;
import com.yuneec.image.module.Language;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class YDialog {
    public enum Response {
        NO, YES, CANCEL
    }

    ;
    private static Response buttonSelected = Response.CANCEL;

    private static ImageView icon = new ImageView();

    static class Dialog extends Stage {
        public Dialog(String title, Stage owner, Scene scene) {
            setTitle(title);
            initStyle(StageStyle.UTILITY);
            initModality(Modality.APPLICATION_MODAL);
            initOwner(owner);
            setResizable(false);
            setScene(scene);
//            icon.setImage(new Image(getClass().getResourceAsStream("/com/sbt/common/images/newUI/gantan.png")));
        }

        //		}
        public void showDialog() {
            sizeToScene();
            centerOnScreen();
            showAndWait();
        }
    }

    static class Message extends Text {
        public Message(String msg) {
            super(msg);
            setWrappingWidth(250);//自动换行的宽度
        }
    }

    public static Response showConfirmDialog(Stage owner, String message, String title) {
        VBox vb = new VBox();
        Scene scene = new Scene(vb,300,150);
        final Dialog dial = new Dialog(title, owner, scene);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setSpacing(50);
        Button yesButton = new Button("YES");
        yesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
                buttonSelected = Response.YES;
            }
        });
        Button noButton = new Button("NO");
        noButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
                buttonSelected = Response.NO;
            }
        });
        BorderPane bp = new BorderPane();
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);
        buttons.getChildren().addAll(yesButton, noButton);
        bp.setCenter(buttons);
        HBox msg = new HBox();
        msg.setSpacing(5);
        msg.getChildren().addAll(icon, new Message(message));
        vb.getChildren().addAll(msg, bp);
        dial.showDialog();
        return buttonSelected;
    }

    public static void showMessageDialog(Stage owner, String message, String title) {
        showMessageDialog(owner, new Message(message), title);
    }

    public static void showMessageDialog(Stage owner, Node message, String title) {
        VBox vb = new VBox();
        Scene scene = new Scene(vb);
        final Dialog dial = new Dialog(title, owner, scene);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setSpacing(10);
        Button okButton = new Button("OK");
        okButton.setAlignment(Pos.CENTER);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dial.close();
            }
        });
        BorderPane bp = new BorderPane();
        bp.setCenter(okButton);
        HBox msg = new HBox();
        msg.setSpacing(5);
        msg.getChildren().addAll(icon, message);
        vb.getChildren().addAll(msg, bp);
        dial.showDialog();
    }

    public static boolean showConfirmDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, new ButtonType(Language.getString("NO","否"), ButtonBar.ButtonData.NO),
                new ButtonType(Language.getString("YES","是"), ButtonBar.ButtonData.YES));
        alert.setTitle(Language.getString("Tips","提示"));
        alert.setHeaderText(message);
        alert.initOwner(Global.primaryStage);
        Optional<ButtonType> _buttonType = alert.showAndWait();
        if (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
            return true;
        } else {
            return false;
        }
    }

    public static void showInformationDialog(String title,String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, null,
                new ButtonType("OK", ButtonBar.ButtonData.YES));
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.initOwner(Global.primaryStage);
        alert.show();
    }

}
