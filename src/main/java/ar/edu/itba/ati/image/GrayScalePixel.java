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
    public void add(Pixel p) {
        GrayScalePixel gp = (GrayScalePixel) p;
        this.grayScale = Math.min(255, this.grayScale + gp.getGrayScale());
    }

    @Override
    public void subtract(Pixel p) {
        GrayScalePixel gp = (GrayScalePixel) p;
        this.grayScale = Math.max(0, this.grayScale - gp.getGrayScale());
    }

    @Override
    public void multiply(Double d) {
        this.grayScale = (int) Math.min(255, Math.max(0, this.grayScale * d));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gray Scale: ")
                .append(this.grayScale);
        return sb.toString();
    }

    @Override
    public void dynamicRangeCompress(Double c) {
        this.grayScale = (int) (c * Math.log(1 + this.grayScale));
    }

    @Override
    public void gammaPower(Double c, Double gamma) {
        this.grayScale = (int) (c * Math.pow(this.grayScale, gamma));
    }


}
