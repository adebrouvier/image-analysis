package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class GaussianNoiseDialog extends NoiseDialog {

    private JFormattedTextField meanField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField stDevField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public GaussianNoiseDialog() {
        add(new JLabel("Mean: "));
        meanField.setColumns(5);
        add(meanField);
        add(Box.createHorizontalStrut(15)); // a spacer
        stDevField.setColumns(5);
        add(new JLabel("StDev:"));
        add(stDevField);
    }

    public Double getMean() {
        return Double.valueOf(meanField.getText());
    }

    public Double getStDev() {
        return Double.valueOf(stDevField.getText());
    }
}
