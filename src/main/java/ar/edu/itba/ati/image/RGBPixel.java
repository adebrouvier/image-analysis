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
