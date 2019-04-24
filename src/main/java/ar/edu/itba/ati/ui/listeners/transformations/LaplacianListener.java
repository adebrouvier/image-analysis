package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LaplacianListener extends ATIActionListener {

    public LaplacianListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();

        DoubleDialog dialog = new DoubleDialog("Threshold");
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter slope threshold", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.laplacianOperator(dialog.getDoubleValue().intValue());
            FrameHelper.create(newImage);
        }
    }
}
