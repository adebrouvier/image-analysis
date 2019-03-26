package ar.edu.itba.ati.image;

public abstract class Pixel {
    public abstract int getRed();

    public abstract int getGreen();

    public abstract int getBlue();

    public abstract void add(Pixel p);

    public abstract void subtract(Pixel p);

    public abstract void multiply(Double d);

    public abstract void dynamicRangeCompress(Double c);

    public abstract void gammaPower(Double c, Double gamma);
}
