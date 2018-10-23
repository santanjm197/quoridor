package santanjm.quoridor;

import javax.swing.*;
import java.awt.*;

import santanjm.quoridor.*;

public class SpacePanel extends JComponent {
	// The width and height of every Space is constant
	private static final int SPACE_WIDTH = 70;
	private static final int SPACE_HEIGHT = SPACE_WIDTH;
	
	// The x and y coordinates of each Space varies
	int spacex;
	int spacey;
	
	// Flag to tell whether the controlling space is occupied or not
	boolean occupied;
	
	// The number of the player that is currently occupying the space
	int playerNum;
	
	/**
	 * Constructs a basic Space Panel with the given coordinates
	 * 
	 * @param x the x coordinate of the Space on the screen
	 * @param y the y coordinate of the Space on the screen
	 */
	public SpacePanel(int x, int y) {
		super();
		spacex = x;
		spacey = y;
	}
	
	/**
	 * Tells this space that it is now occupied by a specified player
	 * 
	 * @param pNum integer corresponding to the player that now occupies this space
	 */
	public void occupy(int pNum) {
		occupied = true;
		playerNum = pNum;
	}
	
	/**
	 * Tells this spaces that it is now no longer occupied by a player
	 */
	public void unoccupy() {
		occupied = false;
		playerNum = 0;
	}
	
	/**
	 * Overriden painComponent method for the Space Panel.  If the space is occupied
	 * it fills the space with an oval of that player's color.  Otherwise it is
	 * simply an empty square
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(! occupied) {
			g.clearRect(0, 0, SPACE_WIDTH, SPACE_HEIGHT);
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, SPACE_WIDTH, SPACE_HEIGHT);
		
		// If the space is occupied we want to draw the correct pawn there
		if(occupied) {
			switch(playerNum) {
				case 1: g.setColor(Color.RED);
						g.fillOval(0, 0, SPACE_WIDTH, SPACE_HEIGHT);
						break;
				case 2: g.setColor(Color.BLUE);
						g.fillOval(0, 0, SPACE_WIDTH, SPACE_HEIGHT);
						break;
				case 3: g.setColor(Color.YELLOW);
						g.fillOval(0, 0, SPACE_WIDTH, SPACE_HEIGHT);
						break;
				case 4: g.setColor(Color.GREEN);
						g.fillOval(0, 0, SPACE_WIDTH, SPACE_HEIGHT);
						break;
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SPACE_WIDTH, SPACE_HEIGHT);
	}
}



















