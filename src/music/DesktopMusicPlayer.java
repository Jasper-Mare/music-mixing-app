package src.music;

import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class DesktopMusicPlayer implements MusicPlayer {

    MusicStream stream;
    boolean playing;

    LinkedList<short[]> blockBuffer = new LinkedList<short[]>();
    byte[] nextBlockToWrite;
    boolean blockConsumed;
    private static final int blockSize = 8192; // load up 16KiB of sound (each sample is 2b)

    @Override
    public void play() throws PlaybackError {

        try {
            nextBlockToWrite = new byte[2 * blockSize];
            blockConsumed = true; // so block gets refilled
            float frequency = stream.getFrequency();

            (new Thread(() -> {
                bufferBuilder();
            })).start();

            playing = true;

            SourceDataLine sourceDataLine;
            AudioFormat audioFormat = new AudioFormat(frequency, 16, 1, true, false);
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

            sourceDataLine.open(audioFormat, blockSize * 2);
            sourceDataLine.start();

            while (playing) {
                synchronized (nextBlockToWrite) {
                    if (!blockConsumed) {
                        sourceDataLine.write(nextBlockToWrite, 0, blockSize * 2);
                        blockConsumed = true;
                    }
                }
            }

            // sourceDataLine.drain();
            sourceDataLine.stop();
        } catch (Exception e) {
            throw new PlaybackError(e.getMessage());
        }
    }

    private void bufferBuilder() {
        while (playing) {
            blockBuffer.addLast(stream.getNextBlock(blockSize));

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
