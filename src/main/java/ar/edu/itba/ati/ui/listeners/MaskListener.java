package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaskListener implements ActionListener {

    private WindowContext windowContext;
    private Double[] mask;
    private Integer maskSize;

    public MaskListener(WindowContext windowContext, Double[] mask, Integer maskSize) {
        this.windowContext = windowContext;
        this.mask = mask;
        this.maskSize = maskSize;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        Image newImage = image.applyMask(this.maskSize, (pixels) -> image.getWeightedValue(pixels, this.mask));
        FrameHelper.create(newImage);
    }
}
