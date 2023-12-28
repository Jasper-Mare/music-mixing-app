package src.music.streams;

public class EmptyStream implements MusicStream {

    @Override
    public float getFrequency() {
        return 0;
    }

    @Override
    public short getNextSample() {
        return 0;
    }

    @Override
    public short peekNextSample() {
        return 0;
    }

    @Override
    public short[] getNextBlock(int requestedLength) {
        return new short[requestedLength];
    }

    @Override
    public void addOnStreamDoneListener(OnStreamDoneListener listener) {
        return; // it is never done
    }

}
