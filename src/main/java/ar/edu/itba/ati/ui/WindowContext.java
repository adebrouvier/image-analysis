package ar.edu.itba.ati.ui;

import javax.swing.*;

public class WindowContext {
    private ImageContainer imageContainer;
    private OptionMenu optionMenu;
    private JLabel informationLabel;
    private MouseOptions mouseOptions;

    public WindowContext() {
        imageContainer = new ImageContainer(this);
        optionMenu = new OptionMenu(this);
        informationLabel = new JLabel();
        mouseOptions = new MouseOptions(this);
    }

    public ImageContainer getImageContainer() {
        return imageContainer;
    }

    public OptionMenu getOptionMenu() {
        return optionMenu;
    }

    public JLabel getInformationLabel() {
        return informationLabel;
    }

    public MouseOptions getMouseOptions() {
        return mouseOptions;
    }
}
