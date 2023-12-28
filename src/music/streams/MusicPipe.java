package src.music.streams;

import java.util.ArrayList;

public class MusicPipe implements MusicStream, MusicStream.OnStreamDoneListener {

    private MusicStream inputStream;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    public MusicPipe(MusicStream inputStream) {
        this.inputStream = inputStream;
        inputStream.addOnStreamDoneListener(this);
    }

    @Override
    public float getFrequency() {
        return inputStream.getFrequency();
    }

    @Override
    public short getNextSample() {
        return inputStream.getNextSample();
    }

    @Override
    public short peekNextSample() {
        return inputStream.peekNextSample();
    }

    @Override
    public short[] getNextBlock(int requestedLength) {
        return inputStream.getNextBlock(requestedLength);
    }

    @Override
    public void addOnStreamDoneListener(OnStreamDoneListener listener) {
        doneListeners.add(listener);
    }

    @Override
    public void OnStreamDone(MusicStream CompletedStream) {
        // underlying stream is done, so propogate the info
        for (OnStreamDoneListener onStreamDoneListener : doneListeners) {
            onStreamDoneListener.OnStreamDone(this);
        }
    }

}
