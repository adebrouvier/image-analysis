package ar.edu.itba.ati.ui.listeners.clickables;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.image.RGBPixel;
import ar.edu.itba.ati.ui.MouseOptions;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.MouseEvent;

public class PaintClickable implements Clickable {

    private int x;
    private int y;
    private WindowContext windowContext;

    public PaintClickable(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void onMouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        Image image = this.windowContext.getImageContainer().getImage();
        MouseOptions mouseOptions = this.windowContext.getMouseOptions();
        Pixel p = image.getPixel(x, y);
        Pixel newPixel;
        if (p instanceof RGBPixel){
            newPixel = new RGBPixel(mouseOptions.getRed(), mouseOptions.getGreen(), mouseOptions.getBlue());
        } else {
            newPixel = new GrayScalePixel(mouseOptions.getGray());
        }
        image.changePixel(x, y, newPixel);
        this.windowContext.getImageContainer().renderImage();
    }

}
