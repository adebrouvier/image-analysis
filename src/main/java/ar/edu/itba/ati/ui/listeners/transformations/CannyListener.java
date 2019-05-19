package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.CannyDialog;
import ar.edu.itba.ati.ui.dialogs.SaltAndPepperNoiseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CannyListener implements ActionListener {

    private WindowContext windowContext;

    public CannyListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        CannyDialog dialog = new CannyDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter thresholds for Canny", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.cannyBorderDetector(dialog.getFirstValue(), dialog.getSecondValue());
            FrameHelper.create(newImage);
        }
    }
}
