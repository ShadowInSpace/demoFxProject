package com.example.demo;

import javafx.animation.*;
import javafx.fxml.FXML;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    private ImageView bg1, bg2, player, enemy,enemyAttack, playerJump;
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

    private TranslateTransition enemyTransition, enemyATransition;
    public static boolean jump = false;
    public static boolean right = false;
    public static boolean left = false;
    public static boolean gameRunning = false;
    private boolean isAlive = true;
    private int lives=3;
    public static boolean isPouse = false;
    private static int scoreCount=0;
    private int playerSpeed = 3, jumpDownSpeed = 5, enemySpeed = 3500;

    private MediaPlayer mediaPlayer, jumpPlayer;

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
                enemyTransition.play();
                enemyATransition.play();
            } else {
                playerSpeed = 0;
                jumpDownSpeed = 0;
                parallelTransition.pause();
                enemyTransition.pause();
                enemyATransition.pause();
            }

            if (isPouse && !labelPause.isVisible()) {
                labelPause.setVisible(true);
                gameRunning = false;
            } else if (!isPouse && labelPause.isVisible()) {
                labelPause.setVisible(false);
                gameRunning = true;
            }

            if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                hitByEnemy();
            }
            if(lives>2){
                heart1.setVisible(true);
                heart2.setVisible(true);
                heart3.setVisible(true);
            } else if (lives>1) {
                heart1.setVisible(true);
                heart2.setVisible(true);
                heart3.setVisible(false);
            } else if (lives>0) {
                heart1.setVisible(true);
                heart2.setVisible(false);
                heart3.setVisible(false);
            } else {
                heart1.setVisible(false);
                heart2.setVisible(false);
                heart3.setVisible(false);
            }

        }
    };

    @FXML
    void restart() {
        enemyTransition.playFromStart();
        enemyATransition.playFromStart();
        gameRunning=true;
        paneGameOver.setVisible(false);
        enemySpeed=3500;
        scoreCount=0;
        lives = 3;
    }

    private void hitByEnemy(){
        lives--;
//        enemy.setVisible(false);
        Image image = null;

            image = new Image("C:\\Users\\ShadowSpace\\IdeaProjects\\testProjects\\FXProjects\\demo\\src\\main\\resources\\images\\octopusAttac.png");
//todo треба поправити щоб працювало по відносному посиланню
        enemy.setImage(image);
        enemyAttack.setVisible(true);
        parallelTransition.pause();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        parallelTransition.play();
//        enemy.setVisible(true);
//        enemyAttack.setVisible(false);

        if(lives<1) {
            paneGameOver.setVisible(true);
            gameRunning = false;
        }

        enemyTransition.playFromStart();
        enemyATransition.playFromStart();
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

        enemyTransition = new TranslateTransition(Duration.millis(enemySpeed), enemy);
        enemyTransition.setFromX(0);
        enemyTransition.setToX(BG_WIDTH * -1 -100);
        enemyTransition.setInterpolator(Interpolator.LINEAR);
//        enemyTransirion.setCycleCount(Animation.INDEFINITE);
        enemyTransition.setOnFinished(event -> {
            scoreCount++;
            enemySpeed=enemySpeed-50;
            System.out.println("enemySpeed = " + enemySpeed);
            enemyTransition.setDuration(Duration.millis(enemySpeed));
            enemyTransition.play();
                });
        enemyTransition.play();

        enemyATransition = new TranslateTransition(Duration.millis(enemySpeed), enemyAttack);
        enemyATransition.setFromX(0);
        enemyATransition.setToX(BG_WIDTH * -1 -100);
        enemyATransition.setInterpolator(Interpolator.LINEAR);
        enemyATransition.setOnFinished(event -> {
            scoreCount++;
            enemySpeed=enemySpeed-50;
            System.out.println("enemySpeed = " + enemySpeed);
            enemyATransition.setDuration(Duration.millis(enemySpeed));
            enemyATransition.play();
        });
        enemyATransition.play();


        parallelTransition = new ParallelTransition(bgOneTransirion, bgTwoTransirion);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();
        timer.start();
        gameRunning = true;
        fonMusic();
        mediaPlayer.play();
    }

}
