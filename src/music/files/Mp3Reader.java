package src.music.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.*;

import src.music.streams.MusicStream;

public class Mp3Reader implements MusicFile {

    AudioInputStream din;
    MusicStream underlyingStream;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    @Override
    public MusicStream getMusicStream() {
        return underlyingStream;
    }

    @Override
    public void openFile(String fileName) throws IOException, UnsupportedAudioFileException {
        File file = new File(fileName);
        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioInputStream din;
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);

        underlyingStream = new MusicStream() {

            short nextSample;

            @Override
            public float getFrequency() {
                return baseFormat.getSampleRate();
            }

            @Override
            public short getNextSample() {
                short tmp = nextSample;
                nextSample = read();
                return tmp;
            }

            @Override
            public short peekNextSample() {
                return nextSample;
            }

            @Override
            public short[] getNextBlock(int requestedLength) {
                return readN(requestedLength);
            }

            @Override
            public void addOnStreamDoneListener(OnStreamDoneListener listener) {
                doneListeners.add(listener);
            }

            private void notifyDoneListeners() {
                for (OnStreamDoneListener onStreamDoneListener : doneListeners) {
                    onStreamDoneListener.OnStreamDone(this);
                }
            }

            short read() {
                try {
                    int val = din.read();
                    if (val == -1) {
                        notifyDoneListeners();
                    }
                    return (short) (val);
                } catch (IOException e) {
                    return 0;
                }
            }

            short[] readN(int len) {
                short[] block = new short[len];

                block[0] = nextSample;
                for (int i = 1; i < len - 1; i++) {
                    block[i] = read();
                }

                nextSample = read();

                return block;
            }

        };

    }

    @Override
    public void closeFile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeFile'");
    }

}
