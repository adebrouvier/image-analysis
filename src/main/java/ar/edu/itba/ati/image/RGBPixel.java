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
    public void add(Pixel p) {
        this.red = Math.min(255, this.red + p.getRed());
        this.green = Math.min(255, this.green + p.getGreen());
        this.blue = Math.min(255, this.blue + p.getBlue());
    }

    @Override
    public void subtract(Pixel p) {
        this.red = Math.max(0, this.red - p.getRed());
        this.green = Math.max(0, this.green - p.getGreen());
        this.blue = Math.max(0, this.blue - p.getBlue());
    }

    @Override
    public void multiply(Double d) {
        this.red = (int) Math.min(255, Math.max(0, this.red * d));
        this.blue = (int) Math.min(255, Math.max(0, this.blue * d));
        this.green = (int) Math.min(255, Math.max(0, this.green * d));
    }

    @Override
    public void dynamicRangeCompress(Double c) {
        this.red = (int) (c * Math.log(1 + this.red));
        this.blue = (int) (c * Math.log(1 + this.blue));
        this.green = (int) (c * Math.log(1 + this.green));
    }

    @Override
    public void gammaPower(Double c, Double gamma) {
        this.red = (int) (c * Math.pow(this.red, gamma));
        this.blue = (int) (c * Math.pow(this.blue, gamma));
        this.green = (int) (c * Math.pow(this.green, gamma));
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
