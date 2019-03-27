package ar.edu.itba.ati.ui.dialogs;

import javax.swing.*;
import java.util.Hashtable;

public class ContrastDialog extends JPanel {

    private int r1;
    private int r2;
    private JSlider s1 = new JSlider();
    private JSlider s2 = new JSlider();
    private static final int R1_MIN = 0;
    private static final int R2_MAX = 255;

    public ContrastDialog(int r1, int r2){
        this.r1 = r1;
        this.r2 = r2;
        createSliders();
    }

    private void createSliders() {
        createSlider("S1: ", 0, R1_MIN, r1, s1);
        createSlider("S2: ", r2, r2, R2_MAX, s2);
    }

    private void createSlider(String label, int defaultValue, int min, int max, JSlider slider){
        JLabel sliderLabel = new JLabel(label + defaultValue);
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setValue(defaultValue);
        slider.addChangeListener((e) -> {
            int value = ((JSlider) e.getSource()).getValue();
            sliderLabel.setText(label + value);
        });

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(min, new JLabel(String.valueOf(min)));
        labelTable.put(max, new JLabel(String.valueOf(max)));
        slider.setLabelTable( labelTable );
        slider.setPaintLabels(true);

        add(sliderLabel);
        add(slider);
        add(Box.createHorizontalStrut(15));
    }

    public int getS1() {
        return s1.getValue();
    }

    public int getS2() {
        return s2.getValue();
    }
}
