package ar.edu.itba.ati.image;

import ar.edu.itba.ati.filters.GaussianMask;
import ar.edu.itba.ati.utils.IntPair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ActiveContour {

    private Set<IntPair> lin = new HashSet<>();
    private Set<IntPair> lout = new HashSet<>();
    private List<Integer> phi;
    private int objectRed;
    private int objectGreen;
    private int objectBlue;
    private int objectPixels;

    private final int BACKGROUND = 3;
    private final int LOUT = 1;
    private final int LIN = -1;
    private final int OBJECT = -3;

    private Image image;

    private final int GAUSSIAN_MASK_SIZE = 3;
    private final double GAUSSIAN_MASK_SIGMA = 1.0;
    private final int maxIterations2 = GAUSSIAN_MASK_SIZE;

    public Image apply(Image image, int x0, int y0, int x1, int y1, int maxIterations) {
        this.image = image;

        if (phi != null) {
            fixColors(image);
        }
        /* Initial step */
        if (phi == null) {
            initialStep(image, x0, y0, x1, y1);
        }

        int iteration = 0;

        //do {
        while (!end() && iteration++ < maxIterations) {
            cycle(x -> fd(x) > 0, x -> fd(x) < 0);
        }

//        for (int i = 0; i < maxIterations2; i++) {
            //cycle(x -> fs(x) < 0, x -> fs(x) > 0);
//        }
        //} while (!end() && iteration < maxIterations);

//        System.out.println("Iterations: " + iteration);
        return borderCurve();
    }

    private Image borderCurve() {

        Image newImage = image.copy();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Integer phiVal = getPhi(x, y);
                if (phiVal == LIN)
                    newImage.changePixel(x, y, new RGBPixel(0, 255, 0));
                /*else if (phiVal == LOUT)
                    newImage.changePixel(x, y, new RGBPixel(0, 0, 255));
                else if (phiVal == BACKGROUND)
                    newImage.changePixel(x, y, new RGBPixel(255, 255, 255));
                else if (phiVal == OBJECT)
                    newImage.changePixel(x, y, new RGBPixel(255, 0, 0));
                else
                    throw new IllegalStateException("Wrong Phi value");*/
            }
        }

        return newImage;
    }

    private void cycle(Predicate<IntPair> loutPredicate, Predicate<IntPair> linPredicate) {
        /* Step 2 */
        Set<IntPair> loutRemove = lout.stream().filter(loutPredicate).collect(Collectors.toSet());
        switchIn(loutRemove);

        /* Step 3 */
        fix(lin.iterator(), n -> getPhi(n.getX(), n.getY()) < 0, OBJECT);

        /* Step 4 */
        Set<IntPair> linRemove = lin.stream().filter(linPredicate).collect(Collectors.toSet());
        switchOut(linRemove);

        /* Step 5 */
        fix(lout.iterator(), n -> getPhi(n.getX(), n.getY()) > 0, BACKGROUND);
    }

    private void switchIn(Set<IntPair> loutRemove) {
        for (IntPair position : loutRemove) {
            lout.remove(position);

            lin.add(position);
            acumObject(image.getPixel(position.getX(), position.getY()));
            objectPixels++;

            setPhi(position, LIN);
            checkNeighbours(position, lout, BACKGROUND, LOUT);
        }
    }

    private void switchOut(Set<IntPair> linRemove) {
        for (IntPair position : linRemove) {
            lin.remove(position);

            reduceObject(image.getPixel(position.getX(), position.getY()));
            objectPixels--;

            lout.add(position);

            setPhi(position, LOUT);
            checkNeighbours(position, lin, OBJECT, LIN);
        }
    }

    private void fix(Iterator<IntPair> iterator, Predicate<IntPair> predicate, int newPhi) {
        while (iterator.hasNext()) {
            IntPair pixelInSet = iterator.next();
            List<IntPair> neighbours = fourNeighbours(pixelInSet);
            boolean removeFromSet = neighbours.stream().allMatch(predicate);
            if (removeFromSet) {
                iterator.remove();
                setPhi(pixelInSet, newPhi);
            }
        }
    }

    private void checkNeighbours(IntPair pair, Set<IntPair> collection, int oldPhi, int newPhi) {
        List<IntPair> neighbours = fourNeighbours(pair);
        for (IntPair neighbour : neighbours) {
            if (getPhi(neighbour.getX(), neighbour.getY()).equals(oldPhi)) {
                collection.add(neighbour);
                setPhi(neighbour, newPhi);
            }
        }
    }

    private List<IntPair> fourNeighbours(IntPair pair) {

        List<IntPair> neighbours = Arrays.asList(
                new IntPair(pair.getX() + 1, pair.getY()),
                new IntPair(pair.getX() - 1, pair.getY()),
                new IntPair(pair.getX(), pair.getY() + 1),
                new IntPair(pair.getX(), pair.getY() - 1));

        return neighbours.stream().filter(p -> validCoords(p.getX(), p.getY())).collect(Collectors.toList());
    }

    private boolean validCoords(Integer x, Integer y) {
        return x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight();
    }

    private boolean end() {
        return lout.stream().allMatch(p -> fd(p) <= 0) && lin.stream().allMatch(p -> fd(p) >= 0);
    }

    /*
        || Theta_1(x) - Theta(x) || < cota => Fd = 1
     */
    private double fd(IntPair pair) {

        Pixel p = image.getPixel(pair.getX(), pair.getY());

        double inRed = norm(objectRed / (double) objectPixels, p.getRed());
        double inGreen = norm(objectGreen / (double) objectPixels, p.getGreen());
        double inBlue = norm(objectBlue / (double) objectPixels, p.getBlue());

        double inNorm = Math.sqrt(inRed + inGreen + inBlue);

        return inNorm < 60 ? 1 : -1;
    }

    private double norm(double average, double color) {
        return Math.pow(average- color, 2);
    }

    private double fs(IntPair pair) {

        GaussianMask gaussianMask = new GaussianMask(GAUSSIAN_MASK_SIZE, GAUSSIAN_MASK_SIGMA, new GaussianWeight());

        Image phiImage = phiImage();

        List<Pixel> maskPixels = maskPixels(pair, phiImage);

        Pixel p = image.getWeightedValue(maskPixels, gaussianMask.getMask());

        return p.getRed();
    }

    // 3x3 mask
    private List<Pixel> maskPixels(IntPair pair, Image phiImage) {

        int x = pair.getX();
        int y = pair.getY();

        List<Pixel> pixels = new ArrayList<>();

        return null;
    }


    private Image phiImage() {

        List<Pixel> pixels = new ArrayList<>(image.getPixels().size());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Integer phiVal = getPhi(x, y);
                pixels.add(new RGBPixel(phiVal, phiVal, phiVal));
            }
        }

        return new Image(image.getWidth(), image.getHeight(), pixels, Image.ImageType.GRAY_SCALE, Image.Format.JPEG);
    }

    private void initialStep(Image image, int x0, int y0, int x1, int y1) {
        phi = new ArrayList<>(this.image.getPixels().size());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Pixel p = image.getPixel(x, y);
                //Object
                if (x > x0 && x < x1 && y > y0 && y < y1) {
                    phi.add(OBJECT);
                    acumObject(p);
                    objectPixels++;
                } else
                    //LIN
                    if (((x == x0 || x == x1) && (y >= y0 && y <= y1)) ||
                            ((y == y0 || y == y1) && (x >= x0 && x <= x1))) {

                        lin.add(new IntPair(x, y));
                        phi.add(LIN);
                        acumObject(p);
                        objectPixels++;

                    } else
                        //LOUT
                        if (((x == x0 - 1 || x == x1 + 1) && (y >= y0 && y <= y1)) ||
                                ((y == y0 - 1 || y == y1 + 1) && (x >= x0 && x <= x1))) {

                            lout.add(new IntPair(x, y));
                            phi.add(LOUT);

                        } else { //Background
                            phi.add(BACKGROUND);
                        }
            }
        }
    }

    private void acumObject(Pixel p) {
        objectRed += p.getRed();
        objectGreen += p.getGreen();
        objectBlue += p.getBlue();
    }

    private void reduceObject(Pixel p) {
        objectRed -= p.getRed();
        objectGreen -= p.getGreen();
        objectBlue -= p.getBlue();
    }

    private void setPhi(IntPair pair, int value) {
        int index = getIndex(pair.getX(), pair.getY());
        phi.set(index, value);
    }

    private Integer getPhi(int x, int y) {
        int index = getIndex(x, y);
        return phi.get(index);
    }

    private int getIndex(int x, int y) {
        return x + (y * image.getWidth());
    }

    private void fixColors(Image image) {
        objectGreen = 0;
        objectBlue = 0;
        objectRed = 0;
        final AtomicInteger index = new AtomicInteger(0);
        image.getPixels().forEach(p -> {
            if (phi.get(index.getAndAdd(1)).equals(OBJECT)) {
                objectBlue += p.getBlue();
                objectRed += p.getRed();
                objectGreen += p.getGreen();
            }
        });
    }
}
