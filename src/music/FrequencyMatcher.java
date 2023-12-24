package src.music;

import src.util.MoreMaths;

public class FrequencyMatcher implements MusicStream {

    MusicStream slowStream;

    double targetFreq, actualFreq, targetPeriod, actualPeriod, timeSinceActualSample;

    float lastActualSample, nextActualSample;

    public FrequencyMatcher(MusicStream slowStream, float targetFreq) {
        this.slowStream = slowStream;
        this.targetFreq = targetFreq;

        targetPeriod = 1 / targetFreq;
        actualPeriod = 1 / actualFreq;

        actualFreq = slowStream.getFrequency();

        timeSinceActualSample = 0;
    }

    @Override
    public float getFrequency() {
        return (float) targetFreq;
    }

    @Override
    public Byte[] getNextSample() {
        timeSinceActualSample += targetPeriod;

        if (timeSinceActualSample >= actualPeriod) {
            lastActualSample = MusicUtils.sampleToFloat(slowStream.getNextSample());
            nextActualSample = MusicUtils.sampleToFloat(slowStream.peekNextSample());

            timeSinceActualSample -= actualPeriod;
        }

        float syntheticSample = (float) MoreMaths.lerp(lastActualSample, nextActualSample,
                (timeSinceActualSample / actualPeriod));

        return MusicUtils.floatToSample(syntheticSample);
    }

    // assumes next step won't go over another actual sample, might result in weird
    // sound idk
    @Override
    public Byte[] peekNextSample() {
        float syntheticSample = (float) MoreMaths.lerp(lastActualSample, nextActualSample,
                ((timeSinceActualSample + targetPeriod) / actualPeriod));

        return MusicUtils.floatToSample(syntheticSample);
    }

}
