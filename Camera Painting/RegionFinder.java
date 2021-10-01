
import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * @author Amanda Sun, Fall 2021, updated to include floodfillAlgorithm, regionFinder, and more to allow static and dynamic identification of the largest similar-colored regions
 */
public class RegionFinder {
    private static final int maxColorDiff = 19;                // how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 5;                // how many points in a region to be worth considering

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored
    private BufferedImage visited;

    private ArrayList<ArrayList<Point>> regions = new ArrayList<ArrayList<Point>>();            // a region is a list of points
    // so the identified regions are in a list of lists of points

    public RegionFinder() {
        this.image = null;
        this.visited = null;
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
        this.visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    // toString method to debug
    public String toString(){
        return "Region Finder";
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    /**
     * @return The regions' size.
     */
    public int regionsSize() {return regions.size();}

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */
    public void findRegions(Color targetColor) {
        // Loop through image, starting from upper left, going down
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (visited.getRGB(x, y) == 0 && colorMatch(new Color(image.getRGB(x, y)), targetColor)) { // If this point has not been visited and has correct color
                    ArrayList<Point> colorReg = floodFillAlgorithm(x, y, targetColor); // floodfillAlgorithm returns a region of points of the same color
                    if (colorReg.size() >= minRegion) { // checks this size before adding the region
                        regions.add(0, colorReg);

                    }
                }
            }
        }
    }

    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
     */
    private static boolean colorMatch(Color c1, Color c2) {
        if (Math.abs(c1.getBlue() - c2.getBlue()) <= maxColorDiff &&
                Math.abs(c1.getRed() - c2.getRed()) <= maxColorDiff &&
                Math.abs(c1.getGreen() - c2.getGreen()) <= maxColorDiff) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Based on breadth-first search, floodFillAlgorithm returns an ArrayList region of points that are of the right color.
     * @param x The starting point's x-value, which is the right color and hasn't been visited.
     * @param y The starting point's y-value, which is the right color and hasn't been visited.
     * @param targetColor
     * @return An ArrayList region of points that are of the right color.
     */
    public ArrayList<Point> floodFillAlgorithm(int x, int y, Color targetColor) {
        ArrayList<Point> coloredPts = new ArrayList<Point>();
        ArrayList<Point> toVisit = new ArrayList<Point>();
        Point firstPt = new Point(x, y);
        toVisit.add(firstPt);
        coloredPts.add(firstPt); // first one won't get added to coloredPts so doing it here
        visited.setRGB(x, y, 1); //prevents while loop from looking at this pt again

        while (!toVisit.isEmpty()) {
            // dequeue front node and process it
            Point p = toVisit.get(0);
            toVisit.remove(0);
            // (x, y) represents the current pixel
            x = p.x;
            y = p.y;

            // process all eight adjacent pixels of the current pixel and
            // enqueue each valid pixel
            for (int anX = Math.max(x - 1, 0); anX <= Math.min(x + 1, image.getWidth()-1); anX++) {
                for (int aY = Math.max(y - 1, 0); aY <= Math.min(y + 1, image.getHeight()-1); aY++) {
                    if (visited.getRGB(anX, aY) == 0) { //If this point has not been visited
                        visited.setRGB(anX, aY, 1);
                        Color aColor = new Color(image.getRGB(anX, aY));
                        if (colorMatch(aColor, targetColor)) { //If its also the right color
                            // enqueue adjacent pixel
                            Point aPt = new Point(anX, aY);
                            toVisit.add(aPt);
                            coloredPts.add(aPt);
                        }
                    }
                }
            }
        }
        return coloredPts;
    }

    /**
     * Returns the largest region detected (if any region has been detected).
     */
    public ArrayList<Point> largestRegion() {
        int biggestIndex = 0;

        // Loops through regions list to find the list of points with the biggest size
        for (int i = 1; i < regions.size(); i++) {
            if (regions.get(i).size() > regions.get(biggestIndex).size()) {
                biggestIndex = i;
            }
        }

        return regions.get(biggestIndex);
    }

    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        // Now recolor the regions in it
        // TODO: YOUR CODE HERE

        for (int regionIndex = 0; regionIndex < regions.size(); regionIndex++) { // loop over regions
            int red = (int) (Math.random() * 256); // for a region, same color
            int green = (int) (Math.random() * 256);
            int blue = (int) (Math.random() * 256);
            int randRGB = (new Color(red, green, blue)).getRGB();

            for (int pointIndex = 0; pointIndex < regions.get(regionIndex).size(); pointIndex++) { // For the region, loop over its points
                Point ptAddress = regions.get(regionIndex).get(pointIndex);
                int x = (int) ptAddress.getX();
                int y = (int) ptAddress.getY();

                recoloredImage.setRGB(x, y, randRGB); //PROBLEM WAS IN THE SETRGB


            }
        }


    }
}
