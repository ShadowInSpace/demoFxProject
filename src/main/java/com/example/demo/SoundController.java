package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;

public class SoundController {
    private static MediaPlayer jumpPlayer;

    protected static void jumpSound() {
        if (jumpPlayer == null) {
            try {
                String fileName = SoundController.class.getResource("/sounds/jump.mp3").toURI().toString();
                Media media = new Media(fileName);
                jumpPlayer = new MediaPlayer(media);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        jumpPlayer.seek(Duration.ZERO);
        jumpPlayer.play();

    }
}