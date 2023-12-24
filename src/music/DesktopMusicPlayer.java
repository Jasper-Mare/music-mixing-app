package src.music;

import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class DesktopMusicPlayer implements MusicPlayer {

    MusicStream stream;
    boolean playing;

    LinkedList<byte[]> playbackBuffer = new LinkedList<byte[]>();

    @Override
    public void play() throws PlaybackError {

        float frequency = stream.getFrequency(); // 44100 sample points per 1 second
        playing = true;

        for (int i = 0; i < frequency * 5; i++) { // load up 5 seconds of sound
            Byte[] next = stream.getNextSample();
            byte[] newBytes = new byte[] { next[0], next[1] };
            playbackBuffer.addLast(newBytes);
        }

        (new Thread(() -> {
            bufferBuilder();
        })).start();

        SourceDataLine sourceDataLine;

        try {
            AudioFormat audioFormat = new AudioFormat(frequency, 16, 1, true, false);
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

            sourceDataLine.open();
        } catch (LineUnavailableException e) {
            throw new PlaybackError(e.getMessage());
        }

        sourceDataLine.start();

        for (int i = 0; i < frequency * 5; i++) { // load up 5 seconds of sound
            sourceDataLine.write(playbackBuffer.removeFirst(), 0, 2);
        }

        while (playing) {
            sourceDataLine.write(playbackBuffer.removeFirst(), 0, 2);
        }

        sourceDataLine.drain();
        sourceDataLine.stop();

    }

    private void bufferBuilder() {
        while (playing) {
            Byte[] next = stream.getNextSample();
            byte[] newBytes = new byte[] { next[0], next[1] };
            playbackBuffer.addLast(newBytes);
        }
    }

    @Override
    public void pause() {
        playing = false;
    }

    @Override
    public MusicStream getSoundStream() {
        return stream;
    }

    @Override
    public void setSoundStream(MusicStream stream) {
        this.stream = stream;
    }

}