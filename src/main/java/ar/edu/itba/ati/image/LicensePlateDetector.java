package ar.edu.itba.ati.image;

public class LicensePlateDetector {

    public static Image detect(Image image) {

        Image grayscale = GrayScaleTransformation.apply(image);
        Histogram histogram = new Histogram(grayscale, Image.Channel.GRAY);
        Image equalized = histogram.equalize();
        Image sobel = equalized.sobelOperation();

        //OCR.run(image);

        return sobel;
    }
}
