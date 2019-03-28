package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class SaltAndPepperNoiseDialog extends JPanel {

    private Double minValue;
    private Double maxValue;

    public SaltAndPepperNoiseDialog(){
        this.minValue = 0.0;
        this.maxValue = 1.0;
        JLabel minPercentage = new JLabel("Min: 0%");
        JSlider minSlider = new JSlider(JSlider.HORIZONTAL,
                0, 100, 0);
        minSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            minPercentage.setText("Min: " + value + "%");
            this.minValue = value / 100.0;
        });
        this.add(minPercentage);
        this.add(minSlider);

        JLabel maxPercentage = new JLabel("Max: 100%");
        JSlider maxSlider = new JSlider(JSlider.HORIZONTAL,
                0, 100, 100);
        maxSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            maxPercentage.setText("Max: " + value + "%");
            this.maxValue = value / 100.0;
        });
        this.add(maxPercentage);
        this.add(maxSlider);
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }
}
