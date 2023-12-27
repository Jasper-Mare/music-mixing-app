package src.music;

import src.util.MoreMaths;

public class MusicUtils {
    public static float bufferToFloat(byte[] buffer) {
        return sampleToFloat(bufferToSample(buffer));
    }

    public static byte[] floatToBuffer(float value) {
        return sampleToBuffer(floatToSample(MoreMaths.clamp(-1, value, 1)));
    }

    public static float sampleToFloat(short sample) {
        return sample / 32767.0f;
    }

    public static short floatToSample(float value) {
        return (short) (value * 32767);
    }

    public static short bufferToSample(byte[] buffer) {
        // Combine the two bytes into a 16-bit short
        return (short) ((buffer[1] << 8) | (buffer[0] & 0xFF));
    }

    public static byte[] sampleToBuffer(short sample) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) (sample & 0xFF); // write 8bits ________WWWWWWWW out of 16
        buffer[1] = (byte) (sample >> 8); // write 8bits WWWWWWWW________ out of 16

        return buffer;
    }

}
