package com.meowmivice.game;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class Audio {
    static Long currentFrame;
    static private Clip clip;
    static private AudioInputStream audioStream;
    static private File file = new File("resources/Audio/mystery.wav");

    static void audio() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    static void resumeAudio() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        clip.close();
        resetAudio();
        clip.setMicrosecondPosition(currentFrame);
        clip.start();

    }
    public static void pauseAudio() {
        currentFrame = clip.getMicrosecondPosition();
        clip.stop();
    }

    public static void resetAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioStream = AudioSystem.getAudioInputStream(file);
        clip.open(audioStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}