package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.io.*;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class OpenDialogListener implements ActionListener {

    public Image getImageFromFile(WindowContext windowContext) {
        JFileChooser fc = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("RAW and PNM Images",
                "raw", "pbm", "pgm", "ppm");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(windowContext.getImageContainer());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
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
                        System.exit(1);
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
                default: {
                    System.err.println("Wrong file format.");
                    return null;
                }
            }

            try {
                return imageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
