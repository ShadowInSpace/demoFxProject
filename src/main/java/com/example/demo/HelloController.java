package com.example.demo;

import javafx.animation.*;
import javafx.fxml.FXML;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ImageView bg1, bg2, player, enemy, playerJump;
    @FXML
    private ImageView heart1, heart2, heart3;
    @FXML
    private Pane paneGameOver;
    @FXML
    private Label labelPause, lableGameOver;
    @FXML
    private Text score;
    @FXML
    private Button restartButton;
    private final int BG_WIDTH = 714;
    private ParallelTransition parallelTransition;

    private TranslateTransition enemyTransirion;
    public static boolean jump = false;
    public static boolean right = false;
    public static boolean left = false;
    public static boolean gameRunning = false;
    private boolean isAlive = true;
    public static boolean isPouse = false;
    private static int scoreCount=0;
    private int playerSpeed = 3, jumpDownSpeed = 5, enemySpeed = 3500;

    private MediaPlayer mediaPlayer;

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            score.setText(Integer.toString(scoreCount));



            if (jump && player.getLayoutY() > 20f) {
                player.setLayoutY(player.getLayoutY() - playerSpeed);
                playerJump.setLayoutY(playerJump.getLayoutY() - playerSpeed);
                playerJump.setVisible(true);
                player.setVisible(false);
            } else if (player.getLayoutY() <= 181f) {
                jump = false;
                player.setLayoutY(player.getLayoutY() + jumpDownSpeed);
                playerJump.setLayoutY(playerJump.getLayoutY() + jumpDownSpeed);
                playerJump.setVisible(false);
                player.setVisible(true);

            }
            if (right && player.getLayoutX() < 200f) {
                player.setLayoutX(player.getLayoutX() + playerSpeed);
                playerJump.setLayoutX(playerJump.getLayoutX() + playerSpeed);
            }
            if (left && player.getLayoutX() > 28f) {
                player.setLayoutX(player.getLayoutX() - playerSpeed);
                playerJump.setLayoutX(playerJump.getLayoutX() - playerSpeed);
            }
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

            if (isPouse && !labelPause.isVisible()) {
                labelPause.setVisible(true);
                gameRunning = false;
            } else if (!isPouse && labelPause.isVisible()) {
                labelPause.setVisible(false);
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
        enemySpeed=3500;
        scoreCount=0;
    }
protected void fonMusic() {
    if (mediaPlayer == null){
        try {
            String fileName = getClass().getResource("/sounds/ABitOfHope.mp3").toURI().toString();
            Media media = new Media(fileName);
            mediaPlayer = new MediaPlayer(media);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    mediaPlayer.setOnEndOfMedia(() -> {
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    });
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

        enemyTransirion = new TranslateTransition(Duration.millis(enemySpeed), enemy);
        enemyTransirion.setFromX(0);
        enemyTransirion.setToX(BG_WIDTH * -1 -100);
        enemyTransirion.setInterpolator(Interpolator.LINEAR);
//        enemyTransirion.setCycleCount(Animation.INDEFINITE);
        enemyTransirion.setOnFinished(event -> {
            scoreCount++;
            enemySpeed=enemySpeed-50;
            System.out.println("enemySpeed = " + enemySpeed);
            enemyTransirion.setDuration(Duration.millis(enemySpeed));
            enemyTransirion.play();
                });
        enemyTransirion.play();


        parallelTransition = new ParallelTransition(bgOneTransirion, bgTwoTransirion);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();
        timer.start();
        gameRunning = true;
        fonMusic();
        mediaPlayer.play();
    }

}
