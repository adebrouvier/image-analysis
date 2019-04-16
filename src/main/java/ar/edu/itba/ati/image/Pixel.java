package ar.edu.itba.ati.image;

public abstract class Pixel {
    public abstract int getRed();

    public abstract int getGreen();

    public abstract int getBlue();

    public abstract Pixel add(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue);

    public abstract void add(Pixel p);

    public abstract Pixel subtract(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue);

    public abstract void multiply(Double m);

    public abstract void dynamicRangeCompress(Double maxRed, Double maxGreen, Double maxBlue);

    public abstract void gammaPower(Double c, Double gamma);

    public abstract void threshold(Double threshold);

    public abstract void turnBlack();

    public abstract void turnWhite();
}
