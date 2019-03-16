package ar.edu.itba.ati;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public abstract class PNMReader{

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
        maxColor = sc.nextInt();

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

        return new PPMReaderInfo(dis, width, height);
    }


}
