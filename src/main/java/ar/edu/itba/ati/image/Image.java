package ar.edu.itba.ati.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private ImageType type;

    public Image(int width, int height, List<Pixel> pixels, Format format) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.format = format;
        if (this.format.equals(Format.PPM)){
            this.type = ImageType.RGB;
        } else {
            this.type = ImageType.GRAY_SCALE;
        }
    }

    public Image(BufferedImage bufferedImage, ImageType imageType) {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.pixels = new ArrayList<>(this.width * this.height);
        this.type = imageType;
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
        for (int i = 0; i < image.getHeight() || i < this.height; i++) {
            for (int j = 0; j < image.getWidth() || j < this.width; j++) {
                int thisIndex = this.getIndex(x + j, y + i);
                int imageIndex = this.getIndex(j, i);
                this.pixels.remove(thisIndex);
                this.pixels.add(thisIndex, this.pixels.get(imageIndex));
            }
        }
    }

    public Format getFormat() {
        return format;
    }

    public ImageType getType() {
        return pixels.get(0) instanceof GrayScalePixel ? ImageType.GRAY_SCALE : ImageType.RGB;
    }

    public Image add(Image image) {
        int maxRed = 0, maxGreen = 0, maxBlue = 0, minRed = 255, minGreen = 255, minBlue = 255;
        Image newImage = this.copy();

        for (int i = 0; i < image.getHeight() || i < this.height; i++) {
            for (int j = 0; j < image.getWidth() || j < this.width; j++) {
                Pixel myPixel = this.getPixel(j, i);
                Pixel otherPixel = image.getPixel(j, i);
                maxRed = Math.max(maxRed, myPixel.getRed() + otherPixel.getRed());
                maxBlue = Math.max(maxBlue, myPixel.getBlue() + otherPixel.getBlue());
                maxGreen = Math.max(maxGreen, myPixel.getGreen() + otherPixel.getGreen());
                minRed = Math.min(minRed, myPixel.getRed() + otherPixel.getRed());
                minBlue = Math.min(minBlue, myPixel.getBlue() + otherPixel.getBlue());
                minGreen = Math.min(minGreen, myPixel.getGreen() + otherPixel.getGreen());
            }
        }

        for (int i = 0; i < image.getHeight() || i < this.height; i++) {
            for (int j = 0; j < image.getWidth() || j < this.width; j++) {
                Pixel myPixel = this.getPixel(j, i);
                Pixel otherPixel = image.getPixel(j, i);

                newImage.changePixel(j, i, myPixel.add(otherPixel, maxRed, minRed, maxGreen, minGreen, maxBlue, minBlue));
            }
        }
        return newImage;
    }

    public Image substract(Image image) {
        int maxRed = 0, maxGreen = 0, maxBlue = 0, minRed = 255, minGreen = 255, minBlue = 255;
        Image newImage = this.copy();

        for (int i = 0; i < image.getHeight() || i < this.height; i++) {
            for (int j = 0; j < image.getWidth() || j < this.width; j++) {
                Pixel myPixel = this.getPixel(j, i);
                Pixel otherPixel = image.getPixel(j, i);
                maxRed = Math.max(maxRed, myPixel.getRed() - otherPixel.getRed());
                maxBlue = Math.max(maxBlue, myPixel.getBlue() - otherPixel.getBlue());
                maxGreen = Math.max(maxGreen, myPixel.getGreen() - otherPixel.getGreen());
                minRed = Math.min(minRed, myPixel.getRed() - otherPixel.getRed());
                minBlue = Math.min(minBlue, myPixel.getBlue() - otherPixel.getBlue());
                minGreen = Math.min(minGreen, myPixel.getGreen() - otherPixel.getGreen());
            }
        }

        for (int i = 0; i < image.getHeight() || i < this.height; i++) {
            for (int j = 0; j < image.getWidth() || j < this.width; j++) {
                Pixel myPixel = this.getPixel(j, i);
                Pixel otherPixel = image.getPixel(j, i);

                newImage.changePixel(j, i, myPixel.subtract(otherPixel, maxRed, minRed, maxGreen, minGreen, maxBlue, minBlue));
            }
        }
        return newImage;
    }

    public Image multiply(Double d) {
        Double maxRed = 0.0, maxGreen = 0.0, maxBlue = 0.0;
        Image newImage = this.copy();
        for (Pixel p: newImage.pixels) {
            maxRed = Math.max(maxRed, p.getRed() * d);
            maxBlue = Math.max(maxBlue, p.getBlue() * d);
            maxGreen = Math.max(maxGreen, p.getGreen() * d);
        }

        double cRed = 255.0 / Math.log(1 + maxRed);
        double cBlue = 255.0 / Math.log(1 + maxBlue);
        double cGreen = 255.0 / Math.log(1 + maxGreen);


        for (Pixel p: this.pixels) {
            p.multiply(d, cRed, cGreen, cBlue);
        }
        return newImage;
    }

    public Image dynamicRangeCompress(Double threshold){
        double c = 255.0 / Math.log(1 + threshold);
        Image newImage = this.copy();
        for (Pixel p: newImage.pixels) {
            p.dynamicRangeCompress(c);
        }
        return newImage;
    }

    public Image gammaPower(Double gamma){
        double c = Math.pow(255.0, 1 - gamma);
        Image newImage = this.copy();
        for (Pixel p: newImage.pixels) {
            p.gammaPower(c, gamma);
        }
        return newImage;
    }

    public Image threshold(Double threshold) {
        Image newImage = this.copy();
        for (Pixel p: newImage.pixels) {
            p.threshold(threshold);
        }
        return newImage;
    }

    public Image saltAndPepperNoise(Double p0, Double p1) {
        Random r = new Random();
        Image image = this.copy();
        for (Pixel p: image.pixels) {
            Double p2 = r.nextDouble();
            if (p2 <= p0) {
                p.turnBlack();
            } else if (p2 >= p1) {
                p.turnWhite();
            }
        }
        return image;
    }

    public Image copy() {
        return new Image(this.width, this.height, new ArrayList<>(this.pixels), this.format);
    }
}
