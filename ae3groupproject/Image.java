package uk.ac.nulondon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
/***
   AE3 Project
   Version 5
 ***/
public class Image {
    private final List<Pixel> rows;

    private int width;
    private int height;

    /**
     * Makes image
     * @returns image
     */
    public Image(BufferedImage img) {
        width = img.getWidth();
        height = img.getHeight();
        rows = new ArrayList<>();
        Pixel current = null;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel pixel = new Pixel(img.getRGB(col, row));
                if (col == 0) {
                    rows.add(pixel);
                } else {
                    current.right = pixel;
                    pixel.left = current;
                }
                current = pixel;
            }
        }
    }

    /**
     * Buffered image
     * @return BufferedImage
     */
    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; row++) {
            Pixel pixel = rows.get(row);
            int col = 0;
            while (pixel != null) {
                image.setRGB(col++, row, pixel.color.getRGB());
                pixel = pixel.right;
            }
        }
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Energy calculates each pixel's value
     * @param above Gets above pixel value after initializing
     * @param current Gets current pixel at location value
     * @param below Gets the pixel below row value
     * @return Double
     */
    double energy(Pixel above, Pixel current, Pixel below) {
        //TODO: Calculate energy based on neighbours of the current pixel
        if (above == null || below == null || current.left == null || current.right == null) {
            return current.brightness(); // Use brightness directly for edges
        }
        // Calculate Horizontal Energy for E
        double horizEnergy = (above.left.brightness() + 2 * above.brightness() + above.right.brightness()) -
                (below.left.brightness() + 2 * below.brightness() + below.right.brightness());

        double vertEnergy = (above.left.brightness() + 2 * current.left.brightness() + below.left.brightness()) -
                (above.right.brightness() + 2 * current.right.brightness() + below.right.brightness());

        return Math.sqrt(horizEnergy * horizEnergy + vertEnergy * vertEnergy);
    }

    /**
     * Calculate energy gets us the total energy for pixels in the image
     */
    public void calculateEnergy() {
        //TODO: calculate energy for all the pixels in the image
        // Iterate over each pixel row by row

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Pixel currentPixel = getPixel(row, col);
                /* instead of skipping the edges,
                use condition to find the energy of edges directly */
                if (row == 0 || row == height-1 || col == 0 || col == width-1){
                    currentPixel.energy = currentPixel.brightness();
                } else {
                    Pixel above = getPixel(row - 1, col);
                    Pixel below = getPixel(row + 1, col);
                    currentPixel.energy = energy(above, currentPixel, below);
                }
            }
        }
    }

    // Helper method to retrieve the pixel from the list
    private Pixel getPixel(int row, int col) {
        Pixel currentPixel = rows.get(row);
        for (int i = 0; i < col; i++) {
            currentPixel = currentPixel.right;
        }
        return currentPixel;
    }

    /**
     * Functions highlights a certain seam a certain color, for some reason doesnt make blue
     * We use a double linked list implementation in order to traverse through the seam
     * And highlight each pixel.
     * @param color Is the color we want for pixel
     */

    public List<Pixel> highlightSeam(List<Pixel> seam, Color color) {
        //TODO: highlight the seam, return previous values
        List<Pixel> originalSeam = new ArrayList<>();

        if (seam == null) {
            return null;
        }
        for (int i = 0; i < seam.size(); i++) {
            Pixel currentPixel = seam.get(i);
            originalSeam.add(currentPixel);
            Pixel highlightedPixel = new Pixel(color);

            highlightedPixel.left = currentPixel.left;
            highlightedPixel.right = currentPixel.right;

            if (highlightedPixel.left != null) {
                highlightedPixel.left.right = highlightedPixel;
            }

            if (highlightedPixel.right != null) {
                highlightedPixel.right.left = highlightedPixel;
            }
            if(currentPixel == rows.get(i)){
                rows.set(i, highlightedPixel);
            }

            seam.set(i, highlightedPixel);
        }
        return originalSeam;
    }

    /**
     * RemoveSeam removes a certain pattern of pixels in an image and subtracts width
     * @param seam is the list of pixels in a certain order based on location in the pixel
     */
    public void removeSeam(List<Pixel> seam) {
        //TODO: remove the provided seam
        if (seam == null) {
            return;
        }
        for (int i = 0; i < seam.size(); i++) {
            Pixel pixelRow = seam.get(i);
            if (pixelRow.left != null) {
                pixelRow.left.right = pixelRow.right;
            } else {
                rows.set(i, pixelRow.right);
                pixelRow.right.left = null;
            }
            if (pixelRow.right != null) {
                pixelRow.right.left = pixelRow.left;
            } else {
                rows.set(i, pixelRow.left);
            }
        }
        width--;
    }

    /**
     * addseam  Adds a new pattern of pixel's to the image and adds width
     * @param seam Is the list of pixels in a certain order based on location in the pixel
     */
    public void addSeam(List<Pixel> seam) {
        //TODO: Add the provided seam
        if (seam == null) {
            return;
        }
        for (int i = 0; i < seam.size(); i++) {
            Pixel pixelRow = seam.get(i);
            if(pixelRow.right != null){
                pixelRow.right.left = pixelRow;
            }
            else{
                rows.set(i, pixelRow);
            }
            if(pixelRow.left != null){
                pixelRow.left.right = pixelRow;
            }
            else{
                rows.set(i, pixelRow);
            }
        }
        width++;

    }

    /**
     * This method gets us the highest valued seams for any image by comparing
     * Pixel Values
     * @param valueGetter Gets the value of energy for each pixel to compare
     * @return Function is the list of pixels maximized
     */
    private List<Pixel> getSeamMaximizing(Function<Pixel, Double> valueGetter) {
        //TODO: find the seam which maximizes total value extracted from the given pixel
        if (rows.isEmpty() || width == 0) {
            return new ArrayList<>();
        }
        double[][] dp = new double[height][width];
        int[][] path = new int[height][width];

        Pixel currentPixel = rows.getFirst();
        for (int col = 0; col < width; col++) {
            dp[0][col] = valueGetter.apply(currentPixel);
            path[0][col] = -1;
            currentPixel = currentPixel.right;
        }
        for (int row = 1; row < height; row++) {
            currentPixel = rows.get(row);
            for (int col = 0; col < width; col++) {
                double maximumValue = Double.NEGATIVE_INFINITY;
                int greatPColumn = -1;

                for (int bPColumn = Math.max(0, col - 1);
                     bPColumn <= Math.min(col + 1, width - 1);
                     bPColumn++) {

                    if (dp[row - 1][bPColumn] > maximumValue) {
                        maximumValue = dp[row - 1][bPColumn];
                        greatPColumn = bPColumn;
                    }
                }
                dp[row][col] = maximumValue + valueGetter.apply(currentPixel);
                path[row][col] = greatPColumn;
                currentPixel = currentPixel.right;
            }
        }
        double maxValue = Double.NEGATIVE_INFINITY;
        int endCol = 0;
        for (int col = 0; col < width; col++) {
            if (dp[height - 1][col] > maxValue) {
                maxValue = dp[height - 1][col];
                endCol = col;
            }
        }
        List<Pixel> seam = new ArrayList<>();
        int currentCol = endCol;

        for (int row = height - 1; row >= 0; row--) {
            // Traverse to the correct column
            Pixel pixel = rows.get(row);
            for (int i = 0; i < currentCol; i++) {
                pixel = pixel.right;
            }
            seam.add(0, pixel);

            currentCol = path[row][currentCol];
        }

        return seam;
    }




    /**
     * Gets Greenest colored seam based on valesmathematically
     * @return getSeamMaximizing of green pixel rgb values
     */
    public List<Pixel> getGreenestSeam() {
        return getSeamMaximizing(Pixel::getGreen);
        /*Or, since we haven't lectured on lambda syntax in Java, this can be
        return getSeamMaximizing(new Function<Pixel, Double>() {
            @Override
            public Double apply(Pixel pixel) {
                return pixel.getGreen();
            }
        });*/

    }

    /**
     * Gets the lowest value of seams
     * @return getSeamMaximizing
     */
    public List<Pixel> getLowestEnergySeam() {
        calculateEnergy();
        /*
        Maximizing negation of energy is the same as minimizing the energy.
         */
        return getSeamMaximizing(pixel -> -pixel.energy);

        /*Or, since we haven't lectured on lambda syntax in Java, this can be
        return getSeamMaximizing(new Function<Pixel, Double>() {
            @Override
            public Double apply(Pixel pixel) {
                return -pixel.energy;
            }
        });
        */
    }



}
