package src.util;

public class MoreMaths {
    public static float lerp(float a, float b, float p) {
        return (1 - p) * a + p * b;
    }

    public static double lerp(double a, double b, double p) {
        return (1 - p) * a + p * b;
    }

    public static float clamp(float min, float val, float max) {
        return Math.min(max, Math.max(val, min));
    }
}
