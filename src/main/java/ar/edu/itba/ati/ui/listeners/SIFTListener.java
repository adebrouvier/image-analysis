package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.ui.WindowContext;
import ar.edu.itba.ati.ui.dialogs.SIFTDialog;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.engine.DoGSIFTEngineOptions;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.feature.local.keypoints.KeypointVisualizer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class SIFTListener implements ActionListener {

    private WindowContext windowContext;

    public SIFTListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MBFImage query = null;
        MBFImage target = null;

        SIFTDialog dialog = new SIFTDialog();
        int result = JOptionPane.showConfirmDialog(null, dialog,
                "Please enter SIFT options", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        DoGSIFTEngineOptions options = getOptions(dialog);

        try {
            File queryFile = openFile();
            if (queryFile == null)
                return;
            File targetFile = openFile();
            if (targetFile == null)
                return;
            query = ImageUtilities.readMBF(queryFile);
            target = ImageUtilities.readMBF(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        generateSIFTImage(query, target, dialog.getThresholdField(), options);
    }

    private DoGSIFTEngineOptions getOptions(SIFTDialog dialog) {
        DoGSIFTEngineOptions options = new DoGSIFTEngineOptions();

        options.setEigenvalueRatio(dialog.getEigenvalueRadiusField());
        options.setGaussianSigma(dialog.getGaussianSigmaField());
//        options.setMagnificationFactor(dialog.getMagnificationFactorField());
//        options.setMagnitudeThreshold(dialog.getMagnitudeThresholdField());
        options.setNumOriBins(dialog.getNumOriBinsField());
        options.setNumOriHistBins(dialog.getNumOriHistBinsField());
        options.setNumSpatialBins(dialog.getNumSpatialBinsField());
        options.setPeakThreshold(dialog.getPeakThresholdField());
        options.setSamplingSize(dialog.getSamplingSizeField());
        options.setScaling(dialog.getScalingField());
        options.setSmoothingIterations(dialog.getSmoothingIterationField());
        options.setValueThreshold(dialog.getValueThresholdField());
        return options;
    }

    private File openFile() {
        JFileChooser fc = new JFileChooser(".");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images",
                "jpeg", "png", "jpg");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(windowContext.getImageContainer());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    private void generateSIFTImage(MBFImage query, MBFImage target, int threshold, DoGSIFTEngineOptions options) {
        DoGSIFTEngine engine = new DoGSIFTEngine(options);
        LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
        LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());

        LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(threshold);
        matcher.setModelFeatures(queryKeypoints);
        matcher.findMatches(targetKeypoints);

//        RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(100.0, 1500,
//                new RANSAC.PercentageInliersStoppingCondition(0.5));
//        matcher = new ConsistentLocalFeatureMatcher2d<>(
//                new FastBasicKeypointMatcher<>(100), modelFitter);
//
//        matcher.setModelFeatures(queryKeypoints);
//        matcher.findMatches(targetKeypoints);


        MBFImage queryMatches = KeypointVisualizer.drawPatchesInplace(query, queryKeypoints, null, RGBColour.BLUE);
        MBFImage targetMatches = KeypointVisualizer.drawPatchesInplace(target, targetKeypoints, null, RGBColour.BLUE);

        MBFImage consistentMatches = MatchingUtilities.drawMatches(queryMatches, targetMatches, matcher.getMatches(),
                RGBColour.RED);

        DisplayUtilities.display(consistentMatches);
        DisplayUtilities.display(queryMatches);
        DisplayUtilities.display(targetMatches);

        System.out.println("Matches image 1: " + queryKeypoints.size());
        System.out.println("Matches image 2: " + targetKeypoints.size());

        AtomicReference<Double> diff = new AtomicReference<>(0.0);
        matcher.getMatches().forEach((pair) -> {
            Keypoint first = pair.firstObject();
            Keypoint second = pair.secondObject();
            double addition = first.ori - second.ori;
            addition = addition * addition;
            double finalAddition = addition;
            diff.updateAndGet(v -> new Double((v + finalAddition)));
        });
        diff.set(Math.sqrt(diff.get()));

        System.out.println("Correspondence: " + diff);
    }

}

