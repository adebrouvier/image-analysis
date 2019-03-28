package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;

public class WindowSizeDialog extends JPanel {

    public Integer getWindowsSize() {
        return windowSize;
    }

    private Integer windowSize;

    public WindowSizeDialog(){
        this.windowSize = 3;
        JLabel thresholdLabel = new JLabel("Window size: 3x3");
        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL,
                3, 11, 3);
        thresholdSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            if (value % 2 == 0) {
                value -= 1;
            }
            thresholdLabel.setText("Window size: " + value + "x" + value);
            this.windowSize = value;
        });
        this.add(thresholdLabel);
        this.add(thresholdSlider);
    }

}
