package ar.edu.itba.ati.ui.listeners.operations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultiplyListener implements ActionListener {

    private WindowContext windowContext;

    public MultiplyListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        DoubleDialog dialog = new DoubleDialog("Multiply by");
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter value", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.multiply(dialog.getDoubleValue());
            FrameHelper.create(newImage);
        }
    }
}
