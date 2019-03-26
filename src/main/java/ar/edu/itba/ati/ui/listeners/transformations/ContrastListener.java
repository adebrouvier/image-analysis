package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContrastListener implements ActionListener {

    private WindowContext windowContext;
    public ContrastListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Image image = windowContext.getImageContainer().getImage();
        image.increaseContrast();
        windowContext.getImageContainer().renderImage();
    }
}
