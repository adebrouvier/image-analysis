package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.BilateralFilterDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BilateralFilterListener extends ATIActionListener {

    public BilateralFilterListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();

        BilateralFilterDialog dialog = new BilateralFilterDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please input the data", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.bilateralFilter(dialog.getMaskSize(), dialog.getFirstValue(), dialog.getSecondValue());
            FrameHelper.create(newImage);
        }
    }
}
