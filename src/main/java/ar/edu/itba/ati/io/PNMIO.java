package ar.edu.itba.ati.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public abstract class PNMIO {

    public enum MagicNumber {
        P1, P2, P3, P4, P5, P6
    }

    public PPMInfo readHeader(File file) throws IOException {
        Scanner sc = new Scanner(new FileInputStream(file));
        MagicNumber magicNumber = MagicNumber.valueOf(sc.next());
        int width;
        int height;
        int maxColor = 0;

        //Skip comment if present
        if (sc.hasNext("#")) {
            sc.nextLine(); //TODO: Doesn't work for all comments
            sc.nextLine();
        }

        width = sc.nextInt();
        height = sc.nextInt();
        if (!magicNumber.equals(MagicNumber.P4))
            maxColor = sc.nextInt();

        sc.close();

        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fileInputStream);

        //TODO: Obtain number of new lines in some other way
        int newLines = magicNumber.equals(MagicNumber.P4) ? 3 : 4;
        while (newLines > 0) {

            String s;
            do {
                char c = (char) (dis.readUnsignedByte());
                s = String.valueOf(c);
            } while (!s.matches("\\s"));
            newLines--;
        }

        return new PPMInfo(dis, width, height);
    }


}
