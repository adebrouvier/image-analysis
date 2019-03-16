package ar.edu.itba.ati;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PGMReader extends PNMReader implements Reader{

    @Override
    public Image read(File file) throws IOException {
        PPMReaderInfo info = readHeader(file);

        List<Pixel> pixels = new ArrayList<>();

        for (int i = 0; i < info.getWidth()*info.getHeight(); i++){
            int color = info.getDis().readUnsignedByte();
            pixels.add(new Pixel(color, color, color));
        }

        return new Image(info.getWidth(), info.getHeight(), pixels);
    }
}
