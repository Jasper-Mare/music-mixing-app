package test;

import java.util.LinkedList;

import src.music.*;
import src.music.streams.*;
import src.music.MusicPlayer.PlaybackError;
import src.music.desktopMusic.DesktopMusicPlayer;
import src.music.playlists.Playlist;

public class MusicTest {

    public static void main(String[] args) throws PlaybackError, InterruptedException {
        testMusicManager();
    }

    static void testMusicManager() throws PlaybackError {
        MusicPlayer outPlayer = new DesktopMusicPlayer();
        // MusicManager manager = new MusicManager(outPlayer);
        //
        // LinkedList<MusicStream> streamSequence = new LinkedList<>();
        //
        // streamSequence.add(new LimitedDurationStream(new MusicGeneratorA(), 5_000));
        // streamSequence.add(new LimitedDurationStream(new MusicGeneratorB(), 5_000));
        // streamSequence.add(new LimitedDurationStream(new MusicGeneratorC(), 5_000));
        //
        // Playlist playlist = new Playlist(streamSequence);
        //
        // manager.setPlaylist(playlist);
        //
        // manager.start();

        // outPlayer.setSoundStream(new LimitedDurationStream(new MusicGeneratorB(),
        // 5_000));
        outPlayer.setSoundStream(new MusicGeneratorB());

        outPlayer.start();

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
