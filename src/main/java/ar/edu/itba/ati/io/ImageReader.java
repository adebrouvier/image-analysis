package ar.edu.itba.ati.io;

import ar.edu.itba.ati.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ImageReader {

    public static Image readImageFromFile(File file) {
        String filename = file.getName();
        int extensionIndex = filename.lastIndexOf(".");
        String extension = filename.toLowerCase().substring(extensionIndex + 1);
        ImageIO imageIO;

        switch (extension) {
            case "raw": {
                String dataFile = filename.substring(0, extensionIndex) + ".txt";
                try {
                    Scanner sc = new Scanner(new File(dataFile));
                    int width = sc.nextInt();
                    int height = sc.nextInt();
                    imageIO = new RAWImageIO(width, height);
                    break;
                } catch (FileNotFoundException e) {
                    System.err.println("Could not read RAW data");
                    return null;
                }
            }
            case "pbm": {
                imageIO = new PBMImageIO();
                break;
            }
            case "pgm": {
                imageIO = new PGMImageIO();
                break;
            }
            case "ppm": {
                imageIO = new PPMImageIO();
                break;
            }
            case "jpeg":
            case "jpg": {
                imageIO = new JPEGImageIO();
                break;
            }
            case "png": {
                imageIO = new PNGImageIO();
                break;
            }
            default: {
                System.err.println("Wrong file format.");
                return null;
            }
        }

        try {
            return imageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error reading file.");
        }

        return null;
    }
}
