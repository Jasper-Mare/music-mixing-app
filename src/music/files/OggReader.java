package src.music.files;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.spi.vorbis.sampled.convert.VorbisFormatConversionProvider;
import src.music.streams.MusicStream;

public class OggReader implements MusicFile {

    MusicStream underlyingStream;

    double duration;

    @Override
    public MusicStream getMusicStream() {
        return underlyingStream;
    }

    @Override
    public void openFile(String fileName) throws IOException {
        try {
            String tmpOutput = MusicTempDir.getTempMusicFile().toString();

            // TODO: not working

            AudioSystem.getAudioInputStream(AudioFormat, AudioInputStream) 
            Converter conv = new Converter();
            conv.convert(fileName, tmpOutput);

            WavReader wavReader = new WavReader();
            wavReader.openFile(tmpOutput);
            duration = wavReader.getDuration();

            underlyingStream = wavReader.getMusicStream();

        } catch (JavaLayerException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public double getDuration() {
        return duration;
    }

}
