package ar.edu.itba.ati.image;

import static ar.edu.itba.ati.image.Constants.BLACK;
import static ar.edu.itba.ati.image.Constants.WHITE;

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
    public Pixel add(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue) {
        int diffGray = maxRed - minRed;
        if (diffGray == 0) {
            return new GrayScalePixel(maxRed);
        }
        int grayScale = (255 * (this.grayScale + p.getRed() - minRed) / diffGray);
        return new GrayScalePixel(grayScale);
    }

    @Override
    public void add(Pixel p) {
        this.grayScale += p.getRed();
    }

    @Override
    public Pixel subtract(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue) {
        int diffGray = maxRed - minRed;
        if (diffGray == 0) {
            return new GrayScalePixel(maxRed);
        }
        int grayScale = (255 * (this.grayScale - p.getRed() - minRed) / diffGray);
        return new GrayScalePixel(grayScale);
    }

    @Override
    public void multiply(Double m) {
        this.grayScale = (int) (m * this.grayScale);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gray Scale: ")
                .append(this.grayScale);
        return sb.toString();
    }

    @Override
    public void dynamicRangeCompress(Double c, Double maxGreen, Double maxBlue) {
        this.grayScale = (int) (c * Math.log(1 + this.grayScale));
    }

    @Override
    public void gammaPower(Double c, Double gamma) {
        this.grayScale = (int) (c * Math.pow(this.grayScale, gamma));
    }

    @Override
    public void threshold(Double threshold) {
        this.grayScale = this.grayScale < threshold ? BLACK : WHITE;
    }

    @Override
    public void turnBlack() {
        this.grayScale = BLACK;
    }

    @Override
    public void turnWhite() {
        this.grayScale = WHITE;
    }

}
