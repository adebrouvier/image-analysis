package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.SaltAndPepperNoiseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaltAndPepperNoiseListener implements ActionListener {

    private WindowContext windowContext;

    public SaltAndPepperNoiseListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        SaltAndPepperNoiseDialog dialog = new SaltAndPepperNoiseDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Salt and Pepeer options", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.saltAndPepperNoise(dialog.getMinValue(), dialog.getMaxValue());
            FrameHelper.create(newImage);
        }
    }
}
