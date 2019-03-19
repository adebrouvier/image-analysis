package ar.edu.itba.ati.image;

public class GrayScalePixel extends Pixel {

    private int grayScale;

    public GrayScalePixel(int grayscale) {
        this.grayScale = grayscale;
    }

    public int getGrayScale() {
        return grayScale;
    }

    @Override
    public int getRed() {
        return grayScale;
    }

    @Override
    public int getGreen() {
        return grayScale;
    }

    @Override
    public int getBlue() {
        return grayScale;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gray Scale: ")
                .append(this.grayScale);
        return sb.toString();
    }
}
