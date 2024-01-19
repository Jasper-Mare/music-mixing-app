package src.music.files;

import java.io.IOException;

import src.music.streams.MusicStream;

public interface MusicFile {
    public MusicStream getMusicStream();

    public void openFile(String fileName) throws IOException;
}
