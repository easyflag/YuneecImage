package com.yuneec.image.demo;

import com.yuneec.image.Configs;
import com.yuneec.image.utils.YLog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ProgressDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

        Button b1 = new Button();
        b1.setText("start");
        b1.setLayoutX(150);
        b1.setLayoutY(20);
        b1.setPrefWidth(100);

        Label label = new Label("123");
        label.setTextFill(Color.RED);
        label.setLayoutX(100);
        label.setLayoutY(24);

        ProgressStage progress = new ProgressStage();

        b1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                YLog.I("initCenterSettingPane MouseClicked ..." + e.getButton().name());
                progress.of(primaryStage,
                        new MyTask(0, new CallBack() {
                            @Override
                            public void getNum(int num) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        label.setText("" + num);
//                                        progress.setProgress(num / 100d);
                                    }
                                });
                            }

                            @Override
                            public void finish() {

                            }
                        }),
                        "Loading......"
                );
                progress.show();
            }
        });

        Group root = new Group();

        root.getChildren().addAll(b1);
        root.getChildren().add(label);

        Scene sence = new Scene(root);
        Screen screen = Screen.getPrimary();
        sence.setFill(Paint.valueOf(Configs.backgroundColor));
        Rectangle2D r1 = screen.getBounds();
        Rectangle2D r2 = screen.getVisualBounds();
        pf("" + r1.getHeight() + "," + r1.getWidth());
        pf("" + r2.getHeight() + "," + r2.getWidth());
        primaryStage.setWidth(r2.getWidth()/2);
        primaryStage.setHeight(r2.getHeight()/2);
        primaryStage.setScene(sence);
        primaryStage.show();
    }



    public void pf(String s){
        YLog.I(s);
    }



    public class ProgressStage {

        private Stage stage;
        private Task<?> work;
        private boolean showing = false;
        private int timeOut = 8 * 1000;

        private ProgressStage() {}

        public void of(Stage parent, Task<?> work, String ad) {
            this.work = Objects.requireNonNull(work);
            initUI(parent, ad);
        }
        public void show() {
            if (showing){
                return;
            }
            showing = true;
            new Thread(work).start();
            stage.show();
            addTimeOut();
        }

        private void addTimeOut() {
            TimerTask task= new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            dimiss();
                        }
                    });
                }
            };
            Timer timer=new Timer();
            timer.schedule(task,timeOut);
        }

        public void dimiss() {
            stage.close();
            showing = false;
        }

        public void setProgress(double progress){
            if (indicator != null){
                pf("ProgressDemo setProgress : " + progress);
                indicator.setProgress(progress);
            }
        }

        private ProgressIndicator indicator;
        private void initUI(Stage parent, String info) {
            stage = new Stage();
            stage.initOwner(parent);
            // style
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);

            // message
            Label label = new Label(info);
            label.setTextFill(Color.WHITE);

            // progress
            indicator = new ProgressIndicator();
            indicator.setStyle(" -fx-progress-color: #d9d6c3;");
//            indicator.progressProperty().bind(work.progressProperty());

            // pack
            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.setBackground(Background.EMPTY);
            vBox.getChildren().addAll(indicator, label);

            // scene
            Scene scene = new Scene(vBox);
            scene.setFill(null);
            stage.setScene(scene);
            stage.setWidth(info.length() * 8 + 10);
            stage.setHeight(100);

            // show center of parent
            double x = parent.getX() + (parent.getWidth() - stage.getWidth()) / 2;
            double y = parent.getY() + (parent.getHeight() - stage.getHeight()) / 2;
            stage.setX(x);
            stage.setY(y);

            // close if work finish
            work.setOnSucceeded(e -> dimiss());
        }
    }

    public class MyTask extends Task {

        private int number;
        private CallBack callBack;

        public MyTask(int number,CallBack callBack) {
            this.number = number;
            this.callBack = callBack;
        }

        @Override
        protected Object call() throws Exception {
            while (number < 20) {
                number ++ ;
                callBack.getNum(number);
                pf("ProgressDemo number : " + number);
                Thread.sleep(1000);
            }
            return number;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            callBack.finish();
            pf("ProgressDemo succeeded()");
        }
    }

    public interface CallBack{
        void getNum(int num);
        void finish();
    }

}
