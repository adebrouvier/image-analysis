package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class AnisotropicDiffusionDialog extends JPanel {

    private ButtonGroup buttonGroup = new ButtonGroup();
    private JFormattedTextField timeField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField sigmaField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public AnisotropicDiffusionDialog() {

        add(new JLabel("Time: "));
        timeField.setColumns(5);
        add(timeField);

        add(new JLabel("Sigma: "));
        sigmaField.setColumns(5);
        add(sigmaField);

        JRadioButton radioButton = new JRadioButton("Leclerc");
        radioButton.setSelected(true);
        radioButton.setActionCommand("LECLERC");
        add(radioButton);
        buttonGroup.add(radioButton);

        radioButton = new JRadioButton("Lorentz");
        radioButton.setActionCommand("LORENTZ");
        add(radioButton);
        buttonGroup.add(radioButton);
    }

    public int getTime() {
        return ((Number) timeField.getValue()).intValue();
    }

    public String getDetector() {
        return buttonGroup.getSelection().getActionCommand();
    }

    public int getSigma() {
        return ((Number) sigmaField.getValue()).intValue();
    }
}
