package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.MaskRotator;
import ar.edu.itba.ati.image.Pixel;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.SingleImagePanel;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MaskListener implements ActionListener {

    private WindowContext windowContext;
    private Double[] mask;
    private Integer maskSize;
    private String title;

    public MaskListener(WindowContext windowContext, Double[] mask, Integer maskSize, String title) {
        this.windowContext = windowContext;
        this.mask = mask;
        this.maskSize = maskSize;
        this.title = title;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        LinkedList<Image> images = new LinkedList<Image>();
        images.add(image);
        Image newImage = image.applyMask(this.maskSize, (pixels) -> image.getWeightedValue(pixels, this.mask));
        images.add(newImage);
        Double[] newMask = mask;
        for (int i = 0; i < 7; i++) {
            newMask = MaskRotator.rotate3x3Mask(newMask);
            Double[] finalNewMask = newMask;
            newImage = image.applyMask(this.maskSize, (pixels) -> image.getWeightedValue(pixels, finalNewMask));
            images.add(newImage);
        }
        List<java.awt.Image> printableImgs = images.stream().map((img) -> getPrintableImage(img)).collect(Collectors.toList());
        createFrame(printableImgs);
    }

    private void createFrame(List<java.awt.Image> imageList) {
        JFrame rgbFrame = new JFrame(this.title);
        rgbFrame.setLayout(new GridLayout(3, 3));
        imageList.forEach(c -> rgbFrame.add(new SingleImagePanel(c)));
        rgbFrame.setVisible(true);
        rgbFrame.setSize(1000, 800);
    }

    private java.awt.Image getPrintableImage(Image image) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        java.util.List<Pixel> pixels = image.getPixels();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelIndex = (y * image.getWidth()) + x;
                Pixel p = pixels.get(pixelIndex);
                Color color = new Color(p.getRed(), p.getGreen(), p.getBlue());
                img.setRGB(x, y, color.getRGB());
            }
        }
        return img;
    }
}
