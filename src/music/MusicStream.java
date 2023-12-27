package src.music;

public interface MusicStream {

    public float getFrequency();

    public short getNextSample();

    public short peekNextSample();

    public short[] getNextBlock(int requestedLength);

}
