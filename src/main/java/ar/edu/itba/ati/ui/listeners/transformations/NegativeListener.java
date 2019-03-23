package ar.edu.itba.ati.ui.listeners.transformations;

import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.image.RGBPixel;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NegativeListener implements ActionListener {

    private WindowContext windowContext;
    private static int COLORS = 256;

    public NegativeListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Pixel p = image.getPixel(x, y);
                Pixel newPixel = null;
                if (image.getType().equals(Image.ImageType.RGB)) {
                    newPixel = new RGBPixel(negative(p.getRed()), negative(p.getGreen()), negative(p.getBlue()));
                }else {
                    GrayScalePixel g = (GrayScalePixel) p;
                    newPixel = new GrayScalePixel(negative(g.getGrayScale()));
                }
                image.changePixel(x, y, newPixel);
            }
        }
        windowContext.getImageContainer().renderImage();
    }

    private int negative(int component){
        return -component + COLORS - 1;
    }
}
