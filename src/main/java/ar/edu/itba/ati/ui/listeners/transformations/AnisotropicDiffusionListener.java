package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.AnisotropicDiffusionDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AnisotropicDiffusionListener extends ATIActionListener {

    public AnisotropicDiffusionListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();

        AnisotropicDiffusionDialog dialog = new AnisotropicDiffusionDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter anisotropic diffusion parameters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.anisotropicDiffusion(dialog.getTime(), dialog.getSigma(),
                    Image.BorderDetectorType.valueOf(dialog.getDetector()));
            FrameHelper.create(newImage);
        }
    }
}
