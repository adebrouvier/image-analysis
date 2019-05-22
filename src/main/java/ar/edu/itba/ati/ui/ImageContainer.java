package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.ImageUtils;
import ar.edu.itba.ati.ui.listeners.ImageMouseListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ImageContainer extends SingleImagePanel {

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    private BufferedImage bufferedImage;
    private Image image;
    private WindowContext windowContext;
    private MouseEvent startSelection;
    private MouseEvent currentSelection;

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
        BufferedImage bufferedImage = ImageUtils.ImageToBufferedImage(image);
        this.bufferedImage = bufferedImage;
        this.setImage(bufferedImage);
        this.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bufferedImage != null) {
            g.drawImage(bufferedImage, 0, 0, this);
        }

        if (currentSelection != null && startSelection != null) {
            g.setColor(Color.RED);
            int x = Math.min(startSelection.getX(), currentSelection.getX());
            int y = Math.min(startSelection.getY(), currentSelection.getY());
            int width = Math.abs(startSelection.getX() - currentSelection.getX());
            int height = Math.abs(startSelection.getY() - currentSelection.getY());
            g.drawRect(x, y, width, height);
        }
    }

    public void setStartSelection(MouseEvent startSelection) {
        this.startSelection = startSelection;
    }

    public void setCurrentSelection(MouseEvent currentSelection) {
        this.currentSelection = currentSelection;
    }

    public void resetSelection() {
        this.startSelection = null;
        this.currentSelection = null;
    }
}
