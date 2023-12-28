package src.music;

import src.music.streams.MusicStream;

public interface MusicPlayer {

    public void start() throws PlaybackError;

    public void pause() throws PlaybackError;

    public void resume() throws PlaybackError;

    public void stop() throws PlaybackError;

    public MusicStream getSoundStream();

    public void setSoundStream(MusicStream stream) throws PlaybackError;

    public class PlaybackError extends Exception {
        public PlaybackError(String str) {
            super(str);
        }
    }

}
