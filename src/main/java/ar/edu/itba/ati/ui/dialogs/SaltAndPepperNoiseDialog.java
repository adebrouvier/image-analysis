package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.DecimalFormat;

public class SaltAndPepperNoiseDialog extends JPanel {

    private Double minValue;
    private Double maxValue;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public SaltAndPepperNoiseDialog() {
        this.minValue = 0.0;
        this.maxValue = 1.0;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel minPercentage = new JLabel("Min: 0%");
        JSlider minSlider = new JSlider(JSlider.HORIZONTAL,
                0, 200, 0);
        minSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            this.minValue = value / 1000.0;
            minPercentage.setText("Min: " + df2.format(this.minValue * 100) + "%");
        });
        this.add(minPercentage);
        this.add(minSlider);

        JLabel maxPercentage = new JLabel("Max: 100%");
        JSlider maxSlider = new JSlider(JSlider.HORIZONTAL,
                800, 1000, 1000);
        maxSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            this.maxValue = value / 1000.0;
            maxPercentage.setText("Max: " + df2.format(this.maxValue * 100) + "%");
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
