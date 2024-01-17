package src.music.streams;

import java.util.ArrayList;

public class LimitedDurationStream implements MusicStream, MusicStream.OnStreamDoneListener {
    private MusicStream inputStream;

    private float freq;
    private double time, duration, period; // times measured in seconds

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    public LimitedDurationStream(MusicStream inputStream, double duration) {
        this.inputStream = inputStream;

        inputStream.addOnStreamDoneListener(this);

        time = 0;
        this.duration = duration;

        freq = inputStream.getFrequency();
        period = 1 / freq;

    }

    @Override
    public float getFrequency() {
        return freq;
    }

    @Override
    public short getNextSample() {
        time += freq;

        if (time >= duration) {
            notifyDoneListeners();
        }

        return inputStream.getNextSample();
    }

    @Override
    public short peekNextSample() {
        return inputStream.getNextSample();
    }

    @Override
    public short[] getNextBlock(int requestedLength) {
        if (time + (period * requestedLength) > duration) {
            // the request stretches over the end of the time
            int numToEnd = (int) ((duration - time) / period);
            short[] blockIn = inputStream.getNextBlock(numToEnd);
            short[] blockOut = new short[requestedLength];

            for (int i = 0; i < numToEnd; i++) {
                time += period;
                blockOut[i] = blockIn[i];
            }

            notifyDoneListeners();

            return blockOut;
        } else {
            short[] blockIn = inputStream.getNextBlock(requestedLength);
            time += period * requestedLength;
            return blockIn;
        }
    }

    @Override
    public void addOnStreamDoneListener(OnStreamDoneListener listener) {
        doneListeners.add(listener);
    }

    @Override
    public void OnStreamDone(MusicStream CompletedStream) {
        // underlying stream is done, so propogate the info
        notifyDoneListeners();
    }

    private void notifyDoneListeners() {
        for (OnStreamDoneListener onStreamDoneListener : doneListeners) {
            onStreamDoneListener.OnStreamDone(this);
        }
    }
}
