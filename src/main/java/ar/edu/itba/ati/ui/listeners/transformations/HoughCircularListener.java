package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.CircularHoughDialog;
import ar.edu.itba.ati.ui.listeners.CircleListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HoughCircularListener implements ActionListener {

    private WindowContext windowContext;

    public HoughCircularListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        CircularHoughDialog dialog = new CircularHoughDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Hough Circular", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.circularHough(
                    dialog.getXStepValue(),
                    dialog.getYStepValue(),
                    dialog.getRadiusStepValue(),
                    dialog.getEpsilonValue(),
                    dialog.getMaximumCirclesValue());
            FrameHelper.create(newImage);
        }
    }
}
