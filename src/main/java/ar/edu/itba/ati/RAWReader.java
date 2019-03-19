package ar.edu.itba.ati;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.image.RGBPixel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RAWReader implements Reader{

    private int width;
    private int height;

    public RAWReader(int width, int height) {
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

        return new Image(width, height, pixels);
    }
}
