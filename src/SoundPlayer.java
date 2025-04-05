import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
    public static void play(String path, boolean loop) {
        try {
            File file = new File(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
