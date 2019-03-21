package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.ui.ImageAnalyzerFrame;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveListener implements ActionListener {

    private WindowContext windowContext;

    public SaveListener(WindowContext windowContext){
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showSaveDialog(windowContext.getImageContainer());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            //TODO: Save image
        }
    }
}
