package src.music.streams;

import java.util.ArrayList;

import src.util.Func;

public class MusicStreamMerger implements MusicStream, MusicStream.OnStreamDoneListener {

    private MusicStream trackA;
    private MusicStream trackB;

    private Func<MergeData, Short> sampleMergeFunc;

    private float freq;
    private double time, duration, period;

    private boolean doneA, doneB;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    public MusicStreamMerger(MusicStream trackA, MusicStream trackB,
            Func<MergeData, Short> sampleMergeFunc, double duration) {
        this.sampleMergeFunc = sampleMergeFunc;

        doneA = false;
        doneB = false;

        time = 0;
        this.duration = duration;

        // find max polling frequency, and if a track is below that rate, use a
        // frequency matcher to pull it up to speed

        freq = Math.max(trackA.getFrequency(), trackB.getFrequency());
        period = 1 / freq;

        boolean aFullFreq, bFullFreq;
        aFullFreq = (trackA.getFrequency() == freq);
        bFullFreq = (trackB.getFrequency() == freq);

        if (aFullFreq) {
            this.trackA = trackA;
        } else {
            this.trackA = new FrequencyMatcher(trackA, freq);
        }

        if (bFullFreq) {
            this.trackB = trackB;
        } else {
            this.trackB = new FrequencyMatcher(trackB, freq);
        }

        this.trackA.addOnStreamDoneListener(this);
        this.trackB.addOnStreamDoneListener(this);

    }

    // infinite duration means not time dependant
    public MusicStreamMerger(MusicStream trackA, MusicStream trackB,
            Func<MergeData, Short> sampleMergeFunc) {
        this(trackA, trackB, sampleMergeFunc, Double.POSITIVE_INFINITY);
    }

    @Override
    public float getFrequency() {
        return freq;
    }

    @Override
    public short getNextSample() {
        time += freq;

        short sampleA = trackA.getNextSample();
        short sampleB = trackB.getNextSample();

        if (time >= duration) {
            notifyOnStreamDoneListeners();
        }

        return sampleMergeFunc.Run(new MergeData(sampleA, sampleB, time, duration));
    }

    @Override
    public short peekNextSample() {
        short sampleA = trackA.peekNextSample();
        short sampleB = trackB.peekNextSample();

        return sampleMergeFunc.Run(new MergeData(sampleA, sampleB, time + period, duration));
    }

    @Override
    public short[] getNextBlock(int requestedLength) {
        short[] blockA = trackA.getNextBlock(requestedLength);
        short[] blockB = trackB.getNextBlock(requestedLength);
        short[] blockOut = new short[requestedLength];

        for (int i = 0; i < requestedLength; i++) {
            time += period;
            blockOut[i] = sampleMergeFunc.Run(new MergeData(blockA[i], blockB[i], time, duration));
        }

        if (time >= duration) {
            notifyOnStreamDoneListeners();
        }

        return blockOut;
    }

    /**
     * @param sampleA  the current sample from track A
     * @param sampleB  the current sample from track B
     * @param time     how far into the transition we are
     * @param duration how long the transition lasts
     */
    public static record MergeData(short sampleA, short sampleB, double time, double duration) {
    }

    @Override
    public void addOnStreamDoneListener(OnStreamDoneListener listener) {
        doneListeners.add(listener);
    }

    @Override
    public void OnStreamDone(MusicStream completedStream) {
        // an underlying stream is done,

        if (completedStream == trackA) {
            doneA = true;
        } else if (completedStream == trackB) {
            doneB = true;
        }

        // if both done and not time dependant then propogate
        if (doneA && doneB && Double.isInfinite(duration)) {
            notifyOnStreamDoneListeners();
        }

    }

    private void notifyOnStreamDoneListeners() {
        for (OnStreamDoneListener onStreamDoneListener : doneListeners) {
            onStreamDoneListener.OnStreamDone(this);
        }
    }

}
