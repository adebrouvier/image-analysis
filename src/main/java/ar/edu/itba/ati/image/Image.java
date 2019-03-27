package ar.edu.itba.ati.image;

import ar.edu.itba.ati.random.ExponentialGenerator;
import ar.edu.itba.ati.random.GaussianGenerator;
import ar.edu.itba.ati.random.RandomGenerator;
import ar.edu.itba.ati.random.RayleighGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
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

    public void increaseContrast(int r1, int r2, int s1, int s2) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                GrayScalePixel p = (GrayScalePixel) getPixel(x, y);
                int newValue = p.getGrayScale();
                if (p.getGrayScale() <= r1){
                    newValue = f1(newValue, r1, s1);
                }
                if (p.getGrayScale() >= r2){
                    newValue = f2(newValue, r2, s2);
                }
                changePixel(x, y, new GrayScalePixel(newValue));
            }
        }
    }

    private int f1(int x, int r, int s){
        return slope(x, 0, 0, r, s);
    }

    private int f2(int x, int r, int s){
        return slope(x, 255, 255, r, s);
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
                pixel.add(new GrayScalePixel((int) noise));
            }
        }
    }

    private void multiplyNoise(double percentage, RandomGenerator generator) {
        for (Pixel p : pixels){
            if (Math.random() < percentage){
                GrayScalePixel pixel = (GrayScalePixel) p;
                double noise = generator.getDouble();
                pixel.multiply(noise);
            }
        }
    }

    private Image applyMask(int maskSize, MaskApplier mask) {
        if (maskSize % 2 != 1){
            throw new IllegalArgumentException("Mask size must not be divided by 2.");
        }

        Image newImage = this.copy();
        int bound = (maskSize - 1) / 2;
        for (int y = bound; y < height - bound; y++) {
            for (int x = bound; x < width - bound; x++) {
                ArrayList<Pixel> pixels = new ArrayList<>(maskSize * maskSize);
                for (int j = y - bound; j <= y + bound; j++) {
                    for (int i = x - bound; i <= x + bound; i++) {
                        pixels.add(this.getPixel(i, j));
                    }
                }
                Pixel pixel = mask.apply(pixels);
                newImage.changePixel(x, y, pixel);
            }
        }

        return newImage;
    }

    public Image meanFilter(int maskSize) {
        return this.applyMask(maskSize, (pixels) -> {
            int red = 0, green = 0, blue = 0;

            for (Pixel p: pixels){
                red += p.getRed();
                blue += p.getBlue();
                green += p.getGreen();
            }
            red /= pixels.size();
            green /= pixels.size();
            blue /= pixels.size();

            if (this.type.equals(ImageType.RGB)) {
                return new RGBPixel(red, green, blue);
            } else {
                return new GrayScalePixel(red);
            }
        });
    }

    public Image medianFilter(int maskSize) {
        return this.applyMask(maskSize, (pixels) -> {
            List<Integer> red = new ArrayList<>(pixels.size()),
                    green = new ArrayList<>(pixels.size()),
                    blue = new ArrayList<>(pixels.size());

            for (Pixel p: pixels){
                red.add(p.getRed());
                blue.add(p.getBlue());
                green.add(p.getGreen());
            }
            int index = pixels.size() / 2;

            if (this.type.equals(ImageType.RGB)) {
                return new RGBPixel(red.get(index), green.get(index), blue.get(index));
            } else {
                return new GrayScalePixel(red.get(index));
            }
        });
    }

    public Image ponderateMedianFilter() {
        return this.applyMask(3, (pixels) -> {
            Integer[] values = {1, 2, 1, 2, 4, 2, 1, 2, 1};
            return this.getWeightedValue(pixels, values);
        });
    }

    public Image highPassfilter() {
        return this.applyMask(3, (pixels) -> {
            Integer[] values = {-1, -1, -1, -1, 8, -1, -1, -1, -1};
            return this.getWeightedValue(pixels, values);
        });
    }

    private Pixel getWeightedValue(List<Pixel> pixels, Integer[] values) {
        int red = 0, green = 0, blue = 0, totalWeight = 0;
        for (int j = 0; j < values.length; j++) {
            totalWeight += values[j];
            red += pixels.get(j).getRed() * values[j];
            blue += pixels.get(j).getBlue() * values[j];
            green += pixels.get(j).getGreen() * values[j];
        }
        red /= totalWeight;
        blue /= totalWeight;
        green /= totalWeight;

        if (this.type.equals(ImageType.RGB)) {
            return new RGBPixel(red, green, blue);
        } else {
            return new GrayScalePixel(red);
        }
    }
}
