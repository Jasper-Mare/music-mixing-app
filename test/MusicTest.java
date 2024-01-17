package test;

import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.UnsupportedAudioFileException;

import src.desktop.DesktopMusicPlayer;
import src.music.*;
import src.music.streams.*;
import src.music.MusicPlayer.PlaybackError;
import src.music.files.Mp3Reader;
import src.music.playlists.Playlist;

public class MusicTest {

    public static void main(String[] args)
            throws PlaybackError, InterruptedException, IOException, UnsupportedAudioFileException {
        testMusicManager();
    }

    static void testMusicManager() throws PlaybackError, IOException, UnsupportedAudioFileException {
        MusicPlayer outPlayer = new DesktopMusicPlayer();
        MusicManager manager = new MusicManager(outPlayer);

        LinkedList<MusicStream> streamSequence = new LinkedList<>();

        Mp3Reader reader = new Mp3Reader();
        reader.openFile("/home/jasper/Music/David Byrne/Rei Momo/1 - Independence Day.mp3");

        streamSequence.add(new LimitedDurationStream(new ToneGenerator(), 5));
        streamSequence.add(reader.getMusicStream());
        streamSequence.add(new LimitedDurationStream(new NoiseGenerator(), 5));
        streamSequence.add(new LimitedDurationStream(new FluctuatingNoiseGenerator(), 5));

        Playlist playlist = new Playlist(streamSequence);

        manager.setPlaylist(playlist);

        manager.start();

    }

    static class ToneGenerator implements MusicStream {
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

    static class NoiseGenerator implements MusicStream {
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

    static class FluctuatingNoiseGenerator implements MusicStream {
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
