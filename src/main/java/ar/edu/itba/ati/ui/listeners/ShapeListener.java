package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;

import java.awt.event.ActionListener;
import java.util.List;

public abstract class ShapeListener implements ActionListener {

    protected int width;
    protected int height;
    protected int size;
    protected List<Pixel> pixels;

    public ShapeListener(int width, int height, int size){

        if (size > width || size > height){
            throw new IllegalArgumentException("Size of shape can't be bigger than image.");
        }

        this.width = width;
        this.height = height;
        this.size = size;
    }

    public void renderShape(){
        Image image = new Image(width, height, pixels);
        ImageAnalyzerFrame.imageContainer.setImage(image);
        ImageAnalyzerFrame.imageContainer.renderImage();
    }
}
