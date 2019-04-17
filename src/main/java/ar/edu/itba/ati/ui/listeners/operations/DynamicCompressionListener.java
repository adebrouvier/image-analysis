package ar.edu.itba.ati.ui.listeners.operations;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

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
        Image newImage = image.dynamicRangeCompress();
        FrameHelper.create(newImage);
    }
}
