package src.music.files;

import java.io.*;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import src.music.MusicUtils;
import src.music.streams.MusicStream;

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
            long totalFramesRead = 0;

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
                    int numBytesRead = 0;
                    int numFramesRead = 0;

                    // Try to read numBytes bytes from the file.
                    fileDone = ((numBytesRead = inStream.read(audioBytes)) == -1);

                    // Calculate the number of frames actually read.
                    numFramesRead = numBytesRead / bytesPerFrame;
                    totalFramesRead += numFramesRead;
                } catch (IOException ex) {
                    return new short[requestedLength];
                }

                float maxVal = Math.scalb(1, (bytesPerFrame * 8) - 1) - 1;
                short[] processedFrames = new short[requestedLength];
                for (int frameNum = 0; frameNum < requestedLength; frameNum++) {
                    processedFrames[frameNum] = MusicUtils.floatToSample(processFrame(audioBytes, frameNum, maxVal));
                }

                nextSample = MusicUtils.floatToSample(processFrame(audioBytes, requestedLength, maxVal));

                if (fileDone) {
                    for (OnStreamDoneListener listener : doneListeners) {
                        listener.OnStreamDone(underlyingStream);
                    }
                }

                System.out.println(totalFramesRead / requestedLength);

                return processedFrames;
            }

            float processFrame(byte[] audioBytes, int frameNum, float maxVal) {
                float val = 0; // TODO: a problem will probably come from here
                for (int byteNum = 0; byteNum < bytesPerFrame; byteNum++) {
                    val += byteNum * 8 * audioBytes[(frameNum * bytesPerFrame) + byteNum];
                }

                val /= maxVal;

                return val;
            }

            @Override
            public void addOnStreamDoneListener(OnStreamDoneListener listener) {
                doneListeners.add(listener);
            }

        };

    }

}
// https://stackoverflow.com/a/6400178