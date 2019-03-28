package ar.edu.itba.ati.ui.listeners.operations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;
import ar.edu.itba.ati.ui.dialogs.ThresholdDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DynamicCompressionListener implements ActionListener {

    private WindowContext windowContext;

    public DynamicCompressionListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();

        DoubleDialog dialog = new DoubleDialog("Threshold");
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter Threshold", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Image newImage = image.dynamicRangeCompress(dialog.getDoubleValue());
            FrameHelper.create(newImage);
        }
    }
}
