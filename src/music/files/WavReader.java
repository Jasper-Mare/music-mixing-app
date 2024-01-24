package src.music.files;

import java.io.*;
import java.nio.*;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import src.music.MusicUtils;
import src.music.streams.MusicStream;
import src.util.Func;

public class WavReader implements MusicFile {

    MusicStream underlyingStream;

    @Override
    public MusicStream getMusicStream() {
        return underlyingStream;
    }

    @Override
    public void openFile(String fileName) throws IOException {

        AudioInputStream inStream;
        final int bytesPerFrame;

        try {
            File file = new File(fileName);
            inStream = AudioSystem.getAudioInputStream(file);

            int frameSize = inStream.getFormat().getFrameSize();
            if (frameSize == AudioSystem.NOT_SPECIFIED) {
                // some audio formats may have unspecified frame size
                // in that case we may read any amount of bytes
                bytesPerFrame = 1;
            } else {
                bytesPerFrame = frameSize;
            }
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }

        underlyingStream = new MusicStream() {

            short nextSample;
            float freq = inStream.getFormat().getSampleRate();
            int channels = inStream.getFormat().getChannels();
            boolean isBigEndian = inStream.getFormat().isBigEndian();

            ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

            @Override
            public float getFrequency() {
                return freq;
            }

            @Override
            public short getNextSample() {
                short tmp = nextSample;

                nextSample = getNextBlock(1)[0];

                return tmp;
            }

            @Override
            public short peekNextSample() {
                return nextSample;
            }

            @Override
            public short[] getNextBlock(int requestedLength) {
                boolean fileDone = false;
                int numBytes = (requestedLength + 1) * bytesPerFrame;
                byte[] audioBytes = new byte[numBytes];
                try {

                    // Try to read numBytes bytes from the file.
                    fileDone = (inStream.read(audioBytes) == -1);

                    ByteBuffer buffer = ByteBuffer.wrap(audioBytes);

                    if (isBigEndian) {
                        buffer = buffer.order(ByteOrder.BIG_ENDIAN);
                    } else {
                        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
                    }

                    Func<ByteBuffer, Float> converter = getConverter();

                    short[] processedFrames = new short[requestedLength];
                    for (int frameNum = 0; frameNum < requestedLength; frameNum++) {
                        processedFrames[frameNum] = MusicUtils
                                .floatToSample(converter.Run(buffer));
                    }

                    nextSample = MusicUtils.floatToSample(converter.Run(buffer));

                    if (fileDone) {
                        for (OnStreamDoneListener listener : doneListeners) {
                            listener.OnStreamDone(underlyingStream);
                        }
                    }

                    // System.out.println(blocksRead + " done: " + fileDone);

                    return processedFrames;

                } catch (IOException ex) {
                    ex.printStackTrace();
                    return new short[requestedLength];
                }
            }

            public src.util.Func<ByteBuffer, Float> getConverter() {
                switch (bytesPerFrame) {
                    case 2:
                        return (buffer) -> {
                            return (float) buffer.getShort() / Short.MAX_VALUE;
                        };

                    case 4:
                        return (buffer) -> {
                            return (float) buffer.getInt() / Integer.MAX_VALUE;
                        };

                    case 8:
                        return (buffer) -> {
                            return (float) buffer.getLong() / Long.MAX_VALUE;
                        };

                    default:
                        return (buffer) -> {
                            return (float) buffer.get() / Byte.MAX_VALUE;
                        };
                }
            }

            @Override
            public void addOnStreamDoneListener(OnStreamDoneListener listener) {
                doneListeners.add(listener);
            }

        };

    }

}
// https://stackoverflow.com/a/6400178 - Java - reading, manipulating and
// writing WAV files
// https:// stackoverflow.com/a/7619111 - Convert a byte array to integer in
// Java and vice versa