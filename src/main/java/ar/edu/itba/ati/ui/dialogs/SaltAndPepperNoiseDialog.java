package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SaltAndPepperNoiseDialog extends JPanel {

    private JFormattedTextField minField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField maxField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public SaltAndPepperNoiseDialog() {
        minField.setValue(0.0);
        maxField.setValue(1.0);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("Min"));
        this.add(minField);
        this.add(new JLabel("Max"));
        this.add(maxField);
    }

    public Double getMinValue() {
        return Double.valueOf(minField.getText());
    }

    public Double getMaxValue() {
        return Double.valueOf(maxField.getText());
    }
}
