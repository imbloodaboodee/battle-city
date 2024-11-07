package physics;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundUtility {
    private static final Map<String, Clip> soundClips = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Load different sound files
     */
    public static void initialize() {
        System.out.println("INITIALIZE");
        String[] soundNames = {
                "bulletBrick", "bulletTank", "fire", "explosion1", "explosion2",
                "startStage", "pause", "powerupAppear", "powerupPick", "gameover", "statistics"
        };

        String[] filePaths = {
                "./src/assets/sound/bullet_hit_2.wav",
                "./src/assets/sound/bullet_hit_1.wav",
                "./src/assets/sound/bullet_shot.wav",
                "./src/assets/sound/explosion_1.wav",
                "./src/assets/sound/explosion_2.wav",
                "./src/assets/sound/stage_start.wav",
                "./src/assets/sound/pause.wav",
                "./src/assets/sound/powerup_appear.wav",
                "./src/assets/sound/powerup_pick.wav",
                "./src/assets/sound/game_over.wav",
                "./src/assets/sound/statistics_1.wav"
        };

        try {
            for (int i = 0; i < soundNames.length; i++) {
                Clip clip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePaths[i]));
                clip.open(ais);
                soundClips.put(soundNames[i], clip);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(SoundUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        initialized = true;
    }

    private static void playSound(String soundName) {
        if (!initialized) {
            initialize();
        }
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0); // Reset to the start
            clip.start();
        }
    }

    public static void BulletHitBrick() { playSound("bulletBrick"); }
    public static void BulletHitTank() { playSound("bulletTank"); }
    public static void fireSound() { playSound("fire"); }
    public static void explosion1() { playSound("explosion1"); }
    public static void explosion2() { playSound("explosion2"); }
    public static void startStage() { playSound("startStage"); }
    public static void pause() { playSound("pause"); }
    public static void powerupAppear() { playSound("powerupAppear"); }
    public static void powerupPick() { playSound("powerupPick"); }
    public static void gameOver() { playSound("gameover"); }
    public static void statistics() { playSound("statistics"); }
}
