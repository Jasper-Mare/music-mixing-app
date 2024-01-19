package src.music.files;

import java.io.IOException;
import java.util.ArrayList;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import src.music.streams.MusicStream;

public class Mp3Reader implements MusicFile {

    MusicStream underlyingStream;

    ArrayList<MusicStream.OnStreamDoneListener> doneListeners = new ArrayList<>();

    @Override
    public MusicStream getMusicStream() {
        return underlyingStream;
    }

    @Override
    public void openFile(String fileName) throws IOException {

        try {
            String tmpOutput = MusicTempDir.getTempMusicFile().toString();

            Converter conv = new Converter();
            conv.convert(fileName, tmpOutput);

            WavReader wavReader = new WavReader();
            wavReader.openFile(tmpOutput);

            underlyingStream = wavReader.getMusicStream();
        } catch (JavaLayerException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }

    }

}
