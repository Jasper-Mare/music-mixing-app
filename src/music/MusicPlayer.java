package src.music;

public interface MusicPlayer {

    public void play() throws PlaybackError;

    public void pause();

    public MusicStream getSoundStream();

    public void setSoundStream(MusicStream stream);

    public class PlaybackError extends Exception {
        PlaybackError(String str) {
            super(str);
        }
    }

}
