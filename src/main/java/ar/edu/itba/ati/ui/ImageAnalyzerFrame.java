package ar.edu.itba.ati.ui;

import javax.swing.*;
import java.awt.*;

public class ImageAnalyzerFrame extends JFrame {

    public static ImageContainer imageContainer = new ImageContainer();
    public static OptionMenu optionMenu = new OptionMenu();
    public static JLabel informationLabel = new JLabel();
    public static JScrollPane scrollPane = new JScrollPane(imageContainer);


    public ImageAnalyzerFrame(String title) {
        super(title);
        setLayout(new BorderLayout());
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(optionMenu);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(informationLabel, BorderLayout.PAGE_END);

        setLocationRelativeTo(null); // Center
    }
}
