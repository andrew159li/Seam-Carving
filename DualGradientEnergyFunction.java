package seamcarving;

import edu.princeton.cs.algs4.Picture;

public class DualGradientEnergyFunction implements EnergyFunction {
    @Override
    public double apply(Picture picture, int x, int y) {
        if (x < 0 || x > picture.width() - 1 || y < 0 || y > picture.height() -1) {
            throw new IllegalArgumentException();
        }

        return Math.sqrt(getEnergyX(picture, x, y) + getEnergyY(picture, x, y));
    }

    private double getEnergyX(Picture pic, int x, int y) {
        int delta;

        if (x == pic.width() - 1) { //backward
            delta = -1;
        } else if (x == 0) { //forward
            delta = 1;
        } else { //central
            delta = 0;
            double rDiff = Math.pow(pic.get(x + 1, y).getRed() - pic.get(x - 1, y).getRed(), 2);
            double gDiff = Math.pow(pic.get(x + 1, y).getGreen() - pic.get(x - 1, y).getGreen(), 2);
            double bDiff = Math.pow(pic.get(x + 1, y).getBlue() - pic.get(x - 1, y).getBlue(), 2);
            return rDiff + gDiff + bDiff;
        }
        double rDiff =
            Math.pow(-3 * pic.get(x, y).getRed() + 4 * pic.get(x + delta, y).getRed()
                - pic.get(x + (2 * delta), y).getRed(), 2);
        double gDiff =
            Math.pow(-3 * pic.get(x, y).getGreen() + 4 * pic.get(x + delta, y).getGreen()
                - pic.get(x + (2 * delta), y).getGreen(), 2);
        double bDiff =
            Math.pow(-3 * pic.get(x, y).getBlue() + 4 * pic.get(x + delta, y).getBlue()
                - pic.get(x + (2 * delta), y).getBlue(), 2);

        return rDiff + gDiff + bDiff;
    }

    private double getEnergyY(Picture pic, int x, int y) {
        int delta;

        if (y == pic.height() - 1) { //backward
            delta = -1;
        } else if (y == 0) { //forward
            delta = 1;
        } else { //central
            delta = 0;
            double rDiff = Math.pow(pic.get(x, y + 1).getRed() - pic.get(x, y - 1).getRed(), 2);
            double gDiff = Math.pow(pic.get(x, y + 1).getGreen() - pic.get(x, y - 1).getGreen(), 2);
            double bDiff = Math.pow(pic.get(x, y + 1).getBlue() - pic.get(x, y - 1).getBlue(), 2);
            return rDiff + gDiff + bDiff;
        }
        double rDiff =
            Math.pow(-3 * pic.get(x, y).getRed() + 4 * pic.get(x, y + delta).getRed()
                - pic.get(x, y + (2 * delta)).getRed(), 2);
        double gDiff =
            Math.pow(-3 * pic.get(x, y).getGreen() + 4 * pic.get(x, y + delta).getGreen()
                - pic.get(x, y + (2 * delta)).getGreen(), 2);
        double bDiff =
            Math.pow(-3 * pic.get(x, y).getBlue() + 4 * pic.get(x, y + delta).getBlue()
                - pic.get(x, y + (2 * delta)).getBlue(), 2);

        return rDiff + gDiff + bDiff;
    }
}
