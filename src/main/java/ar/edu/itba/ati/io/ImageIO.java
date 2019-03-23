package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.Image;

import java.io.File;
import java.io.IOException;

public interface ImageIO {

    Image read(File file) throws IOException;

    void write(String filename, Image image) throws IOException;

}
