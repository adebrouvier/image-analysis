package ar.edu.itba.ati.ui.listeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveListener implements ActionListener {

    private JPanel panel;

    public SaveListener(JPanel contentPane) {
        panel = contentPane;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showSaveDialog(panel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            //TODO: Save image
        }
    }
}
