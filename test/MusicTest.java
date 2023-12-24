package test;

import src.music.*;
import src.music.MusicPlayer.PlaybackError;

public class MusicTest {

    public static void main(String[] args) throws PlaybackError, InterruptedException {
        DesktopMusicPlayer player = new DesktopMusicPlayer();
        DummyMusicReader generator = new DummyMusicReader();

        FrequencyMatcher frequencyMatcher = new FrequencyMatcher(generator, 44100); // exactly 4 times higher polling
                                                                                    // rate

        player.setSoundStream(frequencyMatcher);

        (new Thread(() -> {
            try {
                player.play();
            } catch (PlaybackError e) {
            }
        })).start();

        Thread.sleep(10000);

        player.pause();
    }

    static class DummyMusicReader implements MusicStream {
        float t = 0, period = 1 / getFrequency();

        @Override
        public float getFrequency() {
            return 11025;
        }

        @Override
        public Byte[] getNextSample() {
            t += period;
            return peekNextSample();
        }

        @Override
        public Byte[] peekNextSample() {
            float ang = (t % 1) * 2 * (float) Math.PI,
                    a = ang * 440, b = ang * 329, c = ang * 392;

            float sample = (float) (((Math.sin(a) + Math.sin(b) + Math.sin(c)) * 0.333) * Math.pow(2, Math.sin(t))
                    * 0.5);

            return MusicUtils.floatToSample(sample);
        }

    }

}
