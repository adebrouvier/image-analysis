package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.listeners.ImageMouseListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageContainer extends SingleImagePanel {

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    private BufferedImage bufferedImage;
    private Image image;
    private WindowContext windowContext;

    public ImageContainer(WindowContext windowContext) {
        super();
        this.windowContext = windowContext;
        setOpaque(false);
        addMouseListener(new ImageMouseListener(this.windowContext));
        addMouseMotionListener(new ImageMouseListener(this.windowContext));
    }

    public ImageContainer(WindowContext windowContext, Image image) {
        this(windowContext);
        this.image = image;
        this.renderImage();
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void renderImage() {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        List<Pixel> pixels = image.getPixels();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelIndex = (y * image.getWidth()) + x;
                Pixel p = pixels.get(pixelIndex);
                Color color = new Color(p.getRed(), p.getGreen(), p.getBlue());
                img.setRGB(x, y, color.getRGB());
            }
        }

        this.bufferedImage = img;
        this.setImage(img);
        this.setSize(img.getWidth(), img.getHeight());
        this.repaint();
    }

}
