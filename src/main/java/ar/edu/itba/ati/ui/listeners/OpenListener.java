package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;

public class OpenListener extends OpenDialogListener {

    private WindowContext windowContext;

    public OpenListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = chooseAndOpenFile(windowContext);
        if (image != null) {
            windowContext.getImageContainer().setImage(image);
            windowContext.getImageContainer().renderImage();
        }
    }
}