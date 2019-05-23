package ar.edu.itba.ati.ui.listeners.transformations.activecontour;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.ImageContainer;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.listeners.transformations.ATIActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ListIterator;

public class PlayActionListener extends ATIActionListener {

    private ListIterator<Image> imageIterator;
    private Timer timer;

    public PlayActionListener(WindowContext windowContext, ListIterator<Image> imageIterator) {
        super(windowContext);
        this.imageIterator = imageIterator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        ImageContainer container = getWindowContext().getImageContainer();

        timer = new Timer(100, e -> {

            if (imageIterator.hasNext()) {
                container.setImage(imageIterator.next());
                container.renderImage();
            } else {
                timer.stop();
            }
        });

        timer.start();
    }
}
