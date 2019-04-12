package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.ThresholdDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThresholdListener implements ActionListener {

    private WindowContext windowContext;

    public ThresholdListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        ThresholdDialog dialog = new ThresholdDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Threshold", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.threshold(dialog.getThreshold().doubleValue());
            FrameHelper.create(newImage);
        }
    }
}
