package src.music.files;

import java.nio.file.*;
import java.io.*;

public class MusicTempDir {

    private static Path loc;

    private MusicTempDir() {
        throw new java.lang.InstantiationError();
    }

    public static Path getTempDir() throws IOException {
        if (loc == null) {
            loc = Files.createTempDirectory("MusicMixingTempMusic");
            System.out.println("temp dir at: " + loc.toString());
        }
        return loc;
    }

    public static Path getTempMusicFile() throws IOException {
        return Files.createTempFile(getTempDir(), "convMusic_", ".wav");
    }
}
