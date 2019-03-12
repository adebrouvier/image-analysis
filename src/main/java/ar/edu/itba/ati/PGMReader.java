package ar.edu.itba.ati;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        return null;
    }
}
