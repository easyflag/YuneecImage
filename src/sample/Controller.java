package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Controller {
    @FXML private Button signBtn;
    @FXML private Text text;

    @FXML protected void signBtnClick(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                text.setVisible(true);
                text.setText("22222");
            }
        });
    }

    public void textCilck(MouseEvent mouseEvent) {
        signBtn.setText("444");
    }
}
