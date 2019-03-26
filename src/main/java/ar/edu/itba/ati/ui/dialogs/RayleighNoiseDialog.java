package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class RayleighNoiseDialog extends NoiseDialog {

    private JFormattedTextField phiField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public RayleighNoiseDialog(){
        add(new JLabel("Phi: "));
        phiField.setColumns(5);
        add(phiField);
    }

    public double getPhi() {
        return Double.valueOf(phiField.getText());
    }
}
