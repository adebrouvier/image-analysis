package ar.edu.itba.ati.ui.listeners.transformations.activecontour;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.listeners.transformations.ATIActionListener;

import java.awt.event.ActionEvent;
import java.util.ListIterator;

public class NextImageActionListener extends ATIActionListener {

    private ListIterator<Image> imageIterator;

    public NextImageActionListener(WindowContext windowContext, ListIterator<Image> imageIterator) {
        super(windowContext);
        this.imageIterator = imageIterator;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (imageIterator.hasNext()) {
            getWindowContext().getImageContainer().setImage(imageIterator.next());
            getWindowContext().getImageContainer().renderImage();
        }
    }
}
