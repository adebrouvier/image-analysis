package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class SIFTDialog extends JPanel {

    public float getEigenvalueRadiusField() {
        return (float) Double.parseDouble(eigenvalueRadiusField.getText());
    }

    public float getGaussianSigmaField() {
        return (float) Double.parseDouble(gaussianSigmaField.getText());
    }

    public Integer getThresholdField() {
        return Integer.valueOf(thresholdField.getText());
    }

    public float getMagnificationFactorField() {
        return (float) Double.parseDouble(magnificationFactorField.getText());
    }

    public float getMagnitudeThresholdField() {
        return (float) Double.parseDouble(magnitudeThresholdField.getText());
    }

    public Integer getNumOriBinsField() {
        return Integer.valueOf(numOriBinsField.getText());
    }

    public Integer getNumOriHistBinsField() {
        return Integer.valueOf(numOriHistBinsField.getText());
    }

    public Integer getNumSpatialBinsField() {
        return Integer.valueOf(numSpatialBinsField.getText());
    }

    public float getPeakThresholdField() {
        return (float) Double.parseDouble(peakThresholdField.getText());
    }

    public float getSamplingSizeField() {
        return (float) Double.parseDouble(samplingSizeField.getText());
    }

    public float getScalingField() {
        return (float) Double.parseDouble(scalingField.getText());
    }

    public Integer getSmoothingIterationField() {
        return Integer.valueOf(smoothingIterationField.getText());
    }

    public float getValueThresholdField() {
        return (float) Double.parseDouble(valueThresholdField.getText());
    }

    private JFormattedTextField eigenvalueRadiusField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField gaussianSigmaField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField thresholdField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField magnificationFactorField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField magnitudeThresholdField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField numOriBinsField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField numOriHistBinsField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField numSpatialBinsField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField peakThresholdField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField samplingSizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField scalingField = new JFormattedTextField(NumberFormat.getNumberInstance());
    private JFormattedTextField smoothingIterationField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField valueThresholdField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public SIFTDialog() {
        eigenvalueRadiusField.setValue(10.0);
        gaussianSigmaField.setValue(1.0);
        thresholdField.setValue(10.0);
//        magnificationFactorField.setValue(0.5);
//        magnitudeThresholdField.setValue(3.0);
        numOriBinsField.setValue(8.0);
        numOriHistBinsField.setValue(36.0);
        numSpatialBinsField.setValue(4.0);
        peakThresholdField.setValue(1.0);
        samplingSizeField.setValue(3.0);
        scalingField.setValue(1.5);
        smoothingIterationField.setValue(6.0);
        valueThresholdField.setValue(0.2);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("Eigenvalue Ratio"));
        this.add(eigenvalueRadiusField);
        this.add(new JLabel("Gaussian Sigma Field"));
        this.add(gaussianSigmaField);
        this.add(new JLabel("Threshold"));
        this.add(thresholdField);
//        this.add(new JLabel("Magnification Factor"));
//        this.add(magnificationFactorField);
//        this.add(new JLabel("Magnitude Threshold"));
//        this.add(magnitudeThresholdField);
        this.add(new JLabel("Number of orientation bins"));
        this.add(numOriBinsField);
        this.add(new JLabel("Number of orientation histogram bins for finding the dominant orientations"));
        this.add(numOriHistBinsField);
        this.add(new JLabel("Number of spatial bins in each direction"));
        this.add(numSpatialBinsField);
        this.add(new JLabel("Peak threshold"));
        this.add(peakThresholdField);
        this.add(new JLabel("Sampling Size"));
        this.add(samplingSizeField);
        this.add(new JLabel("Scaling"));
        this.add(scalingField);
        this.add(new JLabel("Smoothing Iterations"));
        this.add(smoothingIterationField);
        this.add(new JLabel("Value Threshold"));
        this.add(valueThresholdField);
    }

}
