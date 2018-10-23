package santanjm.quoridor;

import javax.swing.*;
import java.awt.*;

import santanjm.quoridor.*;

public class GamePanel extends JPanel {
	// The layout manager for the panel
	GridBagLayout gridbag;
	
	// The constraints for the gridbag
	GridBagConstraints c;
	
	// The labels for the columns of the grid
	JLabel[] columnLabels = new JLabel[9];
	
	// The labels for the rows of the grid
	JLabel[] rowLabels = new JLabel[9];
	
	// The wall panels
	WallPanel[] wallPanels;
	
	/**
	 * Creates a new Game Panel with a gridbag layout and labels for the
	 * columns and rows of the grid
	 */
	public GamePanel() {
		// Initialize the layout manager
		initLayout();
		
		// Initialize the column and row labels
		initLabels();
		
		// Reset some constraints as they will now remain unchanged
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipadx = 0;
		c.ipady = 0;
		
		// The size of the panel
		setSize(675, 675);
	}
	
	/**
	 * Initializes the gridbag layout of the game panel and its constraints
	 */
	private void initLayout() {
		gridbag = new GridBagLayout();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		setLayout(gridbag);
	}
	
	/**
	 * Creates and places the column and row labels for the board in the panel
	 */
	private void initLabels() {
		// Initialize the column and row labels
		for(int i = 0; i < 9; i++) {
			// Create the column label
			c.weightx = 2.0;
			c.ipadx = 10;
			c.ipady = 0;	
			c.gridx = i*2 + 2;
			c.gridy = 1;
			columnLabels[i] = new JLabel(((char) (i+97)) + "", SwingConstants.CENTER);
			gridbag.setConstraints(columnLabels[i], c);
			add(columnLabels[i]);
			
			// Create the row label
			c.gridx = 1;
			c.gridy = i*2 + 2;
			c.ipadx = 5;
			c.ipady = 0;
			rowLabels[i] = new JLabel((i+1) + "", SwingConstants.CENTER);
			gridbag.setConstraints(rowLabels[i], c);
			add(rowLabels[i]);
		}
	}
	
	/**
	 * Adds a space to the game panel at its proper coordinates
	 * 
	 * @param s a space
	 */
	public void addSpace(Space s) {
		int gridCol = s.getColumn()*2 + 2;
		int gridRow = s.getRow()*2 + 2;
		c.gridx = gridCol;
		c.gridy = gridRow;
		gridbag.setConstraints(s.panel, c);
		add(s.panel);
		addNearWalls(s);
		
		c.weightx = 0.0;
	}
	
	/**
	 * Adds the wall panels to the right and below a space if they exist
	 * 
	 * @param s a space
	 */
	public void addNearWalls(Space s) {
		// The column and row of the space
		int col = s.getColumn();
		int row = s.getRow();
		
		// The column and row in the gridbag layout the wall panel will be placed
		int gridCol;
		int gridRow;

		c.ipadx = 0;
		c.ipady = 0;
		
		// Add the vertical wall panel to the right of the space
		if(col != 8) {
			gridCol = s.getColumn()*2 + 3;
			gridRow = s.getRow()*2 + 2;
			c.gridx = gridCol;
			c.gridy = gridRow;
			s.setWallPanel(new WallPanel(9, 70, 0, s), 0);
			
			gridbag.setConstraints(s.getWallPanel(0), c);
			add(s.getWallPanel(0));
		}
		// The horizontal wall panel below the space
		if(row != 8) {
			gridCol = s.getColumn()*2 + 2;
			gridRow = s.getRow()*2 + 3;
			c.gridx = gridCol;
			c.gridy = gridRow;
			s.setWallPanel(new WallPanel(70, 9, 1, s), 1);			
			gridbag.setConstraints(s.getWallPanel(1), c);
			add(s.getWallPanel(1));
		}
		// Add the small corner wall panel southwest of the space
		if(row != 8 && col != 8) {
			gridCol = s.getColumn()*2 + 3;
			gridRow = s.getRow()*2 + 3;
			c.gridx = gridCol;
			c.gridy = gridRow;
			s.setCornerWallPanel(new WallPanel(9));
			gridbag.setConstraints(s.getWallPanel(2), c);
			add(s.getWallPanel(2));
		}
	}
}


















