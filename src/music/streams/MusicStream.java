package src.music.streams;

public interface MusicStream {

    public float getFrequency();

    public short getNextSample();

    public short peekNextSample();

    public short[] getNextBlock(int requestedLength);

    public void addOnStreamDoneListener(OnStreamDoneListener listener);

    public interface OnStreamDoneListener {
        public void OnStreamDone(MusicStream completedStream);
    }
}
