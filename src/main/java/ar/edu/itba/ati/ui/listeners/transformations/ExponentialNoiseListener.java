package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.ExponentialNoiseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExponentialNoiseListener implements ActionListener {

    private WindowContext windowContext;

    public ExponentialNoiseListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        ExponentialNoiseDialog dialog = new ExponentialNoiseDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Exponential Noise options", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.addExponentialNoise(dialog.getPercentage(), dialog.getLambda());
            FrameHelper.create(newImage);
        }
    }
}
