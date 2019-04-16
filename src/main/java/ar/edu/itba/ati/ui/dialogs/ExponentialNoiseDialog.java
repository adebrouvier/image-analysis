package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class ExponentialNoiseDialog extends NoiseDialog {

    private JFormattedTextField lambdaField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public ExponentialNoiseDialog() {
        super();
        add(new JLabel("Lambda: "));
        lambdaField.setColumns(5);
        add(lambdaField);
    }

    public double getLambda() {
        return Double.valueOf(lambdaField.getText());
    }
}
