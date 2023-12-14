package src.music;

import java.util.stream.Stream;

public interface MusicPlayer<T> {

    public void play();

    public void pause();

    public Stream<T> getSoundStream();

    public Stream<T> setSoundStream();

}
