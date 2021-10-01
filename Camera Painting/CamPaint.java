import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Webcam-based drawing
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Amanda Sun, Fall 2021 September 30th
 *
 * This edition can now recolor the image, paint it with a specified color, and just load a webcam upon command.
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece
	private BufferedImage recoloredImage;   // the recolored webcam image
	private boolean doOnce = true;          // instantiates painting, recoloredImage again in processImage, because the constructor is not fed the correct image upon start of program

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		super(); // calls the Webcam's constructor
		finder = new RegionFinder();
		painting = image;

	}

	/**
	 * Resets the painting to a blank image
	 */ //painting should be painting on a white background,
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		targetColor = null; // resets targetColor

	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting,
	 * depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		if(displayMode == 'w'){super.draw(g);}

		else if (displayMode=='r') {
			if (targetColor != null) {g.drawImage(recoloredImage, 0, 0, null);}
			else {super.draw(g);} //so if there is no color, just shows the webcam
		}
		else if (displayMode=='p'){
			if(targetColor !=null){g.drawImage(painting,0,0,null);}
			else{super.draw(g);} //so if there is no color, just shows the webcam
		}


	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		// TODO: YOUR CODE HERE

		// instantiates variables one time, doOnce is true upon first running
		if(doOnce){
			painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			recoloredImage = image;
			doOnce=false;
		}
		if(targetColor!=null && super.image != null) { //this is getting all the recolored regions
			finder = new RegionFinder(image); // feed new image
			finder.findRegions(targetColor);
			finder.recolorImage();
			recoloredImage = finder.getRecoloredImage();

			if(finder.regionsSize()!=0 && displayMode=='p'){ // if in paint mode and the finder has found a region
				ArrayList<Point> brush = finder.largestRegion(); // brush accumulates all the largest regions
				for (int i = 0; i < brush.size(); i++) {
					painting.setRGB((int) brush.get(i).getX(), (int) brush.get(i).getY(), paintColor.getRGB()); // changes painting color at brush location
				}
			}
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE
		targetColor = new Color(image.getRGB(x,y));
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}


