package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.readers.*;
import ar.edu.itba.ati.ui.ImageAnalyzer;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class OpenListener implements ActionListener {

    public OpenListener(){ }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showOpenDialog(ImageAnalyzerFrame.imageContainer);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getName();
            int extensionIndex = filename.lastIndexOf(".");
            String extension = filename.toLowerCase().substring(extensionIndex + 1);
            Reader reader = null;

            switch (extension) {
                case "raw": {
                    String dataFile = filename.substring(0, extensionIndex) + ".txt";
                    try {
                        Scanner sc = new Scanner(new File(dataFile));
                        int width = sc.nextInt();
                        int height = sc.nextInt();
                        reader = new RAWReader(width, height);
                        break;
                    } catch (FileNotFoundException e) {
                        System.err.println("Could not read RAW data");
                        System.exit(1);
                    }
                }
                case "pbm": {
                    reader = new PBMReader();
                    break;
                }
                case "pgm": {
                    reader = new PGMReader();
                    break;
                }
                case "ppm": {
                    reader = new PPMReader();
                    break;
                }
                default: {
                    System.err.println("Wrong file format.");
                    System.exit(1);
                }
            }

            try {
                ImageAnalyzerFrame.imageContainer.setImage(reader.read(file));
                ImageAnalyzerFrame.imageContainer.renderImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}