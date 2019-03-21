package ar.edu.itba.ati.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Image {

    public enum ImageType {
        GRAYSCALE,
        RGB,
    }

    private int width;
    private int height;
    private List<Pixel> pixels;

    public Image(int width, int height, List<Pixel> pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public Image(BufferedImage bufferedImage, ImageType imageType) {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.pixels = new ArrayList<Pixel>(this.width * this.height);
        for (int i = 0; i < this.width; i++){
            for (int j = 0; j < this.height; j++) {
                Color color = new Color(bufferedImage.getRGB(i, j));
                Pixel pixel;
                if (imageType.equals(ImageType.GRAYSCALE)) {
                    pixel = new GrayScalePixel(color.getBlue());
                } else {
                    pixel = new RGBPixel(color.getRed(), color.getGreen(), color.getBlue());
                }
                pixels.add(pixel);
            }
        }
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

    public Pixel getPixel(int x, int y) {
        return this.pixels.get(this.getIndex(x, y));
    }

    public void changePixel(int x, int y, Pixel pixel) {
        int index = this.getIndex(x, y);
        this.pixels.remove(index);
        this.pixels.add(index, pixel);
    }

    private int getIndex(int x, int y) {
        return x + y * height;
    }

    public Image getSubimage(int x1, int y1, int x2, int y2) {
        int width = Math.abs(x1 - x2);
        int height = Math.abs(y1 - y2);
        List<Pixel> pixels = new ArrayList<>(width * height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j >= this.height) {
                    break;
                }
                int currentIndex = this.getIndex(x1 + i, y1 + j);
                pixels.add(this.pixels.get(currentIndex));
            }
            if (i >= this.width) {
                break;
            }
        }
        return new Image(width, height, pixels);
    }

    public void pasteImage(int x, int y, Image image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (j >= this.height) {
                    break;
                }
                int thisIndex = this.getIndex(x + i, y + j);
                int imageIndex = this.getIndex(i, j);
                this.pixels.remove(thisIndex);
                this.pixels.add(thisIndex, this.pixels.get(imageIndex));
            }
            if (i >= this.width) {
                break;
            }
        }
    }
}
