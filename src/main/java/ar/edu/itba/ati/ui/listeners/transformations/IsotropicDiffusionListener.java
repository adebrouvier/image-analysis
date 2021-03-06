package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class IsotropicDiffusionListener extends ATIActionListener {

    public IsotropicDiffusionListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();

        DoubleDialog dialog = new DoubleDialog("Time");
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter max time", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.isotropicDiffusion(dialog.getDoubleValue().intValue());
            FrameHelper.create(newImage);
        }
    }
}
