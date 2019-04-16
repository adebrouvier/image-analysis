package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class DoubleDialog extends JPanel {

    public Double getDoubleValue() {
        return Double.valueOf(doubleValueField.getText());
    }

    private JFormattedTextField doubleValueField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public DoubleDialog(String label) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel(label));
        this.add(doubleValueField);
    }

}