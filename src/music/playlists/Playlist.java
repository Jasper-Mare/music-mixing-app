package src.music.playlists;

import java.util.LinkedList;

import src.music.streams.EmptyStream;
import src.music.streams.MusicStream;

public class Playlist {
    LinkedList<MusicStream> streams;

    public Playlist(LinkedList<MusicStream> contents) {
        // duplicate contents list so this one can be edited without editing the
        // original
        streams = new LinkedList<>(contents);
    }

    public MusicStream getCurrent() {
        if (streams.size() == 0) {
            return new EmptyStream();
        } else {
            return streams.getFirst();
        }
    }

    public void NextStream() {
        streams.removeFirst();
    }
}
