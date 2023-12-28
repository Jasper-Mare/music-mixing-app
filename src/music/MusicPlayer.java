package src.music;

import src.music.streams.MusicStream;

public interface MusicPlayer {

    public void play() throws PlaybackError;

    public void pause();

    public MusicStream getSoundStream();

    public void setSoundStream(MusicStream stream);

    public class PlaybackError extends Exception {
        public PlaybackError(String str) {
            super(str);
        }
    }

}
