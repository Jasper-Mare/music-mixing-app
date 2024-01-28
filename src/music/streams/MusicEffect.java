package src.music.streams;

import java.util.ArrayList;

import src.util.Func;

public class MusicEffect implements MusicStream, MusicStream.OnStreamDoneListener {

    private MusicStream inputStream;

    private Func<EffectData, Short> effectorFunc;

    private float freq;
    private double time, duration, period;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    public MusicEffect(MusicStream inputStream, Func<EffectData, Short> effectorFunc) {
        this(inputStream, effectorFunc, Double.POSITIVE_INFINITY);
    }

    public MusicEffect(MusicStream inputStream, Func<EffectData, Short> effectorFunc, double duration) {
        this.effectorFunc = effectorFunc;
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

        short sample = inputStream.getNextSample();

        if (time > duration) {
            notifyDone();
        }

        return effectorFunc.Run(new EffectData(sample, time, duration));
    }

    @Override
    public short peekNextSample() {
        short sample = inputStream.peekNextSample();

        return effectorFunc.Run(new EffectData(sample, time + period, duration));
    }

    @Override
    public short[] getNextBlock(int requestedLength) {
        short[] blockIn = inputStream.getNextBlock(requestedLength);
        short[] blockOut = new short[requestedLength];

        for (int i = 0; i < requestedLength; i++) {
            time += period;
            blockOut[i] = effectorFunc.Run(new EffectData(blockIn[i], time, duration));
        }

        if (time > duration) {
            notifyDone();
        }

        return blockOut;
    }

    @Override
    public void addOnStreamDoneListener(OnStreamDoneListener listener) {
        doneListeners.add(listener);
    }

    private void notifyDone() {
        for (OnStreamDoneListener onStreamDoneListener : doneListeners) {
            onStreamDoneListener.OnStreamDone(this);
        }
    }

    @Override
    public void OnStreamDone(MusicStream CompletedStream) {
        // underlying stream is done, so propogate the info
        notifyDone();
    }

    /**
     * @param sample   the current sample from track
     * @param time     how far into the effect we are
     * @param duration how long the effect lasts
     */
    public static record EffectData(short sample, double time, double duration) {
    }

}
