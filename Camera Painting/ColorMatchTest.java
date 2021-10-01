import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.*;

/**
 *
 * @author Amanda Sun
 */
public class ColorMatchTest extends DrawingGUI {
    private static final int maxColorDiff = 20;
    // how similar a pixel color must be to the target color, to belong to a region
    private BufferedImage image;

    public ColorMatchTest(String name, RegionFinder finder, Color targetColor) {
        super(name, finder.getImage().getWidth(), finder.getImage().getHeight());

        // Do the region finding and recolor the image.
        finder.findRegions(targetColor);
        finder.recolorImage();
        image = finder.getRecoloredImage();
    }

    public static boolean colorMatch(Color c1, Color c2) {
        // Tf the RGB values are all within the maxColorDifference, return true.

        if (Math.abs(c1.getBlue() - c2.getBlue()) <= maxColorDiff &&
                Math.abs(c1.getRed() - c2.getRed()) <= maxColorDiff &&
                Math.abs(c1.getGreen() - c2.getGreen()) <= maxColorDiff) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        /*
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        Color newColor = new Color(red, green, blue);

        int red1 = (int) red + 20;
        int green1 = (int) green + 41;
        int blue1 = (int) blue + 20;

        System.out.print("red" + red);
        System.out.print("blue" + blue);
        System.out.println("green" + green);

        System.out.println("red" + red1);
        System.out.print("blue" + blue1);
        System.out.print("green" + green1);
        Color color2 = new Color(red1, green1, blue1);

        System.out.println(colorMatch(newColor,color2));

         */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BufferedImage pic = loadImage("pictures/smiley.png");
                new RegionsTest("smiley", new RegionFinder(pic), new Color(0, 0, 0));

            }
        });
    }
}
