package ar.edu.itba.ati.image;

public class RGBPixel extends Pixel {

    private int red;
    private int green;
    private int blue;

    public RGBPixel(int red, int green, int blue) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public Pixel add(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue) {
        int diffRed = maxRed - minRed;
        int diffGreen = maxGreen - minGreen;
        int diffBlue = maxBlue - minBlue;

        int red = (255 * (this.red + p.getRed() - minRed) / diffRed);
        int green = (255 * (this.green + p.getGreen() - minGreen) / diffGreen);
        int blue = (255 * (this.blue + p.getBlue() - minBlue) / diffBlue);

        return new RGBPixel(red, green, blue);
    }

    @Override
    public void add(Pixel p) {
        this.blue += p.getBlue();
        this.green += p.getGreen();
        this.red += p.getRed();
    }

    @Override
    public Pixel subtract(Pixel p, int maxRed, int minRed, int maxGreen, int minGreen, int maxBlue, int minBlue) {
        int diffRed = maxRed - minRed;
        int diffGreen = maxGreen - minGreen;
        int diffBlue = maxBlue - minBlue;

        int red = (255 * (this.red - p.getRed() - minRed) / diffRed);
        int green = (255 * (this.green - p.getGreen() - minGreen) / diffGreen);
        int blue = (255 * (this.blue - p.getBlue() - minBlue) / diffBlue);

        return new RGBPixel(red, green, blue);
    }

    @Override
    public void subtract(Pixel p) {
        this.blue -= p.getBlue();
        this.green -= p.getGreen();
        this.red -= p.getRed();
    }

    @Override
    public void multiply(Double m) {
        this.red = (int) (m * this.red);
        this.blue = (int) (m * this.blue);
        this.green = (int) (m * this.green);
    }

    public void multiply(Pixel pixel) {
        this.red *= pixel.getRed();
        this.green *= pixel.getGreen();
        this.blue *= pixel.getBlue();
    }

    @Override
    public void dynamicRangeCompress(Double cRed, Double cGreen, Double cBlue) {
        this.red = (int) (cRed * Math.log(1 + this.red));
        this.blue = (int) (cBlue * Math.log(1 + this.blue));
        this.green = (int) (cGreen * Math.log(1 + this.green));
    }

    @Override
    public void gammaPower(Double c, Double gamma) {
        this.red = (int) (c * Math.pow(this.red, gamma));
        this.blue = (int) (c * Math.pow(this.blue, gamma));
        this.green = (int) (c * Math.pow(this.green, gamma));
    }

    @Override
    public void threshold(Double threshold) {
        this.red = this.red < threshold ? 0 : 255;
        this.blue = this.blue < threshold ? 0 : 255;
        this.green = this.green < threshold ? 0 : 255;
    }

    public void RGBThreshold(double red, double green, double blue){
        this.red = this.red < red ? 0 : 255;
        this.green = this.green < green ? 0 : 255;
        this.blue = this.blue < blue ? 0 : 255;
    }

    @Override
    public void turnBlack() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }

    @Override
    public void turnWhite() {
        this.red = 255;
        this.green = 255;
        this.blue = 255;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Red: ")
                .append(this.red)
                .append("; Green: ")
                .append(this.green)
                .append("; Blue: ")
                .append(this.blue);
        return sb.toString();
    }
}
