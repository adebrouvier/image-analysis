package ar.edu.itba.ati.ui.listeners.clickables;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.ImageInformation;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.listeners.selectables.Selectable;

import java.awt.event.MouseEvent;

public class InformationClickable implements Clickable {

    private int x;
    private int y;
    private WindowContext windowContext;

    public InformationClickable(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void onMouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        Pixel p = this.windowContext.getImageContainer().getImage().getPixel(x, y);
        this.windowContext.getInformationLabel().setText(
                new StringBuilder()
                        .append(p)
                        .append("; X: ")
                        .append(x)
                        .append("; Y: ")
                        .append(y)
                        .append(".").toString()
        );
    }

}
