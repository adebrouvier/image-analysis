package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RayleighNoiseListener implements ActionListener {

    private WindowContext windowContext;

    public RayleighNoiseListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        image.addRayleighNoise(0.5, 1);
        windowContext.getImageContainer().renderImage();
    }
}
