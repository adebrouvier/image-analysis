package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.text.NumberFormat;

public class WindowSizeGaussDialog extends JPanel {

    public Integer getWindowsSize() {
        return windowSize;
    }

    private Integer windowSize;
    private JFormattedTextField stdField = new JFormattedTextField(NumberFormat.getNumberInstance());

    public WindowSizeGaussDialog(){
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.windowSize = 3;
        JLabel windowSizeLabel = new JLabel("Window size: 3x3");
        JSlider windowSizeSlider = new JSlider(JSlider.HORIZONTAL,
                3, 11, 3);
        windowSizeSlider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            if (value % 2 == 0) {
                value -= 1;
            }
            windowSizeLabel.setText("Window size: " + value + "x" + value);
            this.windowSize = value;
        });
        this.add(windowSizeLabel);
        this.add(windowSizeSlider);
        this.add(new JLabel("Standard deviation:"));
        this.add(stdField);
    }

    public Double getStDev() {
        return Double.valueOf(stdField.getText());
    }

}
