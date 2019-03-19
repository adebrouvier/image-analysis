package ar.edu.itba.ati;

import ar.edu.itba.ati.image.Image;

import java.io.File;
import java.io.IOException;

public interface Reader {

    public Image read(File file) throws IOException;

}
