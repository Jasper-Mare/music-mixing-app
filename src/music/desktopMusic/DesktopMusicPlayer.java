package src.music.desktopMusic;

import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import src.music.MusicPlayer;
import src.music.MusicUtils;
import src.music.MusicPlayer.PlaybackError;
import src.music.streams.MusicStream;

public class DesktopMusicPlayer implements MusicPlayer {

    MusicStream stream;
    boolean stopped, paused;

    LinkedList<short[]> blockBuffer = new LinkedList<short[]>();
    byte[] nextBlockToWrite;
    boolean blockConsumed;
    private final int blockSize = 8192; // load up 16KiB of sound (each sample is 2b)

    @Override
    public void start() throws PlaybackError {
        try {
            blockBuffer = new LinkedList<short[]>();
            nextBlockToWrite = new byte[2 * blockSize];
            blockConsumed = true; // so block gets refilled
            float frequency = stream.getFrequency();

            (new Thread(() -> {
                bufferBuilder();
            })).start();

            stopped = false;
            paused = false;

            SourceDataLine sourceDataLine;
            AudioFormat audioFormat = new AudioFormat(frequency, 16, 1, true, false);
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

            sourceDataLine.open(audioFormat, blockSize * 2);
            sourceDataLine.start();

            // run playback on seperate thread
            (new Thread(() -> {
                boolean lastPaused = false; // if it was paused on the last check

                while (!stopped) {
                    if (lastPaused != paused) {
                        if (paused) {
                            sourceDataLine.stop();
                        } else {
                            sourceDataLine.start();
                        }
                    }

                    synchronized (nextBlockToWrite) {
                        if (!blockConsumed) {
                            sourceDataLine.write(nextBlockToWrite, 0, blockSize * 2);
                            blockConsumed = true;
                        }
                    }

                    lastPaused = paused;
                }

                // sourceDataLine.drain();
                sourceDataLine.stop();
            })).start();

        } catch (Exception e) {
            throw new PlaybackError(e.getMessage());
        }
    }

    private void bufferBuilder() {
        while (!stopped) {
            if (blockBuffer.size() < 3) {
                blockBuffer.addLast(stream.getNextBlock(blockSize)); // add blocks untill 3 are queued
            }

            synchronized (nextBlockToWrite) {
                if (blockConsumed && !blockBuffer.isEmpty()) {
                    short[] shortBlock = blockBuffer.removeFirst();
                    for (int i = 0; i < blockSize; i++) {
                        byte[] tmp = MusicUtils.sampleToBuffer(shortBlock[i]);
                        nextBlockToWrite[i * 2] = tmp[0];
                        nextBlockToWrite[i * 2 + 1] = tmp[1];
                    }
                    blockConsumed = false;
                }
            }
        }
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        if (!stopped) {
            paused = false;
        }
    }

    @Override
    public void stop() {
        stopped = true;
        paused = false;
    }

    @Override
    public MusicStream getSoundStream() {
        return stream;
    }

    @Override
    public void setSoundStream(MusicStream stream) throws PlaybackError {
        stop();
        this.stream = stream;
        start();

    }

}
