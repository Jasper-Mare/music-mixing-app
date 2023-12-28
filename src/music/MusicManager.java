package src.music;

import src.music.MusicPlayer.PlaybackError;
import src.music.playlists.Playlist;
import src.music.streams.*;

/**
 * Organises music streams.
 */
public class MusicManager implements MusicStream.OnStreamDoneListener {

    private MusicPlayer outputPlayer;

    private Playlist currentPlaylist;

    public MusicManager(MusicPlayer output) {
        this.outputPlayer = output;

        clearPlaylist();
    }

    public void setPlaylist(Playlist playlist) {
        currentPlaylist = playlist;

        try {
            outputPlayer.setSoundStream(playlist.getCurrent());
        } catch (PlaybackError e) {
            e.printStackTrace();
            clearPlaylist();
        }

        // subscribe for when the first stream ends
        currentPlaylist.getCurrent().addOnStreamDoneListener(this);
    }

    public void clearPlaylist() {
        currentPlaylist = null;
        try {
            outputPlayer.setSoundStream(new EmptyStream());
        } catch (PlaybackError e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            outputPlayer.start();
        } catch (PlaybackError e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        try {
            outputPlayer.resume();
        } catch (PlaybackError e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            outputPlayer.pause();
        } catch (PlaybackError e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            outputPlayer.stop();
        } catch (PlaybackError e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnStreamDone(MusicStream completedStream) {
        if (completedStream == currentPlaylist.getCurrent()) {
            currentPlaylist.NextStream();

            try {
                outputPlayer.setSoundStream(currentPlaylist.getCurrent());
            } catch (PlaybackError e) {
                e.printStackTrace();
            }

            // listen for when the new current stream ends
            currentPlaylist.getCurrent().addOnStreamDoneListener(this);
        }
    }

}
