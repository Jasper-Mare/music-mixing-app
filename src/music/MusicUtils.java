package src.music;

import src.util.MoreMaths;

public class MusicUtils {
    public static float sampleToFloat(Byte[] buffer) {
        // Combine the two bytes into a 16-bit short
        short soundSample = (short) ((buffer[1] << 8) | (buffer[0] & 0xFF));

        // Normalize the short value to the range [-1, 1]
        float value = soundSample / 32767.0f;

        return value;
    }

    public static Byte[] floatToSample(float value) {
        value = MoreMaths.clamp(-1, value, 1); // Math.min(1, Math.max(value, -1));

        short soundSample = (short) (value * 32767);

        Byte[] buffer = new Byte[2];
        buffer[0] = (byte) (soundSample & 0xFF); // write 8bits ________WWWWWWWW out of 16
        buffer[1] = (byte) (soundSample >> 8); // write 8bits WWWWWWWW________ out of 16

        return buffer;
    }
}
