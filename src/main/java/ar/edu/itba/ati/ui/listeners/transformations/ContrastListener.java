package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.ContrastDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContrastListener implements ActionListener {

    private WindowContext windowContext;
    public ContrastListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Image image = windowContext.getImageContainer().getImage();
        int mean = image.getGrayScaleMean();
        int stDev = image.getGrayScaleStDev();
        int r1 = mean - stDev;
        int r2 = mean + stDev;
        ContrastDialog dialog = new ContrastDialog(r1, r2);
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Contrast options", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            image.increaseContrast(r1, r2, dialog.getS1(), dialog.getS2());
            windowContext.getImageContainer().renderImage();
        }
    }
}
