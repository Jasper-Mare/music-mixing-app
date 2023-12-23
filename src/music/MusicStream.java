package src.music;

import java.util.stream.Stream;

public interface MusicStream extends Stream<byte[]> {

    public float getFrequency();

}
