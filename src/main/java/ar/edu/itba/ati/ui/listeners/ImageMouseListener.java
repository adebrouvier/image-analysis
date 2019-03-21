package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;
import ar.edu.itba.ati.ui.OptionMenu;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageMouseListener extends MouseAdapter {

    private WindowContext windowContext;

    public ImageMouseListener(WindowContext windowContext){
        this.windowContext = windowContext;
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);
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

        if (this.windowContext.getOptionMenu().getSelectable() != null) {
            this.windowContext.getOptionMenu().setSelectable(null);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        if (this.windowContext.getOptionMenu().getSelectable() != null){
            this.windowContext.getOptionMenu().getSelectable().onMousePressed(mouseEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
        if (this.windowContext.getOptionMenu().getSelectable() != null){
            this.windowContext.getOptionMenu().getSelectable().onMouseReleased(mouseEvent);
            this.windowContext.getOptionMenu().setSelectable(null);
        }
    }
//
//    @Override
//    public void mouseMoved(MouseEvent mouseEvent) {
//        int x = mouseEvent.getX();
//        int y = mouseEvent.getY();
//        int red = 0;
//        int green = 0;
//        int blue = 0;
//        if (renderedImage != null) {
//            if (x < renderedImage.getWidth() && y < renderedImage.getHeight()) {
//                int rgb = renderedImage.getRGB(x, y);
//                red = (rgb >> 16) & 0xFF;
//                green = (rgb >> 8) & 0xFF;
//                blue = rgb & 0xFF;
//            }
//        }
//        pixelColor.setText("R: " + red + " G: " + green + " B: " + blue);
//    }
}
