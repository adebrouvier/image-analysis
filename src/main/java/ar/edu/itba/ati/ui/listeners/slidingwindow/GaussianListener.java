package ar.edu.itba.ati.ui.listeners.slidingwindow;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.WindowSizeGaussDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GaussianListener implements ActionListener {

    private WindowContext windowContext;

    public GaussianListener(WindowContext windowContext){
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        WindowSizeGaussDialog dialog = new WindowSizeGaussDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Contrast options", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.gaussMaskFilter(dialog.getStDev(), dialog.getWindowsSize());
            FrameHelper.create(newImage);
        }
    }
}