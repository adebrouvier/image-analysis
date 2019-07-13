package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.GrayScaleTransformation;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.LicensePlateDetector;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GrayscaleListener extends ATIActionListener {

    public GrayscaleListener(WindowContext windowContext) {
        super(windowContext);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = getWindowContext().getImageContainer().getImage();
        Image newImage = GrayScaleTransformation.apply(image);
        FrameHelper.create(newImage);
    }
}
