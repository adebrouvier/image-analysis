package ar.edu.itba.ati.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageUtils {

    public static BufferedImage ImageToBufferedImage(Image image){
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        List<Pixel> pixels = image.getPixels();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelIndex = (y * image.getWidth()) + x;
                Pixel p = pixels.get(pixelIndex);
                Color color = new Color(p.getRed(), p.getGreen(), p.getBlue());
                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }
}
