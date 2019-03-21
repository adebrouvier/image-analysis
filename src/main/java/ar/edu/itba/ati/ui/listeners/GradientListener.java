package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.ImageAnalyzer;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GradientListener implements ActionListener {

    private Color[] colors;
    private Image.ImageType imageType;

    public GradientListener(Color[] colors, Image.ImageType imageType) {
        this.colors = colors;
        this.imageType = imageType;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        BufferedImage renderedImage = ImageAnalyzerFrame.imageContainer.getBufferedImage();
        if (renderedImage != null) {
            Graphics2D g2d = renderedImage.createGraphics();
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(renderedImage.getWidth(), renderedImage.getWidth());
            float[] dist = {0.0f, 0.33f, 0.66f};
            LinearGradientPaint p = new LinearGradientPaint(start, end, dist, colors);
            g2d.setPaint(p);
            g2d.fillRect(0, 0, renderedImage.getWidth(), renderedImage.getHeight());
            g2d.dispose();
            ImageAnalyzerFrame.imageContainer.setImage(new Image(renderedImage, imageType));
            ImageAnalyzerFrame.imageContainer.renderImage();
        }
    }
}
