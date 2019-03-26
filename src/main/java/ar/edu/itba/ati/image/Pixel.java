package ar.edu.itba.ati.image;

public abstract class Pixel {
    public abstract int getRed();

    public abstract int getGreen();

    public abstract int getBlue();

    public abstract Pixel add(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue);

    public abstract Pixel subtract(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue);

    public abstract void multiply(Double d, Double cRed, Double cGreen, Double cBlue);

    public abstract void dynamicRangeCompress(Double c);

    public abstract void gammaPower(Double c, Double gamma);

    public abstract void threshold(Double threshold);

    public abstract void turnBlack();

    public abstract void turnWhite();
}
