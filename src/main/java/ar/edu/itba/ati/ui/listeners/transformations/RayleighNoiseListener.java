package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.RayleighNoiseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RayleighNoiseListener implements ActionListener {

    private WindowContext windowContext;

    public RayleighNoiseListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        RayleighNoiseDialog dialog = new RayleighNoiseDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Rayleigh Noise options", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.addRayleighNoise(dialog.getPercentage(), dialog.getPhi());
            FrameHelper.create(newImage);
        }
    }
}
