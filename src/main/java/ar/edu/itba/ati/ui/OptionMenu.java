package ar.edu.itba.ati.ui;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.ui.listeners.*;
import ar.edu.itba.ati.ui.listeners.histogram.HistogramEqualizationListener;
import ar.edu.itba.ati.ui.listeners.histogram.HistogramListener;
import ar.edu.itba.ati.ui.listeners.operations.*;
import ar.edu.itba.ati.ui.listeners.selectables.InformationSelectable;
import ar.edu.itba.ati.ui.listeners.selectables.Selectable;
import ar.edu.itba.ati.ui.listeners.selectables.SubImageSelectable;
import ar.edu.itba.ati.ui.listeners.slidingwindow.*;
import ar.edu.itba.ati.ui.listeners.transformations.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class OptionMenu extends JMenuBar {

    private static final int BINARY_IMAGE_SIZE = 200;
    private static final int BINARY_SHAPE_SIZE = 50;
    private Selectable selectable = null;
    private WindowContext windowContext;


    public OptionMenu(WindowContext windowContext) {
        super();
        this.windowContext = windowContext;
        createFileMenu();
        createGradientMenu();
        createShapeMenu();
        createSelectMenu();
        createTransformMenu();
        createHistogramMenu();
        createOperations();
    }

    private void addItem(JMenu menu, String name, int key, ActionListener listener) {
        JMenuItem item = new JMenuItem(name, key);
        item.addActionListener(listener);
        menu.add(item);
    }

    private void createFileMenu() {
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_A);

        JMenuItem item;
        item = new JMenuItem("Open...", KeyEvent.VK_O);
        item.addActionListener(new OpenListener(this.windowContext));
        file.add(item);

        item = new JMenuItem("Save", KeyEvent.VK_S);
        item.addActionListener(new SaveListener(this.windowContext));
        file.add(item);

        item = new JMenuItem("Close");
        item.addActionListener((ae) -> System.exit(0));
        file.add(item);

        add(file);
    }

    private void createGradientMenu() {
        JMenu gradient = new JMenu("Gradient");
        gradient.setMnemonic(KeyEvent.VK_G);

        JMenuItem item;
        item = new JMenuItem("Color gradient", KeyEvent.VK_C);
        item.addActionListener(new GradientListener(this.windowContext, new Color[]{Color.RED, Color.GREEN, Color.BLUE}, Image.ImageType.RGB));
        gradient.add(item);

        item = new JMenuItem("Grayscale gradient", KeyEvent.VK_G);
        item.addActionListener(new GradientListener(this.windowContext, new Color[]{Color.BLACK, Color.GRAY, Color.WHITE}, Image.ImageType.GRAY_SCALE));
        gradient.add(item);

        item = new JMenuItem("RGB and HSV", KeyEvent.VK_R);
        item.addActionListener(new RGBHSVDecomposition(this.windowContext));
        gradient.add(item);

        add(gradient);
    }

    private void createSelectMenu() {
        JMenu select = new JMenu("Select");
        select.setMnemonic(KeyEvent.VK_S);

        JMenuItem item;
        item = new JMenuItem("Select sub image", KeyEvent.VK_S);
        item.addActionListener((ae) -> this.selectable = new SubImageSelectable(this.windowContext));
        select.add(item);

        item = new JMenuItem("Get sub image information", KeyEvent.VK_I);
        item.addActionListener((ae) -> this.selectable = new InformationSelectable(this.windowContext));
        select.add(item);

        add(select);
    }

    public Selectable getSelectable() {
        return selectable;
    }

    private void createShapeMenu() {
        JMenu shape = new JMenu("Shape");
        shape.setMnemonic(KeyEvent.VK_S);

        JMenuItem item;
        item = new JMenuItem("Binary circle", KeyEvent.VK_C);
        item.addActionListener(new CircleListener(windowContext, BINARY_IMAGE_SIZE, BINARY_SHAPE_SIZE));
        shape.add(item);

        item = new JMenuItem("Binary square", KeyEvent.VK_Q);
        item.addActionListener(new SquareListener(windowContext, BINARY_IMAGE_SIZE, BINARY_SHAPE_SIZE));
        shape.add(item);

        add(shape);
    }

    public void setSelectable(Selectable selectable) {
        this.selectable = selectable;
    }

    private void createTransformMenu() {
        JMenu transform = new JMenu("Transform");
        transform.setMnemonic(KeyEvent.VK_T);

        JMenuItem item;
        item = new JMenuItem("Negative", KeyEvent.VK_N);
        item.addActionListener(new NegativeListener(windowContext));
        transform.add(item);

        JMenu noise = createNoiseSubMenu();
        transform.add(noise);

        JMenu slidingWindow = createSlidingWindowSubMenu();
        transform.add(slidingWindow);

        item = new JMenuItem("Contrast increase", KeyEvent.VK_C);
        item.addActionListener(new ContrastListener(windowContext));
        transform.add(item);

        transform.add(createBordersSubMenu());
        transform.add(createThresholdSubMenu());
        transform.add(createFiltersSubMenu());

        add(transform);
    }

    private JMenu createNoiseSubMenu() {
        JMenu noise = new JMenu("Noise");

        JMenuItem item;
        item = new JMenuItem("Exponential", KeyEvent.VK_E);
        item.addActionListener(new ExponentialNoiseListener(windowContext));
        noise.add(item);

        item = new JMenuItem("Gaussian", KeyEvent.VK_G);
        item.addActionListener(new GaussianNoiseListener(windowContext));
        noise.add(item);

        item = new JMenuItem("Rayleigh", KeyEvent.VK_R);
        item.addActionListener(new RayleighNoiseListener(windowContext));
        noise.add(item);

        item = new JMenuItem("Salt and Pepper", KeyEvent.VK_P);
        item.addActionListener(new SaltAndPepperNoiseListener(windowContext));
        noise.add(item);

        return noise;
    }

    private JMenu createThresholdSubMenu() {
        JMenu threshold = new JMenu("Threshold");
        JMenuItem item;

        item = new JMenuItem("Simple", KeyEvent.VK_S);
        item.addActionListener(new ThresholdListener(windowContext));
        threshold.add(item);

        item = new JMenuItem("Global", KeyEvent.VK_G);
        item.addActionListener(new GlobalThresholdListener(windowContext));
        threshold.add(item);

        item = new JMenuItem("Otsu", KeyEvent.VK_O);
        item.addActionListener(new OtsuThresholdListener(windowContext));
        threshold.add(item);

        return threshold;
    }


    private JMenu createSlidingWindowSubMenu() {
        JMenu slidingWindow = new JMenu("Sliding Window");

        JMenuItem item;
        item = new JMenuItem("Mean", KeyEvent.VK_E);
        item.addActionListener(new MeanListener(windowContext));
        slidingWindow.add(item);

        item = new JMenuItem("Median", KeyEvent.VK_G);
        item.addActionListener(new MedianListener(windowContext));
        slidingWindow.add(item);

        item = new JMenuItem("Weighted Median", KeyEvent.VK_R);
        item.addActionListener(new WeightedMedianListener(windowContext));
        slidingWindow.add(item);

        item = new JMenuItem("Gauss", KeyEvent.VK_P);
        item.addActionListener(new GaussianListener(windowContext));
        slidingWindow.add(item);

        item = new JMenuItem("Enhancement of edges", KeyEvent.VK_P);
        item.addActionListener(new EdgeEnhancementListener(windowContext));
        slidingWindow.add(item);

        return slidingWindow;
    }

    private void createHistogramMenu() {
        JMenu histogram = new JMenu("Histogram");
        histogram.setMnemonic(KeyEvent.VK_H);

        JMenuItem item;
        item = new JMenuItem("Show");
        item.addActionListener(new HistogramListener(windowContext));
        histogram.add(item);

        item = new JMenuItem("Equalize");
        item.addActionListener(new HistogramEqualizationListener(windowContext));
        histogram.add(item);

        add(histogram);
    }

    private void createOperations() {
        JMenu operations = new JMenu("Operations");
        JMenuItem item;

        item = new JMenuItem("Addition");
        item.addActionListener(new AdditionListener(windowContext));
        operations.add(item);

        item = new JMenuItem("Subtraction");
        item.addActionListener(new SubtractListener(windowContext));
        operations.add(item);

        item = new JMenuItem("Multiply");
        item.addActionListener(new MultiplyListener(windowContext));
        operations.add(item);

        item = new JMenuItem("Dynamic Range Compression");
        item.addActionListener(new DynamicCompressionListener(windowContext));
        operations.add(item);

        item = new JMenuItem("Gamma Power");
        item.addActionListener(new GammaPowerListener(windowContext));
        operations.add(item);

        add(operations);
    }

    private JMenu createBordersSubMenu() {
        JMenu borders = new JMenu("Borders");

        JMenuItem item;
        item = new JMenuItem("Prewitt", KeyEvent.VK_P);
        item.addActionListener(new PrewittListener(windowContext));
        borders.add(item);

        item = new JMenuItem("Sobel", KeyEvent.VK_S);
        item.addActionListener(new SobelListener(windowContext));
        borders.add(item);

        item = new JMenuItem("Sobel", KeyEvent.VK_S);
        item.addActionListener(new SobelListener(windowContext));
        borders.add(item);

        addItem(borders, "Laplacian", KeyEvent.VK_L, new LaplacianListener(windowContext));
        addItem(borders, "LoG", KeyEvent.VK_G, new LoGListener(windowContext));
        addItem(borders, "exercise a)", KeyEvent.VK_A, new MaskListener(
                windowContext, new Double[]{1.0, 1.0, 1.0, 1.0, -2.0, 1.0, -1.0, -1.0, -1.0}, 3));
        addItem(borders, "Kirsch", KeyEvent.VK_K, new MaskListener(
                windowContext, new Double[]{5.0, 5.0, 5.0, -3.0, 0.0, -3.0, -3.0, -3.0, -3.0}, 3));
        addItem(borders, "8 - Prewitt", KeyEvent.VK_K, new MaskListener(
                windowContext, new Double[]{1.0, 1.0, 1.0, 0.0, 0.0, 0.0, -1.0, -1.0, -1.0}, 3));
        addItem(borders, "8 - Sobel", KeyEvent.VK_K, new MaskListener(
                windowContext, new Double[]{1.0, 2.0, 1.0, 0.0, 0.0, 0.0, -1.0, -2.0, -1.0}, 3));
        return borders;
    }

    private JMenu createFiltersSubMenu() {
        JMenu filters = new JMenu("Filters");
        addItem(filters, "Isotropic Diffusion", KeyEvent.VK_I, new IsotropicDiffusionListener(windowContext));
        addItem(filters, "Anisotropic Diffusion", KeyEvent.VK_A, new AnisotropicDiffusionListener(windowContext));
        return filters;
    }
}
