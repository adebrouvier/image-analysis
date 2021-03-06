package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class CircularHoughDialog extends JPanel {

    public Integer getXStepValue() {
        return Integer.valueOf(xStepField.getText());
    }
    public Integer getYStepValue() {
        return Integer.valueOf(yStepField.getText());
    }
    public Double getRadiusStepValue() {
        return Double.valueOf(radiusStepField.getText());
    }
    public Double getEpsilonValue() {
        return Double.valueOf(epsilonField.getText());
    }
    public Integer getMaximumCirclesValue() {
        return Integer.valueOf(maximumCircleField.getText());
    }


    private JFormattedTextField xStepField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField yStepField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField radiusStepField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField epsilonField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField maximumCircleField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public CircularHoughDialog() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("xStep"));
        xStepField.setValue(2);
        this.add(xStepField);
        this.add(new JLabel("yStep"));
        yStepField.setValue(2);
        this.add(yStepField);
        this.add(new JLabel("Radius step"));
        radiusStepField.setValue(2);
        this.add(radiusStepField);
        this.add(new JLabel("epsilon"));
        epsilonField.setValue(2);
        this.add(epsilonField);
        this.add(new JLabel("Maximum amount of circles"));
        maximumCircleField.setValue(15);
        this.add(maximumCircleField);
    }

}