package src.music.streams;

import java.util.ArrayList;

import src.util.MoreMaths;

public class FrequencyMatcher implements MusicStream, MusicStream.OnStreamDoneListener {

    MusicStream actualStream;

    double targetFreq, actualFreq, targetPeriod, actualPeriod, timeSinceLastSample;

    short lastActualSample, nextActualSample;

    boolean downSample;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    public FrequencyMatcher(MusicStream actualStream, float targetFreq) {
        this.actualStream = actualStream;
        actualStream.addOnStreamDoneListener(this);

        this.targetFreq = targetFreq;

        actualFreq = actualStream.getFrequency();

        targetPeriod = 1 / targetFreq;
        actualPeriod = 1 / actualFreq;

        downSample = (targetPeriod >= actualPeriod);

        timeSinceLastSample = 0;
    }

    @Override
    public float getFrequency() {
        return (float) targetFreq;
    }

    @Override
    public short getNextSample() {
        if (downSample) {
            // the actual stream is too fast

            // take until there's enough time for one sample

            timeSinceLastSample += targetPeriod;
            while (timeSinceLastSample > 2 * actualPeriod) {
                timeSinceLastSample -= actualPeriod;
                actualStream.getNextSample(); // discard un-needed samples
            }

            return actualStream.getNextSample();
        }
        // else if up sample

        timeSinceLastSample += targetPeriod;

        if (timeSinceLastSample >= actualPeriod) {
            lastActualSample = actualStream.getNextSample();
            nextActualSample = actualStream.peekNextSample();

            timeSinceLastSample -= actualPeriod;
        }

        short syntheticSample = MoreMaths.lerp(lastActualSample, nextActualSample,
                (timeSinceLastSample / actualPeriod));

        return syntheticSample;
    }

    @Override
    public short peekNextSample() {
        if (downSample) {
            // the actual stream is too fast

            // take until there's enough time for one sample

            // double timeToTake = timeSinceLastSample + targetPeriod;
            // while (timeToTake > 2 * actualPeriod) {
            // timeToTake -= actualPeriod;
            // actualStream.?NextSample(); // how to peek ahead multiple samples?
            // }

            return actualStream.peekNextSample();
        }
        // else if up sample

        short syntheticSample;
        if (timeSinceLastSample + targetPeriod > actualPeriod) { // if next step will leave the current sample range,
                                                                 // approximate with closest bound
            syntheticSample = nextActualSample;
        } else {
            syntheticSample = MoreMaths.lerp(lastActualSample, nextActualSample,
                    (timeSinceLastSample / actualPeriod));
        }

        return syntheticSample;
    }

    @Override
    public short[] getNextBlock(int numTargetSamples) {
        short[] block = new short[numTargetSamples];
        double sampleRatio = (actualFreq / targetFreq);
        int numActualSamples = (int) Math.floor(numTargetSamples * sampleRatio) + 1;

        if (downSample) {
            // the actual stream is too fast

            int foundTargetSamples = 0;
            short[] actualBlock = actualStream.getNextBlock(numActualSamples);

            while (foundTargetSamples < numTargetSamples) {
                block[foundTargetSamples] = actualBlock[(int) (foundTargetSamples * sampleRatio)];
                foundTargetSamples++;
            }

            return block;
        }
        // else if up sample

        int currentActualSample = 0;

        short[] actualBlock = actualStream.getNextBlock(numActualSamples);
        short peekedSample = actualStream.peekNextSample();

        for (int i = 0; i < numTargetSamples; i++) {
            timeSinceLastSample += targetPeriod;

            if (timeSinceLastSample >= actualPeriod) {
                lastActualSample = actualBlock[currentActualSample];

                if (currentActualSample < numActualSamples - 1) {
                    nextActualSample = actualBlock[currentActualSample + 1];
                } else {
                    nextActualSample = peekedSample;
                }

                currentActualSample++;
                timeSinceLastSample -= actualPeriod;
            }

            block[i] = MoreMaths.lerp(lastActualSample, nextActualSample,
                    (timeSinceLastSample / actualPeriod));
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
