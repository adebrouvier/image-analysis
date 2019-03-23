package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.image.RGBPixel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RAWImageIO implements ImageIO {

    private int width;
    private int height;

    public RAWImageIO(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Image read(File file) throws IOException {

        FileInputStream stream = new FileInputStream(file);

        byte[] bytes = new byte[width*height];

        int read = stream.read(bytes);
        List<Pixel> pixels = new ArrayList<>();

        for (byte b : bytes){
            pixels.add(new RGBPixel(b & 0xff, b & 0xff, b & 0xff)); // Unsigned
        }

        return new Image(width, height, pixels, Image.Format.RAW);
    }

    @Override
    public void write(String filename, Image image) throws IOException {

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));

        for (Pixel p : image.getPixels()) {
            GrayScalePixel gp = (GrayScalePixel) p;
            out.write(gp.getGrayScale());
        }

        out.close();
    }
}
