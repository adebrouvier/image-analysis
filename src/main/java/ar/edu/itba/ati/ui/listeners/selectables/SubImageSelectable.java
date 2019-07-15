package ar.edu.itba.ati.ui.listeners.selectables;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class SubImageSelectable implements Selectable {

    private int x;
    private int y;
    private WindowContext windowContext;

    public SubImageSelectable(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void onMousePressed(MouseEvent mouseEvent) {
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
        this.windowContext.getImageContainer().setStartSelection(mouseEvent);
    }

    @Override
    public void onMouseReleased(MouseEvent mouseEvent) {
        this.windowContext.getImageContainer().resetSelection();
        this.windowContext.getImageContainer().repaint();

        Image image = this.windowContext.getImageContainer().getImage();
        Image subImage = image.getSubimage(this.x, this.y, mouseEvent.getX(), mouseEvent.getY());

        JFrame frame = new ImageAnalyzerFrame(subImage);
        frame.setVisible(true);
        frame.setSize(subImage.getWidth(), subImage.getHeight());
    }

    @Override
    public void onMouseDragged(MouseEvent mouseEvent) {
        this.windowContext.getImageContainer().setCurrentSelection(mouseEvent);
        this.windowContext.getImageContainer().repaint();
    }
}
