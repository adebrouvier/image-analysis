package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.GaussianNoiseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GaussianNoiseListener implements ActionListener {

    private WindowContext windowContext;

    public GaussianNoiseListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        GaussianNoiseDialog dialog = new GaussianNoiseDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Gaussian Noise options", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.addGaussianNoise(dialog.getPercentage(), dialog.getMean(), dialog.getStDev());
            FrameHelper.create(newImage);
        }
    }
}
