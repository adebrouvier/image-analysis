package ar.edu.itba.ati;

import java.util.List;

public class Image {

    private int width;
    private int height;
    private List<Pixel> pixels;

    public Image(int width, int height, List<Pixel> pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Pixel> getPixels() {
        return pixels;
    }
}
