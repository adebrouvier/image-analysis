package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.ui.SingleImagePanel;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RGBHSVDecomposition implements ActionListener {

    private WindowContext windowContext;

    public RGBHSVDecomposition(WindowContext windowContext){
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        BufferedImage renderedImage = windowContext.getImageContainer().getBufferedImage();

        List<BufferedImage> rgbChannels = initImages(renderedImage);
        List<BufferedImage> hsvChannels = initImages(renderedImage);

        /* Split each pixel on RGB and HSV components */
        for (int y = 0; y < renderedImage.getHeight(); y++) {
            for (int x = 0; x < renderedImage.getWidth(); x++) {
                Color rgbColor = new Color(renderedImage.getRGB(x, y));
                splitRGB(rgbChannels, x, y, rgbColor);
                splitHSV(hsvChannels, x, y, rgbColor);
            }
        }
        rgbChannels.addAll(hsvChannels);
        List<java.awt.Image> scaled = rgbChannels.stream()
                .map(i -> i.getScaledInstance(300,300, java.awt.Image.SCALE_SMOOTH))
                .collect(Collectors.toList());
        createFrame(scaled);
    }

    private List<BufferedImage> initImages(BufferedImage renderedImage) {
        List<BufferedImage> channels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            channels.add(new BufferedImage(renderedImage.getWidth(), renderedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB));
        }
        return channels;
    }

    private void splitRGB(List<BufferedImage> rgbChannels, int x, int y, Color rgbColor) {
        List<Integer> colors = new ArrayList<>(Arrays.asList(rgbColor.getRed(),
                rgbColor.getBlue(), rgbColor.getGreen()));
        setChannelPixel(colors, rgbChannels, x, y);
    }

    private void splitHSV(List<BufferedImage> rgbChannels, int x, int y, Color rgbColor) {
        float[] hsv = Color.RGBtoHSB(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), null);
        /* RGBtoHSB returns values between 0 and 1 */
        int hue = (int) (hsv[0] * 255);
        int sat = (int) (hsv[1] * 255);
        int brightness = (int) (hsv[2] * 255);
        List<Integer> colors = new ArrayList<>(Arrays.asList(hue, sat, brightness));
        setChannelPixel(colors, rgbChannels, x, y);
    }

    private void setChannelPixel(List<Integer> colors, List<BufferedImage> rgbChannels, int x, int y){
        List<Integer> greyScale = colors.stream()
                .map(component -> new Color(component, component, component).getRGB())
                .collect(Collectors.toList());
        for (int i = 0; i < rgbChannels.size(); i++) {
            BufferedImage image = rgbChannels.get(i);
            image.setRGB(x, y, greyScale.get(i));
        }
    }

    private void createFrame(List<java.awt.Image> imageList){
        JFrame rgbFrame = new JFrame("RGB");
        rgbFrame.setLayout(new GridLayout(2, 3));
        imageList.forEach(c -> rgbFrame.add(new SingleImagePanel(c)));
        rgbFrame.setVisible(true);
        rgbFrame.setSize(450, 450);
    }
}

