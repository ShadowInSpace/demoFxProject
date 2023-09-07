package com.example.demo;

import javafx.animation.*;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ImageView bg1, bg2, player, enemy;
    @FXML
    private Pane paneGameOver;
    @FXML
    private Label lablePause, lableGameOver;
    @FXML
    private Button restartButton;
    private final int BG_WIDTH = 714;
    private ParallelTransition parallelTransition;

    private TranslateTransition enemyTransirion;
    public static boolean jump = false;
    public static boolean right = false;
    public static boolean left = false;
    public static boolean gameRunning = false;
    public static boolean isPouse = false;
    private int playerSpeed = 3, jumpDownSpeed = 5, enemySpeed = 2;

    private MediaPlayer mediaPlayer;

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (jump && player.getLayoutY() > 20f) {
                player.setLayoutY(player.getLayoutY() - playerSpeed);
            } else if (player.getLayoutY() <= 181f) {
                jump = false;
                player.setLayoutY(player.getLayoutY() + jumpDownSpeed);
            }
            if (right && player.getLayoutX() < 200f)
                player.setLayoutX(player.getLayoutX() + playerSpeed);
            if (left && player.getLayoutX() > 28f)
                player.setLayoutX(player.getLayoutX() - playerSpeed);
            if (gameRunning) {
                playerSpeed = 3;
                jumpDownSpeed = 5;
                parallelTransition.play();
                enemyTransirion.play();
            } else {
                playerSpeed = 0;
                jumpDownSpeed = 0;
                parallelTransition.pause();
                enemyTransirion.pause();
            }

            if (isPouse && !lablePause.isVisible()) {
                lablePause.setVisible(true);
                gameRunning = false;
            } else if (!isPouse && lablePause.isVisible()) {
                lablePause.setVisible(false);
                gameRunning = true;
            }

            if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                paneGameOver.setVisible(true);
                gameRunning = false;
            }
        }
    };

    @FXML
    void restart() {
        enemyTransirion.playFromStart();
        gameRunning=true;
        paneGameOver.setVisible(false);
    }
protected void fonMusic(){

}
    @FXML
    void initialize() {
        TranslateTransition bgOneTransirion = new TranslateTransition(Duration.millis(5000), bg1);
        bgOneTransirion.setFromX(0);
        bgOneTransirion.setToX(BG_WIDTH * -1);
        bgOneTransirion.setInterpolator(Interpolator.LINEAR);

        TranslateTransition bgTwoTransirion = new TranslateTransition(Duration.millis(5000), bg2);
        bgTwoTransirion.setFromX(0);
        bgTwoTransirion.setToX(BG_WIDTH * -1);
        bgTwoTransirion.setInterpolator(Interpolator.LINEAR);

        enemyTransirion = new TranslateTransition(Duration.millis(3500), enemy);
        enemyTransirion.setFromX(0);
        enemyTransirion.setToX(BG_WIDTH * -1 -100);
        enemyTransirion.setInterpolator(Interpolator.LINEAR);
        enemyTransirion.setCycleCount(Animation.INDEFINITE);
        enemyTransirion.play();


        parallelTransition = new ParallelTransition(bgOneTransirion, bgTwoTransirion);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();
        timer.start();
        gameRunning = true;
    }

}
