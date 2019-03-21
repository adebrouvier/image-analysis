package ar.edu.itba.ati.readers;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PGMReader extends PNMReader implements Reader {

    @Override
    public Image read(File file) throws IOException {
        PPMReaderInfo info = readHeader(file);

        List<Pixel> pixels = new ArrayList<>();

        for (int i = 0; i < info.getWidth()*info.getHeight(); i++){
            int grayScale = info.getDis().readUnsignedByte();
            pixels.add(new GrayScalePixel(grayScale));
        }

        return new Image(info.getWidth(), info.getHeight(), pixels);
    }
}
