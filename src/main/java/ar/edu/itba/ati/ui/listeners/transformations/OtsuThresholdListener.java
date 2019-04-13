package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;

public class OtsuThresholdListener extends ATIActionListener{

    public OtsuThresholdListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();
        Image newImage = image.otsuThreshold();
        FrameHelper.create(newImage);
    }
}
