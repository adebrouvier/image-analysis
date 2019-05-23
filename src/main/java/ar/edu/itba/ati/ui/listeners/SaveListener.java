package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.io.*;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SaveListener implements ActionListener {

    private WindowContext windowContext;

    public SaveListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showSaveDialog(windowContext.getImageContainer());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            try {
                writeData(filename);
                JOptionPane.showMessageDialog(windowContext.getImageContainer(), "Save successful.");
            } catch (IOException e) {
                System.err.println("Could not save image.");
            }
        }
    }

    private void writeData(String filename) throws IOException {
        Image image = windowContext.getImageContainer().getImage();

        ImageIO writer;

        switch (image.getFormat()) {
            case PBM:
                writer = new PBMImageIO();
                break;
            case PPM:
                writer = new PPMImageIO();
                break;
            case PGM:
                writer = new PGMImageIO();
                break;
            case RAW:
                writer = new RAWImageIO(image.getWidth(), image.getHeight());
                break;
            case JPEG:
                writer = new JPEGImageIO();
                break;
            default:
                throw new IOException("Format not supported");
        }
        writer.write(filename, image);
    }
}
