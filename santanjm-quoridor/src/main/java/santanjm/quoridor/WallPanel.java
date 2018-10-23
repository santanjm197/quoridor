package santanjm.quoridor;

import javax.swing.*;
import java.awt.*;

import santanjm.quoridor.*;

public class WallPanel extends JComponent {
	// The width of height of the wall panel, varies depending on its orientation
	int width;
	int height;
	
	// Flag representing the direction of this wall panel
	// 0 - vertical
	// 1 - horizontal
	int dir;
	
	// The column of this wall panel in the grid.  For a vertical wall,
	// this will be the column of the space to its immediate left and for
	// a horizontal wall, this will be the column of the space immediately above it
	// The row is designated in exactly the same way
	int col;
	int row;
	
	// Flag telling the wall panel whether to be visible or not
	boolean placed;
	
	/**
	 * Constructs a new WallPanel with a given width, height and direction
	 * 
	 * @param wallWidth     the width of the wall panel
	 * @param wallHeight    the height of the wall panel
	 * @param wallDirection the direction of this wall, 0 for vertical and 1 for horizontal
	 * @param designator    the space directly left (for vertical wall) or above (horizontal)
	 */
	public WallPanel(int wallWidth, int wallHeight, int wallDirection, Space designator) {
		super();
		width = wallWidth;
		height = wallHeight;
		dir = wallDirection;
		col = designator.getColumn();
		row = designator.getRow();
	}
	
	/**
	 * Constructs a new wall panel with just a width and height: used for the
	 * small squares at the center of walls
	 * 
	 * @param wallWidth  the width (and height) of the wall
	 */
	public WallPanel(int wallWidth) {
		super();
		width = wallWidth;
		height = wallWidth;
	}
	
	/**
	 * Overridden paintComponent method for the Wall Panel.  If the wall has not
	 * been placed it is drawn as a simple black rectangle, once placed the rectangle
	 * gets filled in
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(placed) {
			g.fillRect(0, 0, width, height);
		} else {
			g.drawRect(0, 0, width, height);
		}	
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}
