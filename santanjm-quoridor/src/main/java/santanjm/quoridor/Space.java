package santanjm.quoridor;

import java.util.Arrays;
import java.awt.*;

import santanjm.quoridor.*;

/**
 * Class which represents a space on the Quoridor board.  A space will know
 * about its position on the grid as well as whether or not it is currently occupied
 * as well as the player which currently occupies it.
 * @author santanjm
 */
public class Space {
	// Two element array which contains the x and y coordinates of this space
	int[] coords;
	
	// Flag which is true when this space is currently occupied and false otherwise
	boolean occupied = false;
	
	// The number corresponding to the player that is currently occupying this space
	int playerNum = 0;
	
	// Array of the spaces around this space: [above, right, below, left]
	Space[] neighbors = new Space[4];
	
	// The panel which will represent this space on the GUI
	public SpacePanel panel;
	
	// The Graphics object used by the Space Panel
	Graphics panelGraphics;
	
	// Array of the wall panels directly left and below the space (one or both may be null)
	// also the small square-shaped wall panel between those two panels
	// wallPanels[0] - right wall
	// wallPanels[1] - below wall
	// wallPanels[2] - corner wall
	WallPanel[] wallPanels = new WallPanel[3];
	
	/**
	 * Constructs a new Space object in the board grid
	 * 
	 * @param col the column (x-coordinate) in the grid to place the new space
	 * @param row the row (y-coordinate) in the grid to place the new space
	 */
	public Space(int col, int row) {
		coords = new int[] {col, row};
		panel = new SpacePanel(col, row);
		panelGraphics = panel.getGraphics();
	}
	
	/**
	 * Test for equality between this space and another, two spaces are equal
	 * if and only if they have the same coordinates
	 * 
	 * @param s the space to test for equality
	 * @return true if the spaces have the same coordinates and false otherwise
	 */
	public boolean equalsSpace(Space s) {
		return Arrays.equals(this.coords, s.getCoords());
	}
	
	/**
	 * Tells this space that it is now occupied by a specified player
	 * 
	 * @param pNum integer corresponding to the player that now occupies this space
	 */
	public void occupy(int pNum) {
		occupied = true;
		playerNum = pNum;
		panel.occupy(pNum);
		panelGraphics = panel.getGraphics();
		panel.update(panelGraphics);
	}
	
	/**
	 * Tells this spaces that it is now no longer occupied by a player
	 */
	public void unoccupy() {
		occupied = false;
		playerNum = 0;
		panel.unoccupy();
		panelGraphics = panel.getGraphics();
		panel.update(panelGraphics);
	}
	
	/**
	 * Calculates the number of spaces away from this space another space is on the board
	 * 
	 * @param other any space
	 * @return the distance between this space and the other space
	 */
	public int distance(Space other) {
		// The column and row of the other space
		int otherCol = other.getColumn();
		int otherRow = other.getRow();
		
		// The difference in column and row between this space and the other
		int colDif = Math.abs(coords[0] - otherCol);
		int rowDif = Math.abs(coords[1] - otherRow);
		
		return colDif + rowDif;
	}
	
	/**
	 * Getter for the column and row of this space in the grid
	 * 
	 * @return array of the column and row of this space in the grid: [col, row]
	 */
	public int[] getCoords() {
		return coords;
	}
	
	/**
	 * Getter for the column of this space in the grid
	 * 
	 * @return the column of this space, coords[0]
	 */
	public int getColumn() {
		return coords[0];
	}
	
	/**
	 * Getter for the row of this space in the grid
	 * 
	 * @return the row of this space, coords[1]
	 */
	public int getRow() {
		return coords[1];
	}
	
	/**
	 * Getter for the array of neighboring spaces
	 * 
	 * @return array of this space's neighbors: [above, right, below, left]
	 */
	public Space[] getNeighbors() {
		return neighbors;
	}
	
	/**
	 * Updates the neighboring spaces of this space with the versions currently
	 * known by the board
	 * 
	 * @param upNeighbors
	 */
	public void updateNeighbors(Space[] upNeighbors) {
		this.neighbors = upNeighbors;
	}
	
	/**
	 * Getter for the number of the player occupying this space
	 * 
	 * @return the player number of the player currently occupying this space
	 */
	public int getPlayerNum() {
		return playerNum;
	}
	
	/**
	 * Getter for one of this space's wall panels
	 * 
	 * @param dir the direction (index) of the wall panel
	 * @return the wall panel at the designated index
	 */
	public WallPanel getWallPanel(int dir) {
		return wallPanels[dir];
	}
	
	/**
	 * Sets a new wall panel for this space in the indicated direction:
	 * 0 - right
	 * 1 - below
	 * 
	 * @param wall a wall panel
	 * @param dir  the direction of the wall panel, 0 for vertical and 1 for horizontal
	 */
	public void setWallPanel(WallPanel wall, int dir) {
		wallPanels[dir] = wall;
	}
	
	/**
	 * Sets a new corner wall panel, the small square panel southwest of the space
	 * 
	 * @param corner a wall panel
	 */
	public void setCornerWallPanel(WallPanel corner) {
		wallPanels[2] = corner;
	}
	
	/**
	 * Tells the wall panel in the indicated direction that it has been placed
	 * and so should re-drawn itself
	 * 
	 * @param dir the direction of the wall to place, 0 for right and 1 for below
	 */
	public void placeWall(int dir) {
		wallPanels[dir].placed = true;
		Graphics wallGraphics = wallPanels[dir].getGraphics();
		wallPanels[dir].update(wallGraphics);
		if(allPlaced()) {
			placeCornerWall();
		}
	}
	
	/**
	 * Places the corner wall for this space if it is not null
	 */
	public void placeCornerWall() {
		if(wallPanels[2] != null) {
			wallPanels[2].placed = true;
			Graphics wallGraphics = wallPanels[2].getGraphics();
			wallPanels[2].update(wallGraphics);
		}
	}
	
	/**
	 * Getter for the occupied flag for this space
	 * 
	 * @return true if space is currently occupied, false otherwise
	 */
	public boolean isOccupied() {
		return occupied;
	}
	
	/**
	 * Checks whether a wall panel in a given direction has been placed or not
	 * 
	 * @param dir the direction of wall panel to check
	 * @return true if the wall panel in the direction has been placed and false if not
	 */
	public boolean isPlaced(int dir) {
		if(dir < 0 || dir > 2) {
			throw new IllegalArgumentException("Direction to check must be between 0 and 2");
		}
		return wallPanels[dir].placed;
	}
	
	/**
	 * Checks whether both wall panels around this space have been placed
	 * and if so, place the corner panel as well
	 * 
	 * @return true if both wall panels have been placed and false otherwise
	 */
	public boolean allPlaced() {
		if((! Arrays.asList(wallPanels).contains(null)) && 
			(wallPanels[0].placed && wallPanels[1].placed)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a String representation of the space
	 */
	public String toString() {
		return "Space at: " + Arrays.toString(coords) + "\nOccupied? " + occupied + "\n";
	}
	
	/**
	 * Returns a string representing a space in the <letter, number> format
	 * 
	 * @return the space's coordinates in the <letter, number> format
	 */
	public String toGrid() {
		char col = (char) (coords[0] + 97);
		char row = (char) (coords[1] + 49);
		
		return "" + col + row;
	}
}








