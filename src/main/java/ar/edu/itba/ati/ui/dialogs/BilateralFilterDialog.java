package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class BilateralFilterDialog extends JPanel {

    public Double getFirstValue() {
        return Double.valueOf(doubleValueFirstField.getText());
    }

    public Double getSecondValue() {
        return Double.valueOf(doubleValueSecondField.getText());
    }

    public Integer getMaskSize() {
        return Integer.valueOf(integerValueField.getText());
    }

    private JFormattedTextField doubleValueFirstField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField doubleValueSecondField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField integerValueField = new JFormattedTextField(NumberFormat.getIntegerInstance());

    public BilateralFilterDialog() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("Spatial Constant"));
        this.add(doubleValueFirstField);
        this.add(new JLabel("Color intensity Constant"));
        this.add(doubleValueSecondField);
        this.add(new JLabel("MaskSize"));
        this.add(integerValueField);
    }

}