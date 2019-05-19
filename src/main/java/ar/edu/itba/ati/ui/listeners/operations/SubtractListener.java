package ar.edu.itba.ati.ui.listeners.operations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.listeners.OpenDialogListener;

import java.awt.event.ActionEvent;

public class SubtractListener extends OpenDialogListener {

    private WindowContext windowContext;

    public SubtractListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        Image newImage = this.chooseAndOpenFile(windowContext);

        if (newImage != null) {
            newImage = image.substract(newImage);
            FrameHelper.create(newImage);
        }
    }
}
