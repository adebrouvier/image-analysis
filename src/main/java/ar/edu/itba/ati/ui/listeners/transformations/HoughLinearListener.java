package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;
import ar.edu.itba.ati.ui.dialogs.LinearHoughDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HoughLinearListener implements ActionListener {

    private WindowContext windowContext;

    public HoughLinearListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        LinearHoughDialog dialog = new LinearHoughDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Hough Linear", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.linearHough(
                    dialog.getAngleStepValue(),
                    dialog.getRoStepValue(),
                    dialog.getEpsilonValue(),
                    dialog.getMaximumLinesValue());
            FrameHelper.create(newImage);
        }

    }
}
