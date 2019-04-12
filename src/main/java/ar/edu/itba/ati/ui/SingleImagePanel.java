package ar.edu.itba.ati.ui;

import javax.swing.*;
import java.awt.*;

public class SingleImagePanel extends JPanel {

    private Image image;

    public SingleImagePanel(){}

    public SingleImagePanel(Image image){
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    public void setImage(Image image) {
        this.image = image;
    }
}