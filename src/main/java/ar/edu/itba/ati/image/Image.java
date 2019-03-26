package ar.edu.itba.ati.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Image {

    public enum ImageType {
        GRAY_SCALE,
        RGB,
    }

    public enum Format {
        RAW,
        PBM,
        PGM,
        PPM
    }

    private int width;
    private int height;
    private List<Pixel> pixels;
    private Format format;

    public Image(int width, int height, List<Pixel> pixels, Format format) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.format = format;
    }

    public Image(BufferedImage bufferedImage, ImageType imageType) {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.pixels = new ArrayList<>(this.width * this.height);
        for (int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++) {
                Color color = new Color(bufferedImage.getRGB(j, i));
                Pixel pixel;
                if (imageType.equals(ImageType.GRAY_SCALE)) {
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
        this.pixels.set(index, pixel);
    }

    private int getIndex(int x, int y) {
        return x + (y * width);
    }

    public Image getSubimage(int x1, int y1, int x2, int y2) {
        int smallestX = Math.min(x1, x2);
        int smallestY = Math.min(y1, y2);
        int width, height;

        if (x1 > this.width || x2 > this.width) {
            width = this.width - smallestX;
        } else {
            width = Math.abs(x1 - x2);
        }

        if (y1 > this.height || y2 > this.height) {
            height = this.height - smallestY;
        } else {
            height = Math.abs(y1 - y2);
        }

        List<Pixel> pixels = new ArrayList<>(width * height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int currentIndex = this.getIndex(smallestX + j, smallestY + i);
                pixels.add(this.pixels.get(currentIndex));
            }
        }
        return new Image(width, height, pixels, format);
    }

    public void pasteImage(int x, int y, Image image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (j >= this.width) {
                    break;
                }
                int thisIndex = this.getIndex(x + j, y + i);
                int imageIndex = this.getIndex(j, i);
                this.pixels.remove(thisIndex);
                this.pixels.add(thisIndex, this.pixels.get(imageIndex));
            }
            if (i >= this.height) {
                break;
            }
        }
    }

    public Format getFormat() {
        return format;
    }

    public ImageType getType() {
        return pixels.get(0) instanceof GrayScalePixel ? ImageType.GRAY_SCALE : ImageType.RGB;
    }

    public void add(Image image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (j >= this.width) {
                    break;
                }
                int imageIndex = this.getIndex(j, i);
                Pixel myPixel = this.pixels.get(imageIndex);
                Pixel otherPixel = image.getPixel(j, i);
                myPixel.add(otherPixel);
            }
            if (i >= this.height) {
                break;
            }
        }
    }

    public void substract(Image image) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (j >= this.width) {
                    break;
                }
                int imageIndex = this.getIndex(j, i);
                Pixel myPixel = this.pixels.get(imageIndex);
                Pixel otherPixel = image.getPixel(j, i);
                myPixel.subtract(otherPixel);
            }
            if (i >= this.height) {
                break;
            }
        }
    }

    public void multiply(Double d) {
        for (Pixel p: this.pixels) {
            p.multiply(d);
        }
    }

    public void dynamicRangeCompress(Double threshold){
        double c = 255.0 / Math.log(1 + threshold);
        for (Pixel p: this.pixels) {
            p.dynamicRangeCompress(c);
        }
    }

    public void gammaPower(Double gamma){
        double c = Math.pow(255.0, 1 - gamma);
        for (Pixel p: this.pixels) {
            p.gammaPower(c, gamma);
        }
    }
}
