package ar.edu.itba.ati;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PGMReader implements Reader{

    @Override
    public Image read(File file) throws IOException {
        Scanner sc = new Scanner(new FileInputStream(file));
        String type = sc.nextLine();
        int width = sc.nextInt();
        int height = sc.nextInt();
        int maxGrey = sc.nextInt();
        sc.close();

        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fileInputStream);

        int newLines = 4;
        while (newLines > 0) {

            String s;
            do {
                char c = (char)(dis.readUnsignedByte());
                s = String.valueOf(c);
            } while (!s.matches("\\s"));
            newLines--;
        }

        List<Pixel> pixels = new ArrayList<>();

        for (int i = 0; i < width*height; i++){
            int color = dis.readUnsignedByte();
            pixels.add(new Pixel(color, color, color));
        }

        return new Image(width, height, pixels);
    }
}
