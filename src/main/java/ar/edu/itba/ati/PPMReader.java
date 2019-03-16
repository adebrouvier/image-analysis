package ar.edu.itba.ati;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PPMReader extends PNMReader implements Reader {
    @Override
    public Image read(File file) throws IOException {

        PPMReaderInfo info = readHeader(file);
        DataInputStream dis = info.getDis();

        List<Pixel> pixels = new ArrayList<>();

        for (int i = 0; i < info.getWidth() * info.getHeight(); i++){
            int r = dis.readUnsignedByte();
            int g = dis.readUnsignedByte();
            int b = dis.readUnsignedByte();
            pixels.add(new Pixel(r, g, b));
        }

        return new Image(info.getWidth(), info.getHeight(), pixels);
    }
}
