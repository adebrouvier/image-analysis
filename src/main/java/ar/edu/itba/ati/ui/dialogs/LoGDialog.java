package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class LoGDialog extends JPanel {

    private JFormattedTextField sigmaField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField thresholdField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public LoGDialog() {
        add(new JLabel("Sigma: "));
        sigmaField.setColumns(5);
        add(sigmaField);
        add(Box.createHorizontalStrut(15)); // a spacer
        thresholdField.setColumns(5);
        add(new JLabel("Threshold: "));
        add(thresholdField);
    }

    public double getSigma() {
        return Double.valueOf(sigmaField.getText());
    }

    public int getThreshold() {
        return Integer.valueOf(thresholdField.getText());
    }
}
