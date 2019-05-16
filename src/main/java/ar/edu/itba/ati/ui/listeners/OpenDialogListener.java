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

    public Image chooseAndOpenFile(WindowContext windowContext) {
        JFileChooser fc = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("RAW and PNM Images",
                "raw", "pbm", "pgm", "ppm", "jpeg", "jpg");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(windowContext.getImageContainer());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return ImageReader.readImageFromFile(file);
        }
        return null;
    }
}
