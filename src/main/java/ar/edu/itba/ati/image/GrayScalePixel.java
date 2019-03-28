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
    public Pixel add(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue) {
        int diffGray = maxRed - minRed;
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
        int grayScale = (255 * (this.grayScale - p.getRed() - minRed) / diffGray);
        return new GrayScalePixel(grayScale);
    }

    @Override
    public void multiply(Double d, Double cRed, Double cGreen, Double cBlue) {
        this.grayScale = (int) (cRed * Math.log(1 + this.grayScale));
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
    public void dynamicRangeCompress(Double maxRed, Double maxGreen, Double maxBlue) {
        this.grayScale = (int) (maxRed * Math.log(1 + this.grayScale));
    }

    @Override
    public void gammaPower(Double c, Double gamma) {
        this.grayScale = (int) (c * Math.pow(this.grayScale, gamma));
    }

    @Override
    public void threshold(Double threshold) {
        this.grayScale = this.grayScale < threshold ? 0 : 255;
    }

    @Override
    public void turnBlack() {
        this.grayScale = 0;
    }

    @Override
    public void turnWhite() {
        this.grayScale = 255;
    }

}
