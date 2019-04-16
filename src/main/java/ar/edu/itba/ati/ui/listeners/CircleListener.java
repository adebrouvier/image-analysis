package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Constants;
import ar.edu.itba.ati.image.GrayScalePixel;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CircleListener extends ShapeListener implements ActionListener {

    public CircleListener(WindowContext windowContext, int dim, int diameter) {
        super(windowContext, dim, dim, diameter);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        pixels = new ArrayList<>();

        for (int i = 0; i < width * height; i++) {
            pixels.add(new GrayScalePixel(Constants.BLACK));
        }

        drawCircle(width / 2, height / 2, size);
        renderShape();
    }

    private void drawCircle(int x0, int y0, int radius) {
        int x = radius - 1;
        int y = 0;
        int dx = 1;
        int dy = 1;
        int err = dx - (radius << 1);

        while (x >= y) {
            setPixel(x0 + x, y0 + y);
            setPixel(x0 + y, y0 + x);
            setPixel(x0 - y, y0 + x);
            setPixel(x0 - x, y0 + y);
            setPixel(x0 - x, y0 - y);
            setPixel(x0 - y, y0 - x);
            setPixel(x0 + y, y0 - x);
            setPixel(x0 + x, y0 - y);

            if (err <= 0) {
                y++;
                err += dy;
                dy += 2;
            }

            if (err > 0) {
                x--;
                dx += 2;
                err += dx - (radius << 1);
            }
        }
    }

    private void setPixel(int x, int y) {
        pixels.set(y * width + x, new GrayScalePixel(Constants.WHITE));
    }
}

