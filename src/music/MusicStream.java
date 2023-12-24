package src.music;

public interface MusicStream {

    public float getFrequency();

    public Byte[] getNextSample();

    public Byte[] peekNextSample();

}
