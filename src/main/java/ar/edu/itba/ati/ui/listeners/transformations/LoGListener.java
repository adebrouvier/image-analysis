package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.LoGDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoGListener extends ATIActionListener {

    public LoGListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();

        LoGDialog dialog = new LoGDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter LoG options", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.logOperator(dialog.getSigma(), dialog.getThreshold());
            FrameHelper.create(newImage);
        }
    }
}
