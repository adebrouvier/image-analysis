package ar.edu.itba.ati.image;

import ar.edu.itba.ati.random.ExponentialGenerator;
import ar.edu.itba.ati.random.GaussianGenerator;
import ar.edu.itba.ati.random.RandomGenerator;
import ar.edu.itba.ati.random.RayleighGenerator;

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

    public void threshold(Double threshold) {
        for (Pixel p: this.pixels) {
            p.threshold(threshold);
        }
    }

    public void saltAndPepperNoise(Double p0, Double p1) {
        Random r = new Random();
        for (Pixel p: this.pixels) {
            Double p2 = r.nextDouble();
            if (p2 <= p0) {
                p.turnBlack();
            } else if (p2 >= p1) {
                p.turnWhite();
            }
        }
    }

    public int getGrayScaleMean() {
        int total = 0;
        for (Pixel p : pixels){
            total += ((GrayScalePixel) p).getGrayScale();
        }
        return total/pixels.size();
    }

    public int getGrayScaleStDev() {
        int mean = getGrayScaleMean();
        int acum = 0;

        for (Pixel p : pixels){
            acum += Math.pow(((GrayScalePixel) p).getGrayScale() - mean, 2);
        }

        return (int) Math.sqrt((1.0/pixels.size())*acum);
    }

    public void increaseContrast() {
        int mean = getGrayScaleMean();
        int stDev = getGrayScaleStDev();
        int r1 = mean - stDev;
        int r2 = mean + stDev;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                GrayScalePixel p = (GrayScalePixel) getPixel(x, y);
                int newValue = p.getGrayScale();
                if (p.getGrayScale() <= r1){
                    newValue = f1(newValue, r1);
                }
                if (p.getGrayScale() >= r2){
                    newValue = f2(newValue, r2);
                }
                changePixel(x, y, new GrayScalePixel(newValue));
            }
        }
    }

    private final static double decreaseFactor = 0.25;
    private final static double increaseFactor = 1 + decreaseFactor;

    private int f1(int x, int r){
        return slope(x, 0, 0, r, r*decreaseFactor);
    }

    private int f2(int x, int r){
        return slope(x, 255, 255, r, r*increaseFactor);
    }

    private int slope(int x, double x1, double y1, double x2, double y2){
        return (int) ((x - x1)*(y2 - y1)/(x2 - x1) + y1);
    }

    public void addExponentialNoise(double percentage, double lambda) {
        multiplyNoise(percentage, new ExponentialGenerator(lambda));
    }

    public void addRayleighNoise(double percentage, double phi) {
        multiplyNoise(percentage, new RayleighGenerator(phi));
    }

    public void addGaussianNoise(double percentage, double mean, double stDev) {
        GaussianGenerator generator = new GaussianGenerator(stDev, mean);
        for (Pixel p : pixels){
            if (Math.random() < percentage){
                GrayScalePixel pixel = (GrayScalePixel) p;
                double noise = generator.getDouble();
                //pixel.add(noise);
            }
        }
    }

    private void multiplyNoise(double percentage, RandomGenerator generator) {
        for (Pixel p : pixels){
            if (Math.random() < percentage){
                GrayScalePixel pixel = (GrayScalePixel) p;
                double noise = generator.getDouble();
               //pixel.multiply(noise);
            }
        }
    }
}
