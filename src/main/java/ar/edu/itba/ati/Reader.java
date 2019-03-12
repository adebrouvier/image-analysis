package ar.edu.itba.ati;

import java.io.File;
import java.io.IOException;

public interface Reader {

    public Image read(File file) throws IOException;

}
