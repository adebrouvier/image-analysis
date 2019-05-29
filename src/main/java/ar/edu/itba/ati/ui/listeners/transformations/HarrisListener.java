package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.HarrisCornerDetector;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;

public class HarrisListener extends ATIActionListener {

    public HarrisListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();
        HarrisCornerDetector harrisCornerDetector = new HarrisCornerDetector();
        FrameHelper.create(harrisCornerDetector.apply(image));
    }
}
