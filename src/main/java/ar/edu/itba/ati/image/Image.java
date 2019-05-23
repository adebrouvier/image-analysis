package ar.edu.itba.ati.image;

import ar.edu.itba.ati.filters.GaussianMask;
import ar.edu.itba.ati.random.ExponentialGenerator;
import ar.edu.itba.ati.random.GaussianGenerator;
import ar.edu.itba.ati.random.RandomGenerator;
import ar.edu.itba.ati.random.RayleighGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ar.edu.itba.ati.image.Constants.BLACK;
import static ar.edu.itba.ati.image.Constants.WHITE;

public class Image {

    public enum ImageType {
        GRAY_SCALE,
        RGB,
    }

    public enum Format {
        RAW,
        PBM,
        PGM,
        PPM,
        JPEG
    }

    public enum BorderDetectorType {
        LECLERC,
        LORENTZ
    }

    public enum Channel {
        RED,
        GREEN,
        BLUE,
        GRAY
    }

    private int width;
    private int height;
    private List<Pixel> pixels;
    private Format format;
    private ImageType type;
    private File file;

    public Image(int width, int height, List<Pixel> pixels, Format format) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.format = format;
        if (this.format.equals(Format.PPM) || this.format.equals(Format.JPEG)) {
            this.type = ImageType.RGB;
        } else {
            this.type = ImageType.GRAY_SCALE;
        }
    }

    public Image(int width, int height, List<Pixel> pixels, Format format, File file) {
        this(width, height, pixels, format);
        this.file = file;
    }

    public Image(int width, int height, List<Pixel> pixels, ImageType type) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.type = type;
    }

    public Image(BufferedImage bufferedImage, ImageType imageType) {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.pixels = new ArrayList<>(this.width * this.height);
        this.type = imageType;
        for (int i = 0; i < this.height; i++) {
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

    public File getFile(){
        return file;
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
        return new Image(width, height, pixels, format, file);
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

    public Image add(Image image) {
        int maxRed = 0, maxGreen = 0, maxBlue = 0, minRed = 255, minGreen = 255, minBlue = 255;
        Image newImage = this.copy();

        for (int i = 0; i < image.getHeight() && i < this.height; i++) {
            for (int j = 0; j < image.getWidth() && j < this.width; j++) {
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

        for (int i = 0; i < image.getHeight() && i < this.height; i++) {
            for (int j = 0; j < image.getWidth() && j < this.width; j++) {
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

        for (int i = 0; i < image.getHeight() && i < this.height; i++) {
            for (int j = 0; j < image.getWidth() && j < this.width; j++) {
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

        for (int i = 0; i < image.getHeight() && i < this.height; i++) {
            for (int j = 0; j < image.getWidth() && j < this.width; j++) {
                Pixel myPixel = this.getPixel(j, i);
                Pixel otherPixel = image.getPixel(j, i);

                newImage.changePixel(j, i, myPixel.subtract(otherPixel, maxRed, minRed, maxGreen, minGreen, maxBlue, minBlue));
            }
        }
        return newImage;
    }

    public Image multiply(Double d) {
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            p.multiply(d);
        }

        return newImage.dynamicRangeCompress();
    }

    public Image dynamicRangeCompress() {
        double maxRed = 0.0, maxGreen = 0.0, maxBlue = 0.0;
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            maxRed = Math.max(maxRed, p.getRed());
            maxBlue = Math.max(maxBlue, p.getBlue());
            maxGreen = Math.max(maxGreen, p.getGreen());
        }

        double cRed = 255.0 / Math.log(1 + maxRed);
        double cBlue = 255.0 / Math.log(1 + maxBlue);
        double cGreen = 255.0 / Math.log(1 + maxGreen);

        for (Pixel p : newImage.pixels) {
            p.dynamicRangeCompress(cRed, cGreen, cBlue);
        }
        return newImage;
    }

    public Image gammaPower(Double gamma) {
        double c = Math.pow(255.0, 1 - gamma);
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            p.gammaPower(c, gamma);
        }
        if (newImage.type.equals(ImageType.RGB)) {
            newImage.normalizeColor();
        } else {
            newImage.normalize();
        }
        return newImage;
    }

    public Image threshold(Double threshold) {
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            p.threshold(threshold);
        }
        return newImage;
    }

    private Image RGBThreshold(double red, double green, double blue) {
        if (type.equals(ImageType.RGB)) {
            Image newImage = this.copy();
            for (Pixel p : newImage.pixels) {
                ((RGBPixel) p).RGBThreshold(red, green, blue);
            }
            return newImage;
        } else {
            return null;
        }
    }

    public Image saltAndPepperNoise(Double p0, Double p1) {
        Random r = new Random();
        Image image = this.copy();
        for (Pixel p : image.pixels) {
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
        ArrayList<Pixel> newPixels = new ArrayList<>(this.pixels.size());
        for (Pixel p : this.pixels) {
            if (this.type.equals(ImageType.RGB)) {
                newPixels.add(new RGBPixel(p.getRed(), p.getGreen(), p.getBlue()));
            } else {
                newPixels.add(new GrayScalePixel(p.getRed()));
            }
        }
        return new Image(this.width, this.height, newPixels, this.type);
    }

    public Image copyToGrayscale() {
        ArrayList<Pixel> newPixels = new ArrayList<>(this.pixels.size());
        for (Pixel p : this.pixels) {
            newPixels.add(new GrayScalePixel(p.getGrayscale()));
        }
        return new Image(this.width, this.height, newPixels, ImageType.GRAY_SCALE);
    }

    public int getGrayScaleMean() {
        return (int) pixelMean(pixels);
    }

    private double pixelMean(List<Pixel> pixels) {
        double total = 0;
        for (Pixel p : pixels) {
            total += ((GrayScalePixel) p).getGrayScale();
        }
        return total / pixels.size();
    }

    public int getGrayScaleStDev() {
        int mean = getGrayScaleMean();
        int acum = 0;

        for (Pixel p : pixels) {
            acum += Math.pow(((GrayScalePixel) p).getGrayScale() - mean, 2);
        }

        return (int) Math.sqrt((1.0 / pixels.size()) * acum);
    }

    public Image increaseContrast(int r1, int r2, int s1, int s2) {
        Image image = copy();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                GrayScalePixel p = (GrayScalePixel) getPixel(x, y);
                int newValue = p.getGrayScale();
                if (p.getGrayScale() <= r1) {
                    newValue = f1(newValue, r1, s1);
                }
                if (p.getGrayScale() >= r2) {
                    newValue = f2(newValue, r2, s2);
                }
                image.changePixel(x, y, new GrayScalePixel(newValue));
            }
        }
        return image;
    }

    private int f1(int x, int r, int s) {
        return slope(x, 0, 0, r, s);
    }

    private int f2(int x, int r, int s) {
        return slope(x, 255, 255, r, s);
    }

    private int slope(int x, double x1, double y1, double x2, double y2) {
        return (int) ((x - x1) * (y2 - y1) / (x2 - x1) + y1);
    }

    public Image addExponentialNoise(double percentage, double lambda) {
        return multiplyNoise(percentage, new ExponentialGenerator(lambda));
    }

    public Image addRayleighNoise(double percentage, double phi) {
        return multiplyNoise(percentage, new RayleighGenerator(phi));
    }

    public Image addGaussianNoise(double percentage, double mean, double stDev) {
        GaussianGenerator generator = new GaussianGenerator(stDev, mean);
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            if (Math.random() < percentage) {
                int noise = (int) generator.getDouble();
                if (this.type.equals(ImageType.RGB)) {
                    p.add(new RGBPixel(noise, noise, noise));
                } else {
                    p.add(new GrayScalePixel(noise));
                }
            }
        }
        if (this.type.equals(ImageType.RGB)){
            newImage.normalizeColor();
        } else {
            newImage.normalize();
        }
        return newImage;
    }

    private Image multiplyNoise(double percentage, RandomGenerator generator) {
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            if (Math.random() < percentage) {
                double noise = generator.getDouble();
                p.multiply(noise);
            }
        }
        return newImage.dynamicRangeCompress();
    }

    private void normalize() {
        double max = 0;
        double min = 255;

        for (Pixel p : pixels) {
            GrayScalePixel pixel = (GrayScalePixel) p;
            max = Math.max(max, pixel.getGrayScale());
            min = Math.min(min, pixel.getGrayScale());
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel p = getPixel(x, y);
                int newVal = (int) ((p.getRed() - min) / (max - min) * 255);
                Pixel newPixel = new GrayScalePixel(newVal);
                changePixel(x, y, newPixel);
            }
        }
    }

    private void normalizeColor() {
        double maxRed = 0, maxGreen = 0, maxBlue = 0;
        double minRed = 255, minGreen = 255, minBlue = 255;

        for (Pixel p : pixels) {
            RGBPixel pixel = (RGBPixel) p;
            maxRed = Math.max(maxRed, pixel.getRed());
            minRed = Math.min(minRed, pixel.getRed());
            maxGreen = Math.max(maxGreen, pixel.getGreen());
            minGreen = Math.min(minGreen, pixel.getGreen());
            maxBlue = Math.max(maxBlue, pixel.getBlue());
            minBlue = Math.min(minBlue, pixel.getBlue());
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel p = getPixel(x, y);
                int newRed = (int) ((p.getRed() - minRed) / (maxRed - minRed) * 255);
                int newGreen = (int) ((p.getGreen() - minGreen) / (maxGreen - minGreen) * 255);
                int newBlue = (int) ((p.getBlue() - minBlue) / (maxBlue - minBlue) * 255);

                Pixel newPixel = new RGBPixel(newRed, newGreen, newBlue);
                changePixel(x, y, newPixel);
            }
        }
    }

    public Image applyMask(int maskSize, MaskApplier mask) {
        return applyMask(maskSize, mask, true);
    }

    public Image applyMask(int maskSize, MaskApplier mask, boolean normalize) {
        if (maskSize % 2 != 1) {
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
        if (normalize) {
            if (newImage.type.equals(ImageType.RGB)) {
                newImage.normalizeColor();
            } else {
                newImage.normalize();
            }
        }
        return newImage;
    }

    public Image meanFilter(int maskSize) {
        return this.applyMask(maskSize, (pixels) -> {
            int red = 0, green = 0, blue = 0;

            for (Pixel p : pixels) {
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

            for (Pixel p : pixels) {
                red.add(p.getRed());
                blue.add(p.getBlue());
                green.add(p.getGreen());
            }
            return getMedian(pixels, red, green, blue);
        });
    }

    private Pixel getMedian(List<Pixel> pixels, List<Integer> red, List<Integer> green, List<Integer> blue) {
        int index = pixels.size() / 2;

        Collections.sort(red);
        Collections.sort(green);
        Collections.sort(blue);

        if (this.type.equals(ImageType.RGB)) {
            return new RGBPixel(red.get(index), green.get(index), blue.get(index));
        } else {
            return new GrayScalePixel(red.get(index));
        }
    }

    public Image weightedMedian() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {1.0, 2.0, 1.0, 2.0, 4.0, 2.0, 1.0, 2.0, 1.0};
            return this.getWeightedMedian(pixels, values);
        });
    }

    public Image highPassFilter() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, -1.0, -1.0, -1.0, 8.0, -1.0, -1.0, -1.0, -1.0};
            return this.getWeightedValue(pixels, values);
        });
    }

    private Pixel getWeightedMedian(List<Pixel> pixels, Double[] values) {
        List<Integer> red = new ArrayList<>(pixels.size()),
                green = new ArrayList<>(pixels.size()),
                blue = new ArrayList<>(pixels.size());

        for (int j = 0; j < values.length; j++) {
            int finalJ = j;
            IntStream.rangeClosed(1, values[j].intValue()).forEach(i -> {
                red.add(pixels.get(finalJ).getRed());
                blue.add(pixels.get(finalJ).getBlue());
                green.add(pixels.get(finalJ).getGreen());
            });
        }

        return getMedian(pixels, red, green, blue);
    }

    public Pixel getWeightedValue(List<Pixel> pixels, Double[] values) {
        int red = 0, green = 0, blue = 0, totalWeight = 0;
        for (int j = 0; j < values.length; j++) {
            totalWeight += values[j];
            red += pixels.get(j).getRed() * values[j];
            blue += pixels.get(j).getBlue() * values[j];
            green += pixels.get(j).getGreen() * values[j];
        }
        if (totalWeight != 0) {
            red /= totalWeight;
            blue /= totalWeight;
            green /= totalWeight;
        }

        if (this.type.equals(ImageType.RGB)) {
            return new RGBPixel(red, green, blue);
        } else {
            return new GrayScalePixel(red);
        }
    }

    public Pixel getUnweightedValue(List<Pixel> pixels, Double[] values) {
        int red = 0, green = 0, blue = 0;
        for (int j = 0; j < values.length; j++) {
            red += pixels.get(j).getRed() * values[j];
            blue += pixels.get(j).getBlue() * values[j];
            green += pixels.get(j).getGreen() * values[j];
        }

        if (this.type.equals(ImageType.RGB)) {
            return new RGBPixel(red, green, blue);
        } else {
            return new GrayScalePixel(red);
        }
    }

    public Image gaussMaskFilter(Double std, Integer maskSize) {
        Double[] mask = new GaussianMask(maskSize, std, new GaussianWeight()).getMask();
        return this.applyMask(maskSize, (pixels) ->
                this.getWeightedValue(pixels, mask)
        );
    }

    public Image getNegative() {
        Image newImage = this.copy();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel p = getPixel(x, y);
                Pixel newPixel;
                if (type.equals(Image.ImageType.RGB)) {
                    newPixel = new RGBPixel(negative(p.getRed()), negative(p.getGreen()), negative(p.getBlue()));
                } else {
                    GrayScalePixel g = (GrayScalePixel) p;
                    newPixel = new GrayScalePixel(negative(g.getGrayScale()));
                }
                newImage.changePixel(x, y, newPixel);
            }
        }
        return newImage;
    }

    private int negative(int component) {
        return -component + 256 - 1;
    }

    public Image prewittOperation() {
        Image firstOperator = this.prewittFirstOperator();

        Image secondOperator = this.prewittSecondOperator();

        return firstOperator.moduleOperation(secondOperator);
    }

    public Image prewittFirstOperator() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0};
            return this.getWeightedValue(pixels, values);
        }, false);
    }

    public Image prewittSecondOperator() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, 0.0, 1.0, -1.0, 0.0, 1.0, -1.0, 0.0, 1.0};
            return this.getWeightedValue(pixels, values);
        }, false);
    }

    public Image sobelOperation() {
        Image firstOperator = this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, -2.0, -1.0, 0.0, 0.0, 0.0, 1.0, 2.0, 1.0};
            return this.getWeightedValue(pixels, values);
        }, false);

        Image secondOperator = this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, 0.0, 1.0, -2.0, 0.0, 2.0, -1.0, 0.0, 1.0};
            return this.getWeightedValue(pixels, values);
        }, false);

        return firstOperator.moduleOperation(secondOperator);
    }

    public Image globalThreshold() {
        double threshold = getGrayScaleMean();
        double previousThreshold = 0;

        while (Math.abs(previousThreshold - threshold) > 1) {
            List<Pixel> g1 = new ArrayList<>();
            List<Pixel> g2 = new ArrayList<>();

            for (Pixel p : pixels) {
                if (p.getRed() <= threshold) {
                    g1.add(p);
                } else {
                    g2.add(p);
                }
            }
            previousThreshold = threshold;
            threshold = 0.5 * (pixelMean(g1) + pixelMean(g2));
        }
        System.out.println("Global threshold: " + previousThreshold);
        return threshold(previousThreshold);
    }

    public Image otsuThreshold() {

        if (type.equals(ImageType.GRAY_SCALE)){
            double threshold = channelOtsu(Channel.GRAY);
            return threshold(threshold);
        }else {
            double redThreshold = channelOtsu(Channel.RED);
            double greenThreshold = channelOtsu(Channel.GREEN);
            double blueThreshold = channelOtsu(Channel.BLUE);
            return RGBThreshold(redThreshold, greenThreshold, blueThreshold);
        }
    }

    private double channelOtsu(Channel channel){
        Histogram histogram = new Histogram(this, channel);
        Map<Integer, Double> acumSum = histogram.pdf();
        Map<Integer, Double> acumMean = histogram.accumulativeMean();
        double globalMean = histogram.globalMean();

        Map<Integer, Double> variance = sampleVariance(acumSum, acumMean, globalMean);
        double max = 0;
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < variance.size(); i++) {
            double v = variance.get(i);
            if (v == max) {
                indexes.add(i);
            } else if (v > max) {
                max = v;
                indexes = new ArrayList<>();
                indexes.add(i);
            }
        }

        OptionalDouble threshold = indexes.stream().mapToInt(Integer::intValue).average();
        System.out.println("Otsu threshold: " + threshold.getAsDouble());
        return threshold.getAsDouble();
    }

    private Map<Integer, Double> sampleVariance(Map<Integer, Double> acumSum,
                                                Map<Integer, Double> acumMean, double globalMean) {

        Map<Integer, Double> variance = new HashMap<>();
        for (int i = 0; i < acumSum.size(); i++) {
            double p1 = acumSum.get(i);
            double m = acumMean.get(i);
            variance.put(i, Math.pow((globalMean * p1 - m), 2) / (p1 * (1 - p1)));
        }
        return variance;
    }

    public Image isotropicDiffusion(int time) {
        return diffusion(time, (gradient) -> 1.0);
    }

    public Image anisotropicDiffusion(int time, double sigma, BorderDetectorType detector) {

        BorderDetector borderDetector;

        switch (detector) {
            case LECLERC:
                borderDetector = (gradient) ->
                        Math.exp(-Math.pow(gradient, 2) / Math.pow(sigma, 2));
                break;
            case LORENTZ:
                borderDetector = (gradient) ->
                        1 / ((Math.pow(gradient, 2) / Math.pow(sigma, 2)) + 1);
                break;
            default:
                throw new IllegalArgumentException("Invalid border detector");
        }

        return diffusion(time, borderDetector);
    }

    private Image diffusion(int time, BorderDetector borderDetector) {

        Image currentImage = this.copy();
        Image newImage = this.copy();

        for (int t = 0; t < time; t++) {
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    final GrayScalePixel pixel = (GrayScalePixel) currentImage.getPixel(x, y);
                    List<GrayScalePixel> neighbours = currentImage.neighbours(x, y);

                    List<Integer> derivatives = neighbours
                            .stream()
                            .map(n -> derivative(pixel, n))
                            .collect(Collectors.toList());

                    List<Double> coefficients = derivatives
                            .stream()
                            .map(borderDetector::apply)
                            .collect(Collectors.toList());

                    GrayScalePixel newPixel = new GrayScalePixel(addDerivatives(derivatives, coefficients));
                    newImage.getPixel(x, y).add(newPixel);
                }
            }
            newImage.normalize();
            currentImage = newImage.copy();
        }

        return newImage;
    }

    /**
     * Returns the north, south, west and east neighbours of a pixel located on the (x, y) coordinates
     *
     * @param x coordinate of the pixel
     * @param y coordinate of the pixel
     * @return a list of neighbouring pixels
     */
    private List<GrayScalePixel> neighbours(int x, int y) {
        List<int[]> coordinates = Arrays.asList(
                new int[]{x, y - 1}, // North
                new int[]{x, y + 1}, // South
                new int[]{x + 1, y}, // West
                new int[]{x - 1, y}  // East
        );

        return coordinates.stream()
                .map(coordinate -> (GrayScalePixel) getPixel(coordinate[0], coordinate[1]))
                .collect(Collectors.toList());
    }

    private int derivative(GrayScalePixel p, GrayScalePixel direction) {
        return direction.getGrayScale() - p.getGrayScale();
    }

    private int addDerivatives(List<Integer> derivatives, List<Double> coefficients) {
        return (int) (0.25 * (
                derivatives.get(0) * coefficients.get(0) +
                        derivatives.get(1) * coefficients.get(1) +
                        derivatives.get(2) * coefficients.get(2) +
                        derivatives.get(3) * coefficients.get(3)));
    }

    public Image laplacianOperator(int threshold) {
        Image masked = this.applyMask(3, (pixels) -> {
            Double[] values = {0.0, -1.0, 0.0, -1.0, 4.0, -1.0, 0.0, -1.0, 0.0};
            return this.getWeightedValue(pixels, values);
        }, false);
        return masked.zeroCrossing(threshold);
    }

    public Image logOperator(double sigma, int threshold) {
        int maskSize = 7;
        Double[] mask = new GaussianMask(maskSize, sigma, new LoGWeight()).getMask();
        Image log = this.applyMask(maskSize, (pixels) ->
                this.getWeightedValue(pixels, mask), false);
        return log.zeroCrossing(threshold);
    }

    public Image zeroCrossing(int threshold) {

        Image result = this.copy();

        // Horizontal
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                GrayScalePixel pixel = (GrayScalePixel) getPixel(x, y);

                if (x + 1 >= width) {
                    result.changePixel(x, y, new GrayScalePixel(BLACK)); // Set border pixels
                    continue;
                }

                GrayScalePixel nextPixel = (GrayScalePixel) getPixel(x + 1, y);
                boolean zero = false;

                if (nextPixel.getGrayScale() == 0) {
                    result.changePixel(x, y, new GrayScalePixel(BLACK));
                    nextPixel = (GrayScalePixel) getPixel(x + 2, y);
                    zero = true;
                }

                int color = BLACK;

                if (Math.signum(pixel.getGrayScale()) != Math.signum(nextPixel.getGrayScale())) {
                    if (slopeEvaluation(pixel, nextPixel, threshold)) {
                        color = WHITE;
                        if (zero) {
                            x++;
                        }
                    }
                }
                result.changePixel(x, y, new GrayScalePixel(color));
            }
        }
        // Vertical
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) {
                GrayScalePixel pixel = (GrayScalePixel) getPixel(x, y);
                GrayScalePixel nextPixel = (GrayScalePixel) getPixel(x, y + 1);
                boolean zero = false;

                if (nextPixel.getGrayScale() == 0) {
                    nextPixel = (GrayScalePixel) getPixel(x, y + 2);
                    zero = true;
                }

                if (Math.signum(pixel.getGrayScale()) != Math.signum(nextPixel.getGrayScale())) {
                    if (slopeEvaluation(pixel, nextPixel, threshold)) {
                        if (zero) {
                            y++;
                        }
                        result.changePixel(x, y, new GrayScalePixel(WHITE));
                    }
                }
            }
        }

        return result;
    }

    private boolean slopeEvaluation(GrayScalePixel pixel, GrayScalePixel nextPixel, int threshold) {
        return Math.abs(pixel.getGrayScale()) + Math.abs(nextPixel.getGrayScale()) > threshold;
    }

    public Image bilateralFilter(int maskSize, Double spatialConst, Double colorConst) {
        return this.applyMask(maskSize, (pixels) -> {
            Pixel centerPixel = pixels.get(pixels.size()/2);
            int index = 0;
            Double totalWeight = 0.0;

            Double color = 0.0;
            for (Pixel p: pixels) {
                int distanceX = index % maskSize - (maskSize / 2);
                int distanceY = index / maskSize - (maskSize / 2);
                Double firstTerm = getFirstTermBilateral(spatialConst, distanceX, distanceY);
                Double secondTerm = getSecondTermBilateral(colorConst, centerPixel.getRed(), p.getRed());
                Double weight = Math.exp(- firstTerm - secondTerm);

                totalWeight += weight;
                color += p.getRed() * weight;

                index++;
            }
            if (centerPixel instanceof RGBPixel) {
                throw new IllegalStateException("Cannot apply this to Color images.");
            } else {
                color /= totalWeight;
                return new GrayScalePixel(color.intValue());
            }
        }, false);
    }

    private Double getFirstTermBilateral(Double spatialConst, int distanceX, int distanceY) {
        return (Math.pow(distanceX, 2) + Math.pow(distanceY, 2)) / (2 * Math.pow(spatialConst, 2));
    }

    private Double getSecondTermBilateral(Double colorConst, int centerColor, int currentColor) {
        return Math.pow(Math.abs(currentColor - centerColor), 2) / (2 * Math.pow(colorConst, 2));
    }

    private Image moduleOperation(Image other) {
        return this.moduleOperation(other, true);
    }

    private Image moduleOperation(Image other, boolean normalize) {
        Image image = this.copy();

        for (int i = 0; i < image.getHeight() && i < this.height; i++) {
            for (int j = 0; j < image.getWidth() && j < this.width; j++) {
                Pixel myPixel = this.getPixel(j, i);
                Pixel otherPixel = other.getPixel(j, i);
                int red = (int) Math.sqrt(Math.pow(myPixel.getRed(), 2) + Math.pow(otherPixel.getRed(), 2));
                int blue = (int) Math.sqrt(Math.pow(myPixel.getBlue(), 2) + Math.pow(otherPixel.getBlue(), 2));
                int green = (int) Math.sqrt(Math.pow(myPixel.getGreen(), 2) + Math.pow(otherPixel.getGreen(), 2));
                if (image.type.equals(ImageType.RGB)) {
                    image.changePixel(j, i, new RGBPixel(red, blue, green));
                } else {
                    image.changePixel(j, i, new GrayScalePixel(red));
                }
            }
        }
        if (!normalize) {
            return image;
        }
        if (image.type.equals(ImageType.RGB)) {
            image.normalizeColor();
        } else {
            image.normalize();
        }
        return image;
    }

    public Image cannyBorderDetector(Integer t1, Integer t2) {
        Image copy = this.copyToGrayscale();
        Image xOperator = copy.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, -2.0, -1.0, 0.0, 0.0, 0.0, 1.0, 2.0, 1.0};
            return copy.getWeightedValue(pixels, values);
        }, false);

        Image yOperator = copy.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, 0.0, 1.0, -2.0, 0.0, 2.0, -1.0, 0.0, 1.0};
            return copy.getWeightedValue(pixels, values);
        }, false);

        Image border = xOperator.moduleOperation(yOperator, false);
        Image tangent = border.copy();
        Image cannyBorder = border.copy();
        for (int i = 0; i < tangent.getWidth(); i++) {
            for (int j = 0; j < tangent.getHeight(); j++) {
                if (i == 0 || j == 0 || i == tangent.getHeight() - 1 || j == tangent.getWidth() - 1){
                    cannyBorder.changePixel(i, j, new GrayScalePixel(0));
                }
                Pixel xPixel = xOperator.getPixel(i, j);
                Pixel yPixel = yOperator.getPixel(i, j);
                int red = 0;
                if (xPixel.getRed() != 0) {
                    red = (int) (Math.atan(yPixel.getRed()/xPixel.getRed()) + Math.PI /2);
                }
                red = getCannyAngle(red);
                tangent.changePixel(i, j, new GrayScalePixel(red));
//                int blue = (int) Math.atan2(xPixel.getRed(), yPixel.getRed());
//                int green = (int) Math.atan2(xPixel.getRed(), yPixel.getRed());
            }
        }

        for (int i = 1; i < tangent.getWidth() - 1; i++) {
            for (int j = 1; j < tangent.getHeight() - 1; j++) {
                Pixel neighbor1, neighbor2;
                Pixel currentPixel = border.getPixel(i,j);
                if (currentPixel.getRed() == 0) {
                    continue;
                }
                int angle = tangent.getPixel(i, j).getRed();
                if (angle == 45) {
                    neighbor1 = border.getPixel(i - 1, j - 1);
                    neighbor2 = border.getPixel(i + 1, j + 1);
                } else if (angle == 90) {
                    neighbor1 = border.getPixel(i, j - 1);
                    neighbor2 = border.getPixel(i, j + 1);
                } else if (angle == 135) {
                    neighbor1 = border.getPixel(i - 1, j + 1);
                    neighbor2 = border.getPixel(i + 1, j - 1);
                } else {
                    neighbor1 = border.getPixel(i - 1, j);
                    neighbor2 = border.getPixel(i + 1, j);
                }
                if (neighbor1.getRed() > currentPixel.getRed() ||
                neighbor2.getRed() > currentPixel.getRed()) {
                    cannyBorder.changePixel(i, j, new GrayScalePixel(0));
                }
            }
        }

        return cannyBorder.thresholdCanny(t1, t2);
    }

    private int getCannyAngle(int color) {
        int firstLimit = (int) (22.5 * Math.PI / 180);
        int secondLimit = (int) (67.5 * Math.PI / 180);
        int thirdLimit = (int) (112.5 * Math.PI / 180);
        int forthLimit = (int) (157.5 * Math.PI / 180);

        if (color > firstLimit && color < secondLimit) {
            return 45;
        } else if (color > secondLimit && color < thirdLimit) {
            return 90;
        } else if (color > thirdLimit && color < forthLimit) {
            return 135;
        } else {
            return 0;
        }
    }

    private Image thresholdCanny(Integer t1, Integer t2) {
        return this.applyMask(3, (pixels) -> {
           Pixel middlePixel = pixels.get(pixels.size()/2);
           if (middlePixel.getRed() < t1) {
               return new GrayScalePixel(0);
           } else if (middlePixel.getRed() > t2) {
               return new GrayScalePixel(255);
           } else {
               for(Pixel p: pixels) {
                   if (p.getRed() > t2) {
                       return new GrayScalePixel(255);
                   }
               }
               return new GrayScalePixel(0);
           }
        });
    }

    public Image susanBorderAndCorners() {
        Image image = this.applyMask(7, (pixels -> {
            Double[] mask = {
                0.0,0.0,1.1,1.1,1.1,0.0,0.0,
                0.0,1.0,1.0,1.0,1.0,1.0,0.0,
                1.0,1.0,1.0,1.0,1.0,1.0,1.0,
                1.0,1.0,1.0,1.0,1.0,1.0,1.0,
                1.0,1.0,1.0,1.0,1.0,1.0,1.0,
                0.0,1.0,1.0,1.0,1.0,1.0,0.0,
                0.0,0.0,1.0,1.0,1.0,0.0,0.0
            };
            double susanValue = 0.0;
            final int THRESHOLD = 27;
            Pixel middlePixel = pixels.get(pixels.size() / 2);
            int middleGray = middlePixel.getGrayscale();
            int index = 0;
            for (Pixel p: pixels) {
                int gray = (int) (p.getGrayscale() * mask[index]);
                index++;
                if (gray == 0) {
                    continue;
                }
                int c = Math.abs(p.getGrayscale() - middleGray);
                if (c < THRESHOLD) {
                    susanValue += 1;
                }
            }
            susanValue = 1 - susanValue / 37;
            if (0.45 <= susanValue && susanValue <= 0.55) {
                return new RGBPixel(0, 255, 0);
            } else if (0.70 <= susanValue && susanValue <= 0.80) {
                return new RGBPixel(255, 0, 0);
            } else {
                return new RGBPixel(middlePixel.getRed(), middlePixel.getGreen(), middlePixel.getBlue());
            }
        }), false);
        image.type = ImageType.RGB;
        return image;
    }

    public Image linearHough(double angleStep, double roStep, double epsilon, int maxLinesDraw) {
        Image copy = this.copy();
        List<Pair<Integer, Integer>> whitePixels = this.getWhitePixelsCoordinates();
        HashMap<Pair<Integer, Integer>, Integer> votes = new HashMap<>();

        Double maxLineDistance = Math.sqrt(2) * Math.max(this.getWidth(), this.getHeight());
        for (Double a = -Math.PI; a < Math.PI ; a += angleStep) {
            for (Double p = 0.0; p < maxLineDistance; p += roStep) {
                for (Pair<Integer, Integer> coords: whitePixels) {
                    if (Math.abs(p - coords.getLeft() * Math.cos(a) - coords.getRight() * Math.sin(a)) < epsilon) {
                        Pair<Integer, Integer> vote = new ImmutablePair<>(p.intValue(), angle2integer(a));
                        if (votes.containsKey(vote)) {
                            Integer numberOfVotes = votes.get(vote);
                            numberOfVotes += 1;
                            votes.put(vote, numberOfVotes);
                        } else {
                            votes.put(vote, 1);
                        }
                    }
                }
            }
        }

        PriorityQueue<Map.Entry<Pair<Integer, Integer>, Integer>> pq = new PriorityQueue<>(1,
                (o1, o2) -> o2.getValue() - o1.getValue());
        pq.addAll(votes.entrySet());

        for (int i = 0; i < maxLinesDraw; i++) {
            Pair<Integer, Integer> parameters = pq.poll().getKey();
            Integer p = parameters.getLeft();
            double a = integer2angle(parameters.getRight());
            System.out.println("Angle:" + a + "; Ro:" + p);
            if (a == 0) {
                copy.drawRedLine(p, this.height, p, 0);
            } else {
                copy.drawRedLine(0, (int) (p / Math.sin(a)),
                        this.width, (int) ((p - this.width * Math.cos(a)) / Math.sin(a)));
            }
        }
        return copy;
    }

    private List<Pair<Integer, Integer>> getWhitePixelsCoordinates() {
        List<Pair<Integer, Integer>> whitePixels = new LinkedList<>();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Pixel p = this.getPixel(i, j);
                if (p.getRed() == 255) {
                    whitePixels.add(new ImmutablePair<>(i, j));
                }
            }
        }
        return whitePixels;
    }

    private void drawRedLine(Integer x0, Integer y0, Integer x1, Integer y1) {
        int deltaX = (x1 - x0);
        int deltaY = (y1 - y0);
        if (deltaY > deltaX) {
            int deltaErr = 2 * deltaX - deltaY;
            Integer x = x0;
            for (Integer y = y0; y < x1; y++){
                if (x > 0 && x < this.width && y > 0 && y < this.height) {
                    this.changePixel(x, y, new RGBPixel(255, 0, 0));
                }
                if (deltaErr > 0){
                    x = x + 1;
                    deltaErr -= 2 * deltaY;
                }
                deltaErr += 2 * deltaX;
            }
        } else {
            int deltaErr = 2 * deltaY - deltaX;
            Integer y = y0;
            for (Integer x = x0; x < x1; x++){
                if (x > 0 && x < this.width && y > 0 && y < this.height) {
                    this.changePixel(x, y, new RGBPixel(255, 0, 0));
                }
                if (deltaErr > 0){
                    y = y + 1;
                    deltaErr -= 2 * deltaX;
                }
                deltaErr += 2 * deltaY;
            }
        }
    }

    private void drawRedCircle(Integer x0, Integer y0, Integer radius) {
        double step = radius / Math.pow((2* Math.PI * radius),2);
        for (double angle = 0; angle < Math.PI * 2; angle += step) {
            Integer x = x0 + (int) (radius * Math.cos(angle));
            Integer y = y0 + (int) (radius * Math.sin(angle));
            if (x > 0 && x < this.width && y > 0 && y < this.height) {
                this.changePixel(x, y, new RGBPixel(255, 0, 0));
            }
        }
    }

    public Image circularHough(Integer xStep, Integer yStep, Double radiusStep, double epsilon, int maxLinesDraw) {
        Image copy = this.copy();
        List<Pair<Integer, Integer>> whitePixels = this.getWhitePixelsCoordinates();
        HashMap<Pair<Integer, Pair<Integer, Integer>>, Integer> votes = new HashMap<>();

        Double maxRadius = Math.sqrt(2) * Math.max(this.getWidth(), this.getHeight()) / 2;
        for (Integer i = 0; i < this.getWidth(); i += xStep) {
            for (Integer j = 0; j < this.getHeight(); j += yStep) {
                for (int r = 1; r < maxRadius; r += radiusStep) {
                    for (Pair<Integer, Integer> coords : whitePixels) {
                        if (Math.abs(r*r - Math.pow(coords.getLeft() - i, 2) - Math.pow(coords.getRight() - j, 2)) < epsilon) {
                            Pair<Integer, Integer> centerVote = new ImmutablePair<>(i, j);
                            Pair<Integer, Pair<Integer, Integer>> vote = new ImmutablePair<>(r, centerVote);
                            if (votes.containsKey(vote)) {
                                Integer numberOfVotes = votes.get(vote);
                                numberOfVotes += 1;
                                votes.put(vote, numberOfVotes);
                            } else {
                                votes.put(vote, 1);
                            }
                        }
                    }
                }
            }
        }

        PriorityQueue<Map.Entry<Pair<Integer, Pair<Integer, Integer>>, Integer>> pq = new PriorityQueue<>(1,
                (o1, o2) -> o2.getValue() - o1.getValue());
        pq.addAll(votes.entrySet());

        for (int i = 0; i < maxLinesDraw; i++) {
            Pair<Integer, Pair<Integer, Integer>> parameters = pq.poll().getKey();
            Integer x0 = parameters.getRight().getLeft();
            Integer y0 = parameters.getRight().getRight();
            Integer radius = parameters.getLeft();
            System.out.println("x0:" + x0 + "; y0:" + y0 + "; r:" +radius);
            copy.drawRedCircle(x0, y0, radius);
        }
        return copy;
    }

    private Integer angle2integer(Double angle) {
        return new Double(angle * 1E8).intValue();
    }

    private Double integer2angle(Integer angle) {
        return angle / 1E8;
    }
}
