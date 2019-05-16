package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

public class NextImageActionListener extends ATIActionListener {

    private List<Image> images;
    private Iterator<Image> imageIterator;

    public NextImageActionListener(WindowContext windowContext, List<Image> images) {
        super(windowContext);
        this.images = images;
        this.imageIterator = images.iterator();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (imageIterator.hasNext()){
            getWindowContext().getImageContainer().setImage(imageIterator.next());
            getWindowContext().getImageContainer().renderImage();
        }
    }
}
