package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;

public class ThresholdDialog extends JPanel {

    public Integer getThreshold() {
        return threshold;
    }

    private Integer threshold;

    public ThresholdDialog() {
        this.threshold = 0;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel thresholdLabel = new JLabel("Threshold: 0");
        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL,
                0, 255, 0);
        thresholdSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            thresholdLabel.setText("Threshold: " + value);
            this.threshold = value;
        });
        this.add(thresholdLabel);
        this.add(thresholdSlider);
    }

}
