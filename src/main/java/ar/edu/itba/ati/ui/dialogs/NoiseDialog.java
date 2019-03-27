package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public abstract class NoiseDialog extends JPanel {
    private JFormattedTextField percentageField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public NoiseDialog() {
        add(new JLabel("Percentage: "));
        percentageField.setColumns(5);
        add(percentageField);
        add(Box.createHorizontalStrut(15));
    }

    public double getPercentage() {
        return Double.valueOf(percentageField.getText());
    }

}
