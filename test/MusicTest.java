package test;

import java.util.ArrayList;

import src.music.*;
import src.music.streams.FrequencyMatcher;
import src.music.streams.MusicEffect;
import src.music.streams.MusicStream;
import src.music.streams.MusicStreamMerger;
import src.music.streams.MusicEffect.EffectData;
import src.music.streams.MusicStreamMerger.MergeData;
import src.music.MusicPlayer.PlaybackError;
import src.music.desktopMusic.DesktopMusicPlayer;
import src.util.Func;

public class MusicTest {

    public static void main(String[] args) throws PlaybackError, InterruptedException {

        // testGeneratorC(5_000);
        Thread.sleep(3_000);
        testMusicEffector(10_000);

    }

    static void testGeneratorA(long duration) throws InterruptedException {
        System.out.println("testing Generator A");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorA generator = new MusicGeneratorA();

        player.setSoundStream(generator);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static void testGeneratorB(long duration) throws InterruptedException {
        System.out.println("testing Generator B");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorB generator = new MusicGeneratorB();

        player.setSoundStream(generator);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static void testGeneratorC(long duration) throws InterruptedException {
        System.out.println("testing Generator C");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorC generator = new MusicGeneratorC();

        player.setSoundStream(generator);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static void testFrequencyMatcher(long duration) throws InterruptedException {
        System.out.println("testing FrequencyMatcher");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorA generator = new MusicGeneratorA();

        FrequencyMatcher frequencyMatcher = new FrequencyMatcher(generator, 44100); // exactly 4 times higher polling
                                                                                    // rate

        player.setSoundStream(frequencyMatcher);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static void testMusicStreamMergerAvg(long duration) throws InterruptedException {
        System.out.println("testing Merger");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorA generatorA = new MusicGeneratorA();
        MusicGeneratorB generatorB = new MusicGeneratorB();

        Func<MergeData, Short> mergeAvg = (MergeData data) -> {
            return (short) ((data.sampleA() + data.sampleB()) / 2);
        };
        MusicStreamMerger merger = new MusicStreamMerger(generatorA, generatorB, mergeAvg, duration);

        player.setSoundStream(merger);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static void testMusicStreamMergerFade(long duration) throws InterruptedException {
        System.out.println("testing Merger");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorA generatorA = new MusicGeneratorA();
        MusicGeneratorB generatorB = new MusicGeneratorB();

        Func<MergeData, Short> mergeFade = (MergeData data) -> {
            float lvlA = (float) (data.time() / data.duration()),
                    lvlB = 1 - lvlA;

            return (short) (data.sampleA() * lvlA + data.sampleB() * lvlB);
        };
        MusicStreamMerger merger = new MusicStreamMerger(generatorA, generatorB, mergeFade, duration);

        player.setSoundStream(merger);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static void testMusicEffector(long duration) throws InterruptedException {
        System.out.println("testing Effector");

        DesktopMusicPlayer player = new DesktopMusicPlayer();
        MusicGeneratorC generator = new MusicGeneratorC();

        Func<EffectData, Short> mergeFade = new Func<EffectData, Short>() {

            ArrayList<Short> sampleList = new ArrayList<Short>();
            boolean startedSampling = false, doneSampling = false, playingBack = false;
            int pointInSample;
            double sampleDuration = Math.random() * 3, playbackStart, sampleStartTime = Math.random() * 3;

            @Override
            public Short Run(EffectData data) {
                if (startedSampling && !doneSampling) {
                    // currently sampling

                    sampleList.add(data.sample());

                    if (data.time() - sampleStartTime >= sampleDuration) {
                        // sampling over
                        doneSampling = true;
                        playbackStart = data.time() + (Math.random() * 3);
                    }

                    return data.sample();
                } else if (doneSampling && playingBack) {
                    // playing sample

                    short sample = sampleList.get(pointInSample);
                    pointInSample++;

                    if (pointInSample >= sampleList.size()) {
                        // stop playing sample
                        playingBack = false;
                        sampleStartTime = data.time() + Math.random() * 3;
                    }

                    return sample;
                } else {
                    // regular playing

                    if (doneSampling && data.time() >= playbackStart) {
                        // start playing sample
                        playingBack = true;
                        pointInSample = 0;
                    } else if (!doneSampling && sampleStartTime <= data.time()) {
                        // start sampling
                        startedSampling = true;
                    }

                    return data.sample();
                }
            }

        };
        MusicEffect merger = new MusicEffect(generator, mergeFade, duration);

        player.setSoundStream(merger);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
                e.printStackTrace();
            }
        })).start();

        Thread.sleep(duration);

        player.pause();
    }

    static class MusicGeneratorA implements MusicStream {
        private float t = 0, period = 1 / getFrequency();

        @Override
        public float getFrequency() {
            return 11000;
        }

        @Override
        public short getNextSample() {
            t += period;
            return MusicUtils.floatToSample(generateTone(t));
        }

        @Override
        public short peekNextSample() {
            return MusicUtils.floatToSample(generateTone(t + period));
        }

        private float generateTone(float t) {
            float ang = (t % 1) * 2 * (float) Math.PI,
                    a = ang * 440, b = ang * 329, c = ang * 392;

            return (float) (((Math.sin(a) + Math.sin(b) + Math.sin(c)) * 0.333) * Math.pow(2, Math.sin(t)) * 0.5);
        }

        @Override
        public short[] getNextBlock(int requestedLength) {
            short[] block = new short[requestedLength];

            for (int i = 0; i < requestedLength; i++) {
                block[i] = getNextSample();
            }

            return block;
        }

        @Override
        public void addOnStreamDoneListener(OnStreamDoneListener listener) {
            return; // never ends, so don't bother with this
        }

    }

    static class MusicGeneratorB implements MusicStream {
        private float t = 0, period = 1 / getFrequency();

        @Override
        public float getFrequency() {
            return 11000;
        }

        @Override
        public short getNextSample() {
            t += period;
            return MusicUtils.floatToSample(generateTone(t));
        }

        @Override
        public short peekNextSample() {
            return MusicUtils.floatToSample(generateTone(t + period));
        }

        private float generateTone(float t) {
            // float ang = ((440 * t) % 1) * 2 * (float) Math.PI;
            // return (float) (Math.sin(ang) * Math.sin(t * 0.25));
            return (float) (Math.random() * 2) - 1;
        }

        @Override
        public short[] getNextBlock(int requestedLength) {
            short[] block = new short[requestedLength];

            for (int i = 0; i < requestedLength; i++) {
                block[i] = getNextSample();
            }

            return block;
        }

        @Override
        public void addOnStreamDoneListener(OnStreamDoneListener listener) {
            return; // never ends, so don't bother with this
        }

    }

    static class MusicGeneratorC implements MusicStream {
        private float t = 0, period = 1 / getFrequency();

        @Override
        public float getFrequency() {
            return 11000;
        }

        @Override
        public short getNextSample() {
            t += period;
            return MusicUtils.floatToSample(generateTone(t));
        }

        @Override
        public short peekNextSample() {
            return MusicUtils.floatToSample(generateTone(t + period));
        }

        private float generateTone(float t) {
            float thingA = (float) Math.pow(10, Math.cos(2 * Math.PI * t)) * 0.1f;
            float noise = (float) (Math.random() * 2) - 1;

            return thingA * noise;
        }

        @Override
        public short[] getNextBlock(int requestedLength) {
            short[] block = new short[requestedLength];

            for (int i = 0; i < requestedLength; i++) {
                block[i] = getNextSample();
            }

            return block;
        }

        @Override
        public void addOnStreamDoneListener(OnStreamDoneListener listener) {
            return; // never ends, so don't bother with this
        }

    }

}
