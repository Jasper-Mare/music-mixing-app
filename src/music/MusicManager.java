package src.music;

import src.util.Tree;
import src.music.streams.*;

/**
 * Organises music streams.
 */
public class MusicManager {

    MusicPlayer outputPlayer;

    Tree<MusicStream> currentStreams;

    public MusicManager(MusicPlayer output) {
        this.outputPlayer = output;

        currentStreams = new Tree<>();
    }

}
