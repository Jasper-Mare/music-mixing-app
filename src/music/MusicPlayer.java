package src.music;

import java.util.stream.Stream;

public interface MusicPlayer {

    public void play() throws PlaybackError;

    public void pause();

    public Stream getSoundStream();

    public Stream setSoundStream();

    public class PlaybackError extends Exception {
        PlaybackError(String str) {
            super(str);
        }
    }

}
