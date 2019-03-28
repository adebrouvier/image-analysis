package ar.edu.itba.ati.ui.listeners.operations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;
import ar.edu.itba.ati.ui.listeners.OpenDialogListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdditionListener extends OpenDialogListener {

    private WindowContext windowContext;

    public AdditionListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        Image newImage = this.getImageFromFile(windowContext);

        if (newImage != null) {
            newImage = image.add(newImage);
            FrameHelper.create(newImage);
        }
    }
}
