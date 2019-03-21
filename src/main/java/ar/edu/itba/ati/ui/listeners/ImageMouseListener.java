package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageMouseListener extends MouseAdapter {

    public ImageMouseListener() {
        super();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        Pixel p = ImageAnalyzerFrame.imageContainer.getImage().getPixel(x, y);
        ImageAnalyzerFrame.informationLabel.setText(
                new StringBuilder()
                        .append(p)
                        .append("; X: ")
                        .append(x)
                        .append("; Y: ")
                        .append(y)
                        .append(".").toString()
        );
    }

//    @Override
//    public void mousePressed(MouseEvent mouseEvent) {
//        super.mousePressed(mouseEvent);
//        areaSelector.setStart(mouseEvent.getX(), mouseEvent.getY());
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent mouseEvent) {
//        super.mouseReleased(mouseEvent);
//        areaSelector.setEnd(mouseEvent.getX(), mouseEvent.getY());
//        contentPane.repaint();
//    }
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
