package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionListener;
import java.util.List;

public abstract class ShapeListener implements ActionListener {

    protected int width;
    protected int height;
    protected int size;
    protected List<Pixel> pixels;
    private WindowContext windowContext;

    public ShapeListener(WindowContext windowContext, int width, int height, int size){
        this.windowContext = windowContext;

        if (size > width || size > height){
            throw new IllegalArgumentException("Size of shape can't be bigger than image.");
        }

        this.width = width;
        this.height = height;
        this.size = size;
    }

    public void renderShape(){
        Image image = new Image(width, height, pixels, Image.Format.PBM);
        this.windowContext.getImageContainer().setImage(image);
        this.windowContext.getImageContainer().renderImage();
    }
}
