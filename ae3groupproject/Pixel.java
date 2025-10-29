package uk.ac.nulondon;

import java.awt.*;

/**
 * Represents a pixel with color, energy, and optional left/right neighbors
 */

public class Pixel {
    Pixel left;
    Pixel right;

    /** Energy value of the pixel */
    double energy;

    /** Pixel color */
    final Color color;

    /**
     * Creates a Pixel from an RGB integer
     * @param rgb RBG color value
     */
    public Pixel(int rgb) {
        this.color = new Color(rgb);
    }

    /**
     * Creates a Pixel from a color objects
     * @param color color the pixel with color
     */
    public Pixel(Color color) {
        this.color = color;
    }

    /**
     * Returns the pixel's brightness/average RGB
     * @return
     */
    public double brightness() {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
    }

    /**
     * Returns the green color component
     *
     * @return green value
     */
    public double getGreen() {
        return color.getGreen();
    }
}
