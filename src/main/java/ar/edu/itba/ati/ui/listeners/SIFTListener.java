package ar.edu.itba.ati.ui.listeners;

import ar.edu.itba.ati.ui.WindowContext;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SIFTListener implements ActionListener {

    private WindowContext windowContext;

    public SIFTListener(WindowContext windowContext) {
        this.windowContext = windowContext;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        MBFImage query = null;
        MBFImage target = null;
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
        generateSIFTImage(query, target);
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

    private void generateSIFTImage(MBFImage query, MBFImage target) {
        DoGSIFTEngine engine = new DoGSIFTEngine();
        LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
        LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());

        LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);
        matcher.setModelFeatures(queryKeypoints);
        matcher.findMatches(targetKeypoints);

        RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(100.0, 1500,
                new RANSAC.PercentageInliersStoppingCondition(0.5));
        matcher = new ConsistentLocalFeatureMatcher2d<>(
                new FastBasicKeypointMatcher<>(100), modelFitter);

        matcher.setModelFeatures(queryKeypoints);
        matcher.findMatches(targetKeypoints);

        MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(),
                RGBColour.RED);

        DisplayUtilities.display(consistentMatches);
    }

}

