package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PGMImageIO extends PNMIO implements ImageIO {

    @Override
    public Image read(File file) throws IOException {
        PPMInfo info = readHeader(file);

        List<Pixel> pixels = new ArrayList<>();

        for (int i = 0; i < info.getWidth()*info.getHeight(); i++){
            int grayScale = info.getDis().readUnsignedByte();
            pixels.add(new GrayScalePixel(grayScale));
        }

        return new Image(info.getWidth(), info.getHeight(), pixels, Image.Format.PGM);
    }

    @Override
    public void write(String filename, Image image) throws IOException {

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));

        out.write("P5\n".getBytes());
        out.write(String.valueOf(image.getWidth() + "\n").getBytes());
        out.write(String.valueOf(image.getHeight() + "\n").getBytes());
        out.write(String.valueOf(255 + "\n").getBytes());

        for (Pixel p : image.getPixels()) {
            GrayScalePixel gp = (GrayScalePixel) p;
            out.write(gp.getGrayScale());
        }

        out.close();
    }
}
