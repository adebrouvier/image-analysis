package ar.edu.itba.ati.ui;

import javax.swing.*;

public class WindowContext {
    private ImageContainer imageContainer = new ImageContainer(this);
    private OptionMenu optionMenu = new OptionMenu(this);
    private JLabel informationLabel = new JLabel();

    public WindowContext() {}

    public ImageContainer getImageContainer() {
        return imageContainer;
    }

    public OptionMenu getOptionMenu() {
        return optionMenu;
    }

    public JLabel getInformationLabel() {
        return informationLabel;
    }
}
