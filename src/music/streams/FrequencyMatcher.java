package src.music.streams;

import java.util.ArrayList;

import src.music.streams.MusicStream.OnStreamDoneListener;
import src.util.MoreMaths;

public class FrequencyMatcher implements MusicStream, MusicStream.OnStreamDoneListener {

    MusicStream slowStream;

    double targetFreq, actualFreq, targetPeriod, actualPeriod, timeSinceActualSample;

    short lastActualSample, nextActualSample;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    public FrequencyMatcher(MusicStream slowStream, float targetFreq) {
        this.slowStream = slowStream;
        slowStream.addOnStreamDoneListener(this);

        this.targetFreq = targetFreq;

        actualFreq = slowStream.getFrequency();

        targetPeriod = 1 / targetFreq;
        actualPeriod = 1 / actualFreq;

        timeSinceActualSample = 0;
    }

    @Override
    public float getFrequency() {
        return (float) targetFreq;
    }

    @Override
    public short getNextSample() {
        timeSinceActualSample += targetPeriod;

        if (timeSinceActualSample >= actualPeriod) {
            lastActualSample = slowStream.getNextSample();
            nextActualSample = slowStream.peekNextSample();

            timeSinceActualSample -= actualPeriod;
        }

        short syntheticSample = MoreMaths.lerp(lastActualSample, nextActualSample,
                (timeSinceActualSample / actualPeriod));

        return syntheticSample;
    }

    @Override
    public short peekNextSample() {

        short syntheticSample;
        if (timeSinceActualSample + targetPeriod > actualPeriod) { // if next step will leave the current sample range,
                                                                   // approximate with closest bound
            syntheticSample = nextActualSample;
        } else {
            syntheticSample = MoreMaths.lerp(lastActualSample, nextActualSample,
                    (timeSinceActualSample / actualPeriod));
        }

        return syntheticSample;
    }

    @Override
    public short[] getNextBlock(int numTargetSamples) {
        short[] block = new short[numTargetSamples];

        int numActualSamples = (int) Math.floor(numTargetSamples * (actualFreq / targetFreq)) + 1;
        int currentActualSample = 0;

        short[] actualBlock = slowStream.getNextBlock(numActualSamples);
        short peekedSample = slowStream.peekNextSample();

        for (int i = 0; i < numTargetSamples; i++) {
            timeSinceActualSample += targetPeriod;

            if (timeSinceActualSample >= actualPeriod) {
                lastActualSample = actualBlock[currentActualSample];

                if (currentActualSample < numActualSamples - 1) {
                    nextActualSample = actualBlock[currentActualSample + 1];
                } else {
                    nextActualSample = peekedSample;
                }

                currentActualSample++;
                timeSinceActualSample -= actualPeriod;
            }

            block[i] = MoreMaths.lerp(lastActualSample, nextActualSample,
                    (timeSinceActualSample / actualPeriod));
        }

        return block;
    }

    @Override
    public void addOnStreamDoneListener(OnStreamDoneListener listener) {
        doneListeners.add(listener);
    }

    @Override
    public void OnStreamDone(MusicStream CompletedStream) {
        // source stream done, so pass on notify
        for (OnStreamDoneListener onStreamDoneListener : doneListeners) {
            onStreamDoneListener.OnStreamDone(this);
        }
    }

}
