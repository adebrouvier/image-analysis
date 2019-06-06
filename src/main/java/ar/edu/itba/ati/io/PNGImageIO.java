package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.image.RGBPixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PNGImageIO implements ImageIO {
    @Override
    public Image read(File file) throws IOException {
        BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);

        List<Pixel> pixels = new ArrayList<>();

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++){
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb);
                pixels.add(new RGBPixel(color.getRed(), color.getGreen(), color.getBlue()));
            }
        }

        return new Image(bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, Image.Format.PNG, file);
    }

    @Override
    public void write(String filename, Image image) throws IOException {
        //TODO
    }
}
