package ar.edu.itba.ati;

import ar.edu.itba.ati.image.Image;
import ar.edu.itba.ati.image.Pixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ImageAnalyzer {

    private static JFrame frame;
    private JPanel contentPane;
    private BufferedImage renderedImage;
    private Image image;
    private AreaSelector areaSelector;

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("Archivo");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        menuItem = new JMenuItem("Abrir...", KeyEvent.VK_O);
        menuItem.addActionListener(new OpenListener(contentPane));
        menu.add(menuItem);

        menuItem = new JMenuItem("Guardar", KeyEvent.VK_S);
        menuItem.addActionListener(new SaveListener(contentPane));
        menu.add(menuItem);

        menuItem = new JMenuItem("Degradee colores", KeyEvent.VK_C);
        menuItem.addActionListener(new GradientListener(new Color[] {Color.RED, Color.GREEN, Color.BLUE}));
        menu.add(menuItem);

        menuItem = new JMenuItem("Degradee grises", KeyEvent.VK_G);
        menuItem.addActionListener(new GradientListener(new Color[] {Color.BLACK, Color.GRAY, Color.WHITE}));
        menu.add(menuItem);

        return menuBar;
    }

    class GradientListener implements ActionListener {

        private Color[] colors;

        public GradientListener(Color[] colors) {
            this.colors = colors;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (renderedImage != null) {
                Graphics2D g2d = renderedImage.createGraphics();
                Point2D start = new Point2D.Float(0, 0);
                Point2D end = new Point2D.Float(renderedImage.getWidth(), renderedImage.getWidth());
                float[] dist = {0.0f, 0.33f, 0.66f};
                LinearGradientPaint p = new LinearGradientPaint(start, end, dist, colors);
                g2d.setPaint(p);
                g2d.fillRect(0, 0, renderedImage.getWidth(), renderedImage.getHeight());
                g2d.dispose();
                contentPane.repaint();
            }
        }
    }

    class OpenListener implements ActionListener {

        private JPanel panel;

        OpenListener(JPanel panel){
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fc = new JFileChooser(".");
            int returnVal = fc.showOpenDialog(panel);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String filename = file.getName();
                int extensionIndex = filename.lastIndexOf(".");
                String extension = filename.toLowerCase().substring(extensionIndex + 1);
                Reader reader = null;

                switch (extension) {
                    case "raw": {
                        String dataFile = filename.substring(0, extensionIndex) + ".txt";
                        try {
                            Scanner sc = new Scanner(new File(dataFile));
                            int width = sc.nextInt();
                            int height = sc.nextInt();
                            reader = new RAWReader(width, height);
                            break;
                        } catch (FileNotFoundException e) {
                            System.err.println("Could not read RAW data");
                            System.exit(1);
                        }
                    }
                    case "pbm": {
                        reader = new PBMReader();
                        break;
                    }
                    case "pgm": {
                        reader = new PGMReader();
                        break;
                    }
                    case "ppm": {
                        reader = new PPMReader();
                        break;
                    }
                    default: {
                        System.err.println("Wrong file format.");
                        System.exit(1);
                    }
                }

                try {
                    image = reader.read(file);
                    renderAsBufferedImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private Container createContentPane() {
        contentPane = new SingleImagePanel();
        contentPane.setOpaque(false);

        JLabel pixelColor = new JLabel();
        pixelColor.setLocation(0,0);
        contentPane.add(pixelColor);
        areaSelector = new AreaSelector();
        contentPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                Pixel p = image.getPixel(x, y);
                System.out.println(
                        new StringBuilder()
                                .append(p)
                                .append("; X: ")
                                .append(x)
                                .append("; Y: ")
                                .append(y)
                                .append(".").toString()
                );
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                areaSelector.setStart(mouseEvent.getX(), mouseEvent.getY());
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                areaSelector.setEnd(mouseEvent.getX(), mouseEvent.getY());
                contentPane.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
                int red = 0;
                int green = 0;
                int blue = 0;
                if (renderedImage != null) {
                    if (x < renderedImage.getWidth() && y < renderedImage.getHeight()) {
                        int rgb = renderedImage.getRGB(x, y);
                        red = (rgb >> 16) & 0xFF;
                        green = (rgb >> 8) & 0xFF;
                        blue = rgb & 0xFF;
                    }
                }
                pixelColor.setText("R: " + red + " G: " + green + " B: " + blue);
            }
        });

        return contentPane;
    }

    private static void createAndShowGUI() {
        frame = new JFrame("ImageAnalyzer");
        frame.setLayout(new BorderLayout());
        frame.setSize(450, 450);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageAnalyzer imageAnalyzer = new ImageAnalyzer();

        frame.setJMenuBar(imageAnalyzer.createMenuBar());
        frame.setContentPane(imageAnalyzer.createContentPane());

        frame.setLocationRelativeTo(null); // Center
        frame.setVisible(true);
    }

    private class SingleImagePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(renderedImage, 0, 0, null);
        }
    }

    private void renderAsBufferedImage(Image image) {

        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        List<Pixel> pixels = image.getPixels();

        for ( int y = 0; y < image.getHeight(); y++ ) {
            for ( int x = 0; x < image.getWidth(); x++ ) {
                int pixelIndex = (y * image.getWidth()) + x;
                Pixel p = pixels.get(pixelIndex);
                Color color = new Color(p.getRed(), p.getGreen(), p.getBlue());
                img.setRGB(x, y, color.getRGB());
            }
        }

        renderedImage = img;
        contentPane.repaint();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(ImageAnalyzer::createAndShowGUI);
    }
}
