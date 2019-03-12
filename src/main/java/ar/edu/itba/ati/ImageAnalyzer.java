package ar.edu.itba.ati;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageAnalyzer {

    private static JFrame frame;
    private JPanel contentPane;
    private BufferedImage renderedImage;

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Archivo");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        menuItem = new JMenuItem("Abrir...", KeyEvent.VK_O);
        menuItem.addActionListener(new OpenListener(contentPane));
        menu.add(menuItem);

        menuItem = new JMenuItem("Guardar", KeyEvent.VK_S);
        menuItem.addActionListener(new SaveListener(contentPane));
        menu.add(menuItem);

        return menuBar;
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
                String filename = file.getName().toLowerCase();
                int extensionIndex = filename.lastIndexOf(".");
                String extension = filename.substring(extensionIndex + 1);
                Reader reader = null;

                switch (extension) {
                    case "raw": {
                        int width = 256;
                        int height = 256;
                        reader = new RAWReader(width, height);
                        break;
                    }
                    case "pgm": {
                        reader = new PGMReader();
                        break;
                    }
                    default: {
                        System.err.println("Wrong file format.");
                        System.exit(1);
                    }
                }

                try {
                    Image image = reader.read(file);
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

        contentPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {}

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageAnalyzer imageAnalyzer = new ImageAnalyzer();

        frame.setJMenuBar(imageAnalyzer.createMenuBar());
        frame.setContentPane(imageAnalyzer.createContentPane());

        frame.setSize(450, 450);
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
