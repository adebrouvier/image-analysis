package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.LicensePlateDetector;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.listeners.transformations.ATIActionListener;

import java.awt.event.ActionEvent;

public class LicensePlateDetectorListener extends ATIActionListener {

    public LicensePlateDetectorListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();
        Image newImage = LicensePlateDetector.detect(image);
        FrameHelper.create(newImage);
    }
}
