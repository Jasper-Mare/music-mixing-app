package src.music;

import src.util.Func;

public class MusicStreamMerger implements MusicStream {

    MusicStream trackA;
    MusicStream trackB;

    Func<MergeData, Byte[]> sampleMergeFunc;

    float freq;

    public MusicStreamMerger(MusicStream trackA, MusicStream trackB,
            Func<MergeData, Byte[]> sampleMergeFunc) {
        this.sampleMergeFunc = sampleMergeFunc;

        freq = Math.max(trackA.getFrequency(), trackB.getFrequency());

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

    }

    @Override
    public float getFrequency() {
        return freq;
    }

    @Override
    public Byte[] getNextSample() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNextSample'");
    }

    @Override
    public Byte[] peekNextSample() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'peekNextSample'");
    }

    public record MergeData(Byte[] sampleA, Byte[] sampleB, double time, double duration) {
    }

}
