package ar.edu.itba.ati.ui.listeners.slidingwindow;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EdgeEnhancementListener implements ActionListener {

    private WindowContext windowContext;

    public EdgeEnhancementListener(WindowContext windowContext){
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        Image newImage = image.highPassFilter();
        FrameHelper.create(newImage);
    }
}