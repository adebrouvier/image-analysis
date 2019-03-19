package ar.edu.itba.ati;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PBMReader extends PPMReader implements Reader {

    @Override
    public Image read(File file) throws IOException {
        PPMReaderInfo info = readHeader(file);

        List<Pixel> pixels = new ArrayList<>();
        DataInputStream dis = info.getDis();

        for (int i = 0; i < info.getHeight(); i++) {
            int rowPixels = 0;
            for (int k = 0; k < Math.ceil(info.getWidth()/8.0); k++) {
                int imageByte = dis.readUnsignedByte();
                for (int j = 0; j < 8; j++) {
                    if (rowPixels < info.getWidth()) {
                        int bit = getBit(imageByte, 7 - j);
                        int color = bit > 0 ? PPMReader.BLACK : PPMReader.WHITE;
                        pixels.add(new GrayScalePixel(color));
                        rowPixels++;
                    }else{
                        break;
                    }
                }
            }
        }
        return new Image(info.getWidth(), info.getHeight(), pixels);
    }

    private static int getBit(int n, int k) {
        return (n >> k) & 1;
    }

}
