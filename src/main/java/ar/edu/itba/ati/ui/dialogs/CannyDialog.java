package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class CannyDialog extends JPanel {

    public Integer getFirstValue() {
        return Integer.valueOf(integerValueFirstField.getText());
    }

    public Integer getSecondValue() {
        return Integer.valueOf(integerValueSecondField.getText());
    }


    private JFormattedTextField integerValueFirstField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField integerValueSecondField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public CannyDialog() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("T1"));
        this.add(integerValueFirstField);
        this.add(new JLabel("T2"));
        this.add(integerValueSecondField);
    }

}