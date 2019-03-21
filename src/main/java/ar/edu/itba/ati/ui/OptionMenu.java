package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.listeners.*;
import ar.edu.itba.ati.ui.listeners.selectables.InformationSelectable;
import ar.edu.itba.ati.ui.listeners.selectables.Selectable;
import ar.edu.itba.ati.ui.listeners.selectables.SubImageSelectable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class OptionMenu extends JMenuBar {

    private static final int BINARY_IMAGE_SIZE = 200;
    private static final int BINARY_SHAPE_SIZE = 50;
    private JMenu file;
    private JMenu gradient;
    private JMenu shape;
    private Selectable selectable = null;
    private WindowContext windowContext;


    public OptionMenu(WindowContext windowContext){
        super();
        this.windowContext = windowContext;
        createFileMenu();
        createGradientMenu();
        createShapeMenu();
        createSelectMenu();
    }

    private void createFileMenu() {
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_A);

        JMenuItem item;
        item = new JMenuItem("Open...", KeyEvent.VK_O);
        item.addActionListener(new OpenListener(this.windowContext));
        file.add(item);

        item = new JMenuItem("Save", KeyEvent.VK_S);
        item.addActionListener(new SaveListener(this.windowContext));
        file.add(item);

        item = new JMenuItem("Close");
        item.addActionListener((ae) -> System.exit(0));
        file.add(item);

        add(file);
    }

    private void createGradientMenu() {
        JMenu gradient = new JMenu("Gradient");
        gradient.setMnemonic(KeyEvent.VK_G);

        JMenuItem item;
        item = new JMenuItem("Color gradient", KeyEvent.VK_C);
        item.addActionListener(new GradientListener(this.windowContext, new Color[] {Color.RED, Color.GREEN, Color.BLUE}, Image.ImageType.RGB));
        gradient.add(item);

        item = new JMenuItem("Grayscale gradient", KeyEvent.VK_G);
        item.addActionListener(new GradientListener(this.windowContext, new Color[] {Color.BLACK, Color.GRAY, Color.WHITE}, Image.ImageType.GRAY_SCALE));
        gradient.add(item);

        item = new JMenuItem("RGB and HSV", KeyEvent.VK_R);
        item.addActionListener(new RGBHSVDecomposition(this.windowContext));
        gradient.add(item);

        add(gradient);
    }

    private void createSelectMenu() {
        JMenu select = new JMenu("Select");
        select.setMnemonic(KeyEvent.VK_S);

        JMenuItem item;
        item = new JMenuItem("Select sub image", KeyEvent.VK_S);
        item.addActionListener((ae) -> this.selectable = new SubImageSelectable(this.windowContext));
        select.add(item);

        item = new JMenuItem("Get sub image information", KeyEvent.VK_I);
        item.addActionListener((ae) -> this.selectable = new InformationSelectable(this.windowContext));
        select.add(item);

        add(select);
    }

    public Selectable getSelectable() {
        return selectable;
    }

    private void createShapeMenu(){
        this.shape = new JMenu("Shape");
        this.shape.setMnemonic(KeyEvent.VK_S);

        JMenuItem item;
        item = new JMenuItem("Binary circle", KeyEvent.VK_C);
        item.addActionListener(new CircleListener(windowContext, BINARY_IMAGE_SIZE, BINARY_SHAPE_SIZE));
        this.shape.add(item);

        item = new JMenuItem("Binary square", KeyEvent.VK_Q);
        item.addActionListener(new SquareListener(windowContext, BINARY_IMAGE_SIZE, BINARY_SHAPE_SIZE));
        this.shape.add(item);

        add(this.shape);
    }

    public void setSelectable(Selectable selectable) {
        this.selectable = selectable;
    }
}
