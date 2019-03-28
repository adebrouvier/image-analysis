package ar.edu.itba.ati.ui.listeners.slidingwindow;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.io.*;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.WindowContext;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class EdgeEnhancementListener implements ActionListener {

    private WindowContext windowContext;

    public EdgeEnhancementListener(WindowContext windowContext){
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Image image = windowContext.getImageContainer().getImage();
        Image newImage = image.highPassfilter();
        FrameHelper.create(newImage);
    }
}