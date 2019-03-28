package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;

import javax.swing.*;

public class FrameHelper {

    public static void create(Image newImage) {
        JFrame frame = new ImageAnalyzerFrame(newImage);
        frame.setSize(newImage.getWidth() + 300, newImage.getHeight() + 100);
        frame.setVisible(true);
    }
}
