package ar.edu.itba.ati.ui.listeners.slidingwindow;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.WindowSizeDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MeanListener implements ActionListener {

    private WindowContext windowContext;

    public MeanListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        WindowSizeDialog dialog = new WindowSizeDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Contrast options", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.meanFilter(dialog.getWindowsSize());
            FrameHelper.create(newImage);
        }
    }
}