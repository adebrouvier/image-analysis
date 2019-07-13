package ar.edu.itba.ati.image;

import java.util.ArrayList;
import java.util.List;

public class GrayScaleTransformation {

    public static Image apply(Image image) {

        List<Pixel> newPixels = new ArrayList<>();

        for (Pixel p : image.getPixels()) {
            GrayScalePixel gp = new GrayScalePixel(grayValue(p));
            newPixels.add(gp);
        }

        return new Image(image.getWidth(), image.getHeight(), newPixels, Image.ImageType.GRAY_SCALE, Image.Format.PGM);
    }

    private static int grayValue(Pixel p) {
        return (int) ((p.getRed() + p.getGreen() + p.getBlue()) / 3.0);
    }
}
