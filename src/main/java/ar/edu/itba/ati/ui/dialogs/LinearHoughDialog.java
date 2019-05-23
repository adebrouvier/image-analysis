package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class LinearHoughDialog extends JPanel {

    public Double getAngleStepValue() {
        return Double.valueOf(angleStepField.getText());
    }
    public Double getRoStepValue() {
        return Double.valueOf(roStepField.getText());
    }
    public Double getEpsilonValue() {
        return Double.valueOf(epsilonField.getText());
    }
    public Integer getMaximumLinesValue() {
        return Integer.valueOf(maximumLinesField.getText());
    }


    private JFormattedTextField angleStepField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField roStepField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField epsilonField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField maximumLinesField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public LinearHoughDialog() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("Angle Step"));
        this.add(angleStepField);
        this.add(new JLabel("Ro Step"));
        this.add(roStepField);
        this.add(new JLabel("epsilon"));
        this.add(epsilonField);
        this.add(new JLabel("Maximum amount of lines"));
        this.add(maximumLinesField);
    }


}