package src.music;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MakeSound {
    public static void main(String[] args) throws LineUnavailableException {
        System.out.println("Make sound");

        byte[] buffer = new byte[2];
        int frequency = 44100; // 44100 sample points per 1 second

        AudioFormat audioFormat = new AudioFormat((float) frequency, 16, 1, true, false);
        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

        sourceDataLine.open();
        sourceDataLine.start();

        int durationMs = 5000;
        int sinFrequency = 441; // number of times in 1sec sin function repeats
        float numberOfSamplesToRepresentFullSin = (float) frequency / sinFrequency;

        for (int i = 0; i < durationMs * (float) 44100 / 1000; i++) { // 1000 ms in 1 second
            // divide with 2 since sin goes 0PI to 2PI
            double angle = i / (numberOfSamplesToRepresentFullSin / 2.0) * Math.PI;

            // 32767 - max value for sample to take (-32767 to 32767)
            short soundSample = (short) (Math.sin(angle) * 32767);
            buffer[0] = (byte) (soundSample & 0xFF); // write 8bits ________WWWWWWWW out of 16
            buffer[1] = (byte) (soundSample >> 8); // write 8bits WWWWWWWW________ out of 16

            sourceDataLine.write(buffer, 0, 2);
        }

        System.out.println("done making data");

        sourceDataLine.drain();

        System.out.println("drained");

        sourceDataLine.stop();

        System.out.println("stopped");
    }
}