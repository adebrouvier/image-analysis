package ar.edu.itba.ati.ui;

import javax.swing.*;
import java.awt.*;
import ar.edu.itba.ati.image.Image;

public class ImageAnalyzerFrame extends JFrame {

    private WindowContext windowContext = new WindowContext();

    public ImageAnalyzerFrame(String title) {
        super(title);
        setLayout(new BorderLayout());
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setJMenuBar(windowContext.getOptionMenu());
        getContentPane().add(windowContext.getImageContainer(), BorderLayout.CENTER);
        getContentPane().add(windowContext.getInformationLabel(), BorderLayout.PAGE_END);

        setLocationRelativeTo(null); // Center
    }

    public ImageAnalyzerFrame(Image image) {
        super("Image Analyzer");
        setLayout(new BorderLayout());
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setJMenuBar(windowContext.getOptionMenu());
        windowContext.getImageContainer().setImage(image);
        windowContext.getImageContainer().renderImage();
        getContentPane().add(windowContext.getImageContainer(), BorderLayout.CENTER);
        getContentPane().add(windowContext.getInformationLabel(), BorderLayout.PAGE_END);

        setLocationRelativeTo(null); // Center
    }
}
