package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;

import javax.swing.*;

public class FrameHelper {

    public static ImageAnalyzerFrame create(Image newImage) {
        ImageAnalyzerFrame frame = new ImageAnalyzerFrame(newImage);
        frame.setSize(newImage.getWidth() + 300, newImage.getHeight() + 100);
        frame.setVisible(true);
        return frame;
    }
}
