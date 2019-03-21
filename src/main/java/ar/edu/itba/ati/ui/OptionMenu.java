package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class OptionMenu extends JMenuBar {

    private static final int BINARY_IMAGE_SIZE = 200;
    private static final int BINARY_SHAPE_SIZE = 50;
    private JMenu file;
    private JMenu gradient;
    private JMenu shape;

    public OptionMenu(){
        super();

        createFileMenu();
        createGradientMenu();
        createShapeMenu();
    }

    private void createFileMenu() {
        this.file = new JMenu("File");
        this.file.setMnemonic(KeyEvent.VK_A);

        JMenuItem item;
        item = new JMenuItem("Open...", KeyEvent.VK_O);
        item.addActionListener(new OpenListener());
        this.file.add(item);

        item = new JMenuItem("Save", KeyEvent.VK_S);
//        item.addActionListener(new SaveListener(contentPane));
        this.file.add(item);

        item = new JMenuItem("Close");
        item.addActionListener((ae) -> { System.exit(0);});
        this.file.add(item);

        add(this.file);
    }

    private void createGradientMenu() {
        this.gradient = new JMenu("Gradient");
        this.gradient.setMnemonic(KeyEvent.VK_G);

        JMenuItem item;
        item = new JMenuItem("Color gradient", KeyEvent.VK_C);
        item.addActionListener(new GradientListener(new Color[] {Color.RED, Color.GREEN, Color.BLUE}, Image.ImageType.RGB));
        this.gradient.add(item);

        item = new JMenuItem("Grayscale gradient", KeyEvent.VK_G);
        item.addActionListener(new GradientListener(new Color[] {Color.BLACK, Color.GRAY, Color.WHITE}, Image.ImageType.GRAYSCALE));
        this.gradient.add(item);

        item = new JMenuItem("RGB and HSV", KeyEvent.VK_R);
        item.addActionListener(new RGBHSVDecomposition());
        this.gradient.add(item);

        add(this.gradient);
    }

    private void createShapeMenu(){
        this.shape = new JMenu("Shape");
        this.shape.setMnemonic(KeyEvent.VK_S);

        JMenuItem item;
        item = new JMenuItem("Binary circle", KeyEvent.VK_C);
        item.addActionListener(new CircleListener(BINARY_IMAGE_SIZE, BINARY_SHAPE_SIZE));
        this.shape.add(item);

        item = new JMenuItem("Binary square", KeyEvent.VK_Q);
        item.addActionListener(new SquareListener(BINARY_IMAGE_SIZE, BINARY_SHAPE_SIZE));
        this.shape.add(item);

        add(this.shape);
    }

}
