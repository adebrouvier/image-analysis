package ar.edu.itba.ati.image;

import java.util.List;

public class ImageInformation {

    private Image.ImageType imageType;
    private int pixelAmount = 0;
    private double grayScaleAvg = 0;
    private double redChannelAvg = 0;
    private double greenChannelAvg = 0;
    private double blueChannelAvg = 0;


    public ImageInformation(Image image) {
        if (image.getWidth() == 0 || image.getHeight() == 0) {
            return;
        }
        Pixel pixel = image.getPixel(0, 0);
        if (pixel instanceof RGBPixel) {
            this.imageType = Image.ImageType.RGB;
        } else {
            this.imageType = Image.ImageType.GRAY_SCALE;
        }
        analyzeImage(image);
    }

    private void analyzeImage(Image image) {
        this.pixelAmount = image.getPixels().size();
        if (this.imageType.equals(Image.ImageType.GRAY_SCALE)) {
            this.analizeGrayScale(image.getPixels());
        } else {
            this.analizeRGB(image.getPixels());
        }
    }

    private void analizeRGB(List<Pixel> pixels) {
        for (Pixel pixel : pixels) {
            this.redChannelAvg += pixel.getRed();
            this.greenChannelAvg += pixel.getGreen();
            this.blueChannelAvg += pixel.getBlue();
        }
        this.redChannelAvg /= this.pixelAmount;
        this.greenChannelAvg /= this.pixelAmount;
        this.blueChannelAvg /= this.pixelAmount;
    }

    private void analizeGrayScale(List<Pixel> pixels) {
        for (Pixel pixel : pixels) {
            this.grayScaleAvg += ((GrayScalePixel) pixel).getGrayScale();
        }
        this.grayScaleAvg /= this.pixelAmount;
    }

    public String toString() {
        if (this.imageType.equals(Image.ImageType.RGB)) {
            return new StringBuilder()
                    .append("Pixels: ")
                    .append(this.pixelAmount)
                    .append("; Red Avg.: ")
                    .append(this.redChannelAvg)
                    .append("; Green Avg.: ")
                    .append(this.greenChannelAvg)
                    .append("; Blue Avg: ")
                    .append(this.blueChannelAvg)
                    .toString();
        } else {
            return new StringBuilder()
                    .append("Pixels: ")
                    .append(this.pixelAmount)
                    .append("; Gray Avg.: ")
                    .append(this.grayScaleAvg)
                    .toString();
        }
    }
}
