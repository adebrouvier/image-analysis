package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.ui.listeners.clickables.Clickable;
import ar.edu.itba.ati.ui.listeners.clickables.InformationClickable;
import ar.edu.itba.ati.ui.listeners.clickables.PaintClickable;

import javax.swing.*;
import java.awt.*;

public class MouseOptions extends JPanel {

    private WindowContext windowContext;
    private Clickable clickable;
    private int red;
    private int green;
    private int blue;
    private int gray;

    private ColorRectangle rgbColor = new ColorRectangle();
    private GrayRectangle grayColor = new GrayRectangle();

    private JButton nextButton;

    public MouseOptions(WindowContext windowContext) {
        this.windowContext = windowContext;
        this.clickable = new InformationClickable(windowContext);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.createButtons();
        this.createSliders();
        this.createPlaybackControls();
    }

    private void createButtons() {
        JRadioButton button1 = new JRadioButton("Information");
        button1.setSelected(true);
        button1.addActionListener((al) -> {
            if (((JRadioButton) al.getSource()).isSelected()) {
                this.clickable = new InformationClickable(windowContext);
            }
        });
        JRadioButton button2 = new JRadioButton("Paint");
        button2.addActionListener((al) -> {
            if (((JRadioButton) al.getSource()).isSelected()) {
                this.clickable = new PaintClickable(windowContext);
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(button1);
        group.add(button2);
        this.add(button1);
        this.add(button2);
    }

    private void createSliders() {
        JLabel redLabel = new JLabel("Red: 0");
        JSlider red = new JSlider(JSlider.HORIZONTAL,
                0, 254, 0);
        red.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            redLabel.setText("Red: " + value);
            this.red = value;
            this.rgbColor.repaint();
        });
        this.add(redLabel);
        this.add(red);

        JLabel greenLabel = new JLabel("Green: 0");
        JSlider green = new JSlider(JSlider.HORIZONTAL,
                0, 254, 0);
        green.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            greenLabel.setText("Green: " + value);
            this.green = value;
            this.rgbColor.repaint();
        });
        this.add(greenLabel);
        this.add(green);

        JLabel blueLabel = new JLabel("Blue: 0");
        JSlider blue = new JSlider(JSlider.HORIZONTAL,
                0, 254, 0);
        blue.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            blueLabel.setText("Blue: " + value);
            this.blue = value;
            this.rgbColor.repaint();
        });
        this.add(blueLabel);
        this.add(blue);


        JLabel grayLabel = new JLabel("Gray: 0");
        JSlider gray = new JSlider(JSlider.HORIZONTAL,
                0, 254, 0);
        gray.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            grayLabel.setText("Gray: " + value);
            this.gray = value;
            this.grayColor.repaint();
        });
        this.add(grayLabel);
        this.add(gray);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(grayColor, 0);
        panel.add(rgbColor, 1);
        this.add(panel);
    }

    private void createPlaybackControls() {
        this.nextButton = new JButton("Next");
        this.nextButton.setEnabled(false);
        this.add(nextButton);
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public Clickable getClickable() {
        return clickable;
    }

    public void setClickable(Clickable clickable) {
        this.clickable = clickable;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }

    public int getGray() {
        return gray;
    }

    class ColorRectangle extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(red, green, blue));
            g.fillRect(10, 0, 20, 20);
        }
    }

    class GrayRectangle extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(gray, gray, gray));
            g.fillRect(10, 0, 20, 20);
        }
    }
}
