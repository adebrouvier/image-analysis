package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.Constants;
import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PBMImageIO extends PPMImageIO implements ImageIO {

    @Override
    public Image read(File file) throws IOException {
        PPMInfo info = readHeader(file);

        List<Pixel> pixels = new ArrayList<>();
        DataInputStream dis = info.getDis();

        for (int i = 0; i < info.getHeight(); i++) {
            int rowPixels = 0;
            for (int k = 0; k < Math.ceil(info.getWidth() / 8.0); k++) {
                int imageByte = dis.readUnsignedByte();
                for (int j = 0; j < 8; j++) {
                    if (rowPixels < info.getWidth()) {
                        int bit = getBit(imageByte, 7 - j);
                        int color = bit > 0 ? Constants.BLACK : Constants.WHITE;
                        pixels.add(new GrayScalePixel(color));
                        rowPixels++;
                    } else {
                        break;
                    }
                }
            }
        }
        return new Image(info.getWidth(), info.getHeight(), pixels, Image.Format.PBM);
    }

    private static int getBit(int n, int k) {
        return (n >> k) & 1;
    }

    @Override
    public void write(String filename, Image image) throws IOException {

        /*DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));

        out.write("P4\n".getBytes());
        out.write(String.valueOf(image.getWidth() + "\n").getBytes());
        out.write(String.valueOf(image.getHeight() + "\n").getBytes());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                GrayScalePixel pixel = (GrayScalePixel) image.getPixel(x, y);
                int value = pixel.getGrayScale();

            }
        }*/
        throw new NotImplementedException();
    }

}
