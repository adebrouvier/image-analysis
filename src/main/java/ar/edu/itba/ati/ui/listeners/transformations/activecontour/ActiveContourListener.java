package ar.edu.itba.ati.ui.listeners.transformations.activecontour;

import ar.edu.itba.ati.image.ActiveContour;
import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.io.ImageReader;
import ar.edu.itba.ati.ui.FrameHelper;
import ar.edu.itba.ati.ui.ImageAnalyzerFrame;
import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.DoubleDialog;
import ar.edu.itba.ati.ui.listeners.selectables.Selectable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class ActiveContourListener implements Selectable {

    private WindowContext windowContext;
    private int x;
    private int y;

    public ActiveContourListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void onMousePressed(MouseEvent mouseEvent) {
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
        this.windowContext.getImageContainer().setStartSelection(mouseEvent);
    }

    @Override
    public void onMouseDragged(MouseEvent mouseEvent) {
        this.windowContext.getImageContainer().setCurrentSelection(mouseEvent);
        this.windowContext.getImageContainer().repaint();
    }

    @Override
    public void onMouseReleased(MouseEvent mouseEvent) {
        this.windowContext.getImageContainer().resetSelection();
        this.windowContext.getImageContainer().repaint();
        ActiveContour activeContour = new ActiveContour();
        DoubleDialog dialog = new DoubleDialog("Iterations");
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter max iterations", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            Image originalImage = windowContext.getImageContainer().getImage();
            ImageAnalyzerFrame frame = FrameHelper.create(originalImage);

            try {
                // List of all files in directory sorted by name
                List<String> files = Files.list(Paths.get(originalImage.getFile().getParentFile().getName()))
                                            .sorted().map(Path::toString).collect(Collectors.toList());

                // Load images
                List<Image> images = files.parallelStream()
                                .map(File::new).map(ImageReader::readImageFromFile).collect(Collectors.toList());

                List<Image> processedImages = new ArrayList<>();
                double processingTime = 0;

                for (Image image : images) {
                    System.out.println("Analyzing image: " + image.getFile().getName());
                    long startTime = System.currentTimeMillis();
                    Image imageWithCurve = activeContour.apply(image, x, y, mouseEvent.getX(), mouseEvent.getY(),
                            dialog.getDoubleValue().intValue());
                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    System.out.println("Processing time: " + elapsedTime + "ms");
                    processingTime += elapsedTime;
                    processedImages.add(imageWithCurve);
                }
                System.out.println("Average processing time: " + processingTime/images.size() + "ms");
                setButtons(frame, processedImages);
            } catch (IOException e) {
                System.err.println("There was an error opening the image sequence");
            }
        }
    }

    private void setButtons(ImageAnalyzerFrame frame, List<Image> images) {
        ListIterator<Image> iterator = images.listIterator();
        JButton playButton = frame.getWindowContext().getMouseOptions().getPlayButton();
        playButton.setEnabled(true);
        playButton.addActionListener(new PlayActionListener(frame.getWindowContext(), iterator));
        JButton nextButton = frame.getWindowContext().getMouseOptions().getNextButton();
        nextButton.setEnabled(true);
        nextButton.addActionListener(new NextImageActionListener(frame.getWindowContext(), iterator));
        JButton previousButton = frame.getWindowContext().getMouseOptions().getPreviousButton();
        previousButton.setEnabled(true);
        previousButton.addActionListener(new PreviousImageActionListener(frame.getWindowContext(), iterator));
    }
}
