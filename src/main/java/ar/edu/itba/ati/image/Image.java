package ar.edu.itba.ati.image;

import ar.edu.itba.ati.random.ExponentialGenerator;
import ar.edu.itba.ati.random.GaussianGenerator;
import ar.edu.itba.ati.random.RandomGenerator;
import ar.edu.itba.ati.random.RayleighGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
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
        PPM
    }

    public enum BorderDetectorType {
        LECLERC,
        LORENTZ
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
        if (this.format.equals(Format.PPM)) {
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
        return new Image(this.width, this.height, newPixels, this.format);
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
                GrayScalePixel pixel = (GrayScalePixel) p;
                double noise = generator.getDouble();
                pixel.add(new GrayScalePixel((int) noise));
            }
        }
        newImage.normalize();
        return newImage;
    }

    private Image multiplyNoise(double percentage, RandomGenerator generator) {
        Image newImage = this.copy();
        for (Pixel p : newImage.pixels) {
            if (Math.random() < percentage) {
                GrayScalePixel pixel = (GrayScalePixel) p;
                double noise = generator.getDouble();
                pixel.multiply(noise);
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

    private Image applyMask(int maskSize, MaskApplier mask) {
        return applyMask(maskSize, mask, true);
    }

    private Image applyMask(int maskSize, MaskApplier mask, boolean normalize) {
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

    private Pixel getWeightedValue(List<Pixel> pixels, Double[] values) {
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

    public Image gaussMaskFilter(Double std, Integer maskSize) {
        Double[] mask = getGaussianMask(maskSize, std, new GaussianWeight());
        return this.applyMask(maskSize, (pixels) ->
                this.getWeightedValue(pixels, mask)
        );
    }

    private Double[] getGaussianMask(Integer maskSize, Double std, Weight weight) {
        Double[] mask = new Double[maskSize * maskSize];
        for (int i = 0; i < maskSize; i++) {
            for (int j = 0; j < maskSize; j++) {
                int x = i - maskSize / 2;
                int y = j - maskSize / 2;
                mask[i + j * maskSize] = weight.get(std, x, y);
            }
        }
        return mask;
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

        return firstOperator.substract(secondOperator);
    }

    public Image prewittFirstOperator() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0};
            return this.getWeightedValue(pixels, values);
        });
    }

    public Image prewittSecondOperator() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, 0.0, 1.0, -1.0, 0.0, 1.0, -1.0, 0.0, -1.0};
            return this.getWeightedValue(pixels, values);
        });
    }

    public Image sobelOperation() {
        Image firstOperator = this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, -2.0, -1.0, 0.0, 0.0, 0.0, 1.0, 2.0, 1.0};
            return this.getWeightedValue(pixels, values);
        });

        Image secondOperator = this.applyMask(3, (pixels) -> {
            Double[] values = {-1.0, 0.0, 1.0, -2.0, 0.0, 2.0, -1.0, 0.0, -1.0};
            return this.getWeightedValue(pixels, values);
        });

        return firstOperator.substract(secondOperator);
    }

    public Image globalThreshold() {
        double threshold = getGrayScaleMean();
        double previousThreshold = 0;
        Image newImage = this;

        while (Math.abs(previousThreshold - threshold) > 1) {
            newImage = threshold(threshold);
            List<Pixel> g1 = new ArrayList<>();
            List<Pixel> g2 = new ArrayList<>();

            for (Pixel p : newImage.pixels) {
                if (p.getRed() == Constants.WHITE) {
                    g1.add(p);
                } else {
                    g2.add(p);
                }
            }
            previousThreshold = threshold;
            threshold = 0.5 * (pixelMean(g1) + pixelMean(g2));
        }

        System.out.println("Global threshold: " + threshold);

        return newImage;
    }

    public Image otsuThreshold() {

        Histogram histogram = new Histogram(this);
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
        return threshold(threshold.getAsDouble());
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
                            .map(d -> borderDetector.apply(pixel.getGrayScale() * d)) //TODO: ask
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
        return direction.getGrayScale() - p.getGrayScale();  //TODO: ask
    }

    private int addDerivatives(List<Integer> derivatives, List<Double> coefficients) {
        return (int) (0.25 * (
                derivatives.get(0) * coefficients.get(0) +
                        derivatives.get(1) * coefficients.get(1) +
                        derivatives.get(2) * coefficients.get(2) +
                        derivatives.get(3) * coefficients.get(3)));
    }

    public Image laplacianOperator() {
        return this.applyMask(3, (pixels) -> {
            Double[] values = {0.0, -1.0, 0.0, -1.0, 4.0, -1.0, 0.0, -1.0, 0.0};
            return this.getWeightedValue(pixels, values);
        });
    }

    public Image logOperator(double sigma) {
        int maskSize = 7;
        Double[] mask = getGaussianMask(maskSize, sigma, new LoGWeight());
        Image log = this.applyMask(maskSize, (pixels) ->
                this.getWeightedValue(pixels, mask), false);

        Image result = log.copy();

        // Zero crossing
        // Horizontal
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                GrayScalePixel pixel = (GrayScalePixel) log.getPixel(x, y);
                GrayScalePixel nextPixel = (GrayScalePixel) log.getPixel(x + 1, y);

                if (nextPixel.getGrayScale() == BLACK) {
                    nextPixel = (GrayScalePixel) log.getPixel(x + 2, y);
                }

                int color = WHITE;

                if (Math.signum(pixel.getGrayScale()) != Math.signum(nextPixel.getGrayScale())) {
                    color = BLACK;
                }
                result.changePixel(x, y, new GrayScalePixel(color));
            }
        }
        // Vertical
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) {
                GrayScalePixel pixel = (GrayScalePixel) log.getPixel(x, y);
                GrayScalePixel nextPixel = (GrayScalePixel) log.getPixel(x, y + 1);

                if (nextPixel.getGrayScale() == BLACK) {
                    nextPixel = (GrayScalePixel) log.getPixel(x, y + 2);
                }

                if (Math.signum(pixel.getGrayScale()) != Math.signum(nextPixel.getGrayScale())) {
                    result.changePixel(x, y, new GrayScalePixel(BLACK));
                }
            }
        }

        //TODO: evaluación de la pendiente

        return result;
    }
}
