package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Constants;
import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EmptySquareListener extends ShapeListener implements ActionListener {

    public EmptySquareListener(WindowContext windowContext, int dim, int size) {
        super(windowContext, dim, dim, size);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        pixels = new ArrayList<>();

        for (int i = 0; i < width * height; i++) {
            pixels.add(new GrayScalePixel(Constants.BLACK));
        }

        int startX = width / 2 - size;
        int startY = height / 2 - size;

        for (int y = startY; y < height / 2 + size; y++) {
            for (int x = startX; x < width / 2 + size; x++) {
                if ( ((y == startY || y == ((height / 2) + size - 1)) && (x < (width / 2) + size))
                   || x == startX || x == (width/2 + size - 1)){
                    pixels.set(y * width + x, new GrayScalePixel(Constants.WHITE));
                }
            }
        }

        renderShape();
    }

}

