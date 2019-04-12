package ar.edu.itba.ati.ui.listeners.selectables;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.ImageInformation;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.MouseEvent;

public class InformationSelectable implements Selectable {

    private int x;
    private int y;
    private WindowContext windowContext;

    public InformationSelectable(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void onMousePressed(MouseEvent mouseEvent) {
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
    }

    @Override
    public void onMouseReleased(MouseEvent mouseEvent) {
        Image image = this.windowContext.getImageContainer().getImage();
        Image subImage = image.getSubimage(this.x, this.y, mouseEvent.getX(), mouseEvent.getY());

        this.windowContext.getInformationLabel().setText(new ImageInformation(subImage).toString());
    }
}
