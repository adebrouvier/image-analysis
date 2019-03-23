package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.image.RGBPixel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PPMImageIO extends PNMIO implements ImageIO {

    @Override
    public Image read(File file) throws IOException {

        PPMInfo info = readHeader(file);
        DataInputStream dis = info.getDis();

        List<Pixel> pixels = new ArrayList<>();

        for (int i = 0; i < info.getWidth() * info.getHeight(); i++){
            int r = dis.readUnsignedByte();
            int g = dis.readUnsignedByte();
            int b = dis.readUnsignedByte();
            pixels.add(new RGBPixel(r, g, b));
        }

        return new Image(info.getWidth(), info.getHeight(), pixels, Image.Format.PPM);
    }

    @Override
    public void write(String filename, Image image) throws IOException {

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));

        out.write("P6\n".getBytes());
        out.write(String.valueOf(image.getWidth() + "\n").getBytes());
        out.write(String.valueOf(image.getHeight() + "\n").getBytes());
        out.write(String.valueOf(255 + "\n").getBytes()); //TODO: set max value

        for (Pixel p : image.getPixels()) {
            out.write(p.getRed());
            out.write(p.getGreen());
            out.write(p.getBlue());
        }

        out.close();
    }
}
