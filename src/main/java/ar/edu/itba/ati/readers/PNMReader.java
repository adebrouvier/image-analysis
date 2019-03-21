package ar.edu.itba.ati.readers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public abstract class PNMReader{

    private enum MagicNumber {
        P1, P2, P3, P4, P5, P6
    }

    static final int BLACK = 0;
    static final int WHITE = 255;

    public PPMReaderInfo readHeader(File file) throws IOException {
        Scanner sc = new Scanner(new FileInputStream(file));
        String type = sc.next();
        int width = 0;
        int height = 0;
        int maxColor = 0;

        //Skip comment if present
        if (sc.hasNext("#")){
            sc.nextLine(); //TODO: Doesn't work for all comments
            sc.nextLine();
        }

        width = sc.nextInt();
        height = sc.nextInt();
        if (!type.equals(MagicNumber.P4.toString()))
            maxColor = sc.nextInt();

        sc.close();

        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fileInputStream);

        //TODO: Obtain number of new lines in some other way
        int newLines = type.equals(MagicNumber.P4.toString()) ? 3 : 4;
        while (newLines > 0) {

            String s;
            do {
                char c = (char)(dis.readUnsignedByte());
                s = String.valueOf(c);
            } while (!s.matches("\\s"));
            newLines--;
        }

        return new PPMReaderInfo(dis, width, height);
    }


}
