package santanjm.quoridor;

import java.awt.Color;
import java.util.*;
import javax.swing.*;

import santanjm.quoridor.*;

/**
 * Class which 'runs' the game of Quoridor.  The board determines how many players there are,
 * the positions of every player and wall and whether a submitted move is legal or not
 * @author santanjm
 */
public class Board {
    // The matrix which makes up the 9x9 grid of spaces
    Space[] spaces = new Space[81];
    
    // Map of each space that designates a wall to a 0 or a 1:
    // 0 - wall is vertical
    // 1 - wall is horizontal
    Map<Space, Integer> walls = new HashMap<Space, Integer>();
    
    // The list of players in the game
    Player[] players;
    
    // The player whose turn it currently is
    Player active;
    
    // The number of players in the game
    public int numPlayers;

    // The current turn number
    public int turn = 0;
    
    // The game panel in which the game is displayed
    GamePanel gamepanel;

    /**
     * Simplest constructor for a board, only creating the grid of spaces
     * and nothing else
     */
    public Board() {
    	createGrid();
    }
    
    /**
     * Creates a board with a single player, used by the player to determine
     * paths on the board
     * 
     * @param p a player object
     */
    public Board(Player p) {
    	createGrid();
    	players = new Player[] {p};
    	placePlayer(p.getColumn(), p.getRow());
    }
    
    /**
     * Constructs a board with a single player and a pre-existing map of walls,
     * solely used in checking for legality of wall placements and shortest path
     * calculations
     * 
     * @param p        a player object
     * @param wallsMap a map of walls from their designating space to their direction
     */
    public Board(Player p, Map<Space, Integer> wallsMap) {
    	this(p);
    	walls = wallsMap;
    }
    
    /**
     * Constructs a new Board object with a specified number of players (2 or 4)
     * 
     * @param numPlayers the number of players in the game
     */
    public Board(int numPlayers) {
    	// If the number of players is not 2 or 4, then we cannot continue
    	if(numPlayers != 2 && numPlayers != 4) {
    		throw new IllegalArgumentException("Game can only be played with 2 or 4 players");
    	}
    	gamepanel = new GamePanel();
    	this.numPlayers = numPlayers;
    	createGrid();
    	if(numPlayers == 2) {
    		players = new Player[2];
    		addPlayer(1, 10);
    		addPlayer(2, 10);
    	} else if(numPlayers == 4) {
    		players = new Player[4];
    		addPlayer(1, 5);
    		addPlayer(2, 5);
    		addPlayer(3, 5);
    		addPlayer(4, 5);
    	}
    	setActivePlayer();
    }

    /**
     * Construct the 9x9 grid of Spaces which will make up the board
     */
    private void createGrid() {
    	// We construct the grid row by row
    	for(int i = 0; i < 9; i++) {
    		for(int j = 0; j < 9; j++) {
    			spaces[9*i + j] = new Space(j, i);
    			if(gamepanel != null) {
    				gamepanel.addSpace(spaces[9*i + j]);
    			}
    		}
    	}
    	// Each space has 2 to 4 neighboring spaces which we set for each space
    	setNeighbors();
    }
    
    /**
     * Creates, for each space in spaces, a 4 element array containing
     * the 4 spaces which neighbor that space: [above, right, below, left]
     */
    private void setNeighbors() {
    	for(int i = 0; i < 9; i++) {
    		for(int j = 0; j < 9; j++) {
    			// The index of the current space in spaces
    			int index = 9*i + j;
    			
    			// The coordinates of the space
    			int[] coords = spaces[index].getCoords();
    			
    			// Array of the four spaces around the space
    			Space[] neighbors = new Space[4];
    			
    			// Top neighbor, which will be spaces[index - 9]
    			if(coords[1] > 0) {
    				neighbors[0] = spaces[index-9];
    			} else {
    				neighbors[0] = null;
    			}
    			
    			// Right neighbor, which will be spaces[index + 1]
    			if(coords[0] < 8) {
    				neighbors[1] = spaces[index+1];
    			} else {
    				neighbors[1] = null;
    			}
    			
    			// Bottom neighbor, which will be spaces[index + 9]
    			if(coords[1] < 8) {
    				neighbors[2] = spaces[index+9];
    			} else {
    				neighbors[2] = null;
    			}
    			
    			// Left neighbor, which will be spaces[index - 1]
    			if(coords[0] > 0) {
    				neighbors[3] = spaces[index-1];
    			} else {
    				neighbors[3] = null;
    			}
    			
    			// Finally, set the neighbors of the space
    			spaces[index].updateNeighbors(neighbors);
    		}
    	}
    }
    
    /**
     * Adds a new player to the board with the given number and number of walls
     * 
     * @param playerNum the number of the new player
     * @param numWalls  the number of walls the new player will start with
     */
    private void addPlayer(int playerNum, int numWalls) {
    	players[playerNum - 1] = new Player(this, playerNum, numWalls);
    	switch(playerNum) {
    		case 1: placePlayer(players[0], 4, 8);
    				break;
    		case 2: placePlayer(players[1], 4, 0);
    				break;
    		case 3: placePlayer(players[2], 0, 4);
    				break;
    		case 4: placePlayer(players[3], 8, 4);
    				break;
    	}
    }
    
    /**
     * Adds a single player to the board and places them at wherever their
     * coordinates say they should be placed
     * 
     * @param p a player object
     */
    public void addPlayer(Player p) {
    	players = new Player[] {p};
    	placePlayer(p.getColumn(), p.getRow());
    }
    
    /**
     * The game of Quoridor's main loop
     */
    public void gameLoop() {
    	Scanner moveReader = new Scanner(System.in);
    	JFrame gameframe = new JFrame("Quoridor");
    	gameframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	gameframe.setResizable(false);
    	gameframe.add(gamepanel);
    	gameframe.pack();
    	gameframe.setSize(gameframe.getPreferredSize());
    	gameframe.setVisible(true);
    	while(true) {
    		// First, set the active player
    		setActivePlayer();
    		
    		// Now we print out the current turn number and the game status
    		System.out.println("Turn " + (turn + 1) + ": " 
    						  + "\n" + "The current game status:\n" + currentGameStatus());
    		
    		// Next, we prompt the active player for a move
    		System.out.print("Enter a move player " + active.getPlayerNum() + ": ");
    		String move = moveReader.nextLine();
    		
    		// First we force the move to be all lower case
        	move = move.toLowerCase();
        	
        	// Next we strip any and all whitespace from the move submitted
        	move = move.replaceAll("\\s+", "");
        	
    		if(isLegalMove(move)) {
    			executeMove(move);
    			System.out.println("Move succeeded");
    			System.out.println(active + "\n");
    			
    			// Update all player's boards
    			for(Player p : players) {
    				p.updateBoard(this);
    				if(p.checkHasWon()) {
    					System.out.println("Player " + p.getPlayerNum() + " has won the game!!!");
    					return;
    				}
    			}
    			turn++;
    		} else {
    			System.out.println("ILLEGAL MOVE");
    		}
    	}
    }
    
    /**
     * Returns a string representing the game's current status: player positions and walls
     * 
     * @return string representing the current state of the game board
     */
    public String currentGameStatus() {
    	String status = "Player Positions and Remaining Walls:\n";
    	
    	for(Player p : players) {
    		status += p + "\n";
    	}
    	status += "Current Walls:\n";
    	for(Space designator : walls.keySet()) {
    		status += designator.toGrid() + " " + walls.get(designator) + "\n";
    	}
    	
    	return status;
    }
    
    /**
     * Determines whether a player's submitted move would be legal by the rules of Quoridor
     * 
     * @param move the string representing the player's attempted move, moves will be in the
     * 		  form outlined in Lisa Glendenning's thesis. See:
     *        https://en.wikipedia.org/wiki/Quoridor#Notation
     * @return true if the move is legal, false if not
     */
    public boolean isLegalMove(String move) {
    	// If the move string is not exactly 2 or 3 characters long it must be malformed
    	if(move.length() < 2 || move.length() > 3) {
    		throw new IllegalArgumentException("The move submitted is malformed");
    	}
    	// Flag saying whether or not the move is legal, this may change as it goes
    	boolean isLegal = false;
    	
    	// The coordinates of the currently active player (the one who submitted the move)
    	int actCol = active.getColumn();
    	int actRow = active.getRow();
    	Space actSpace = getSpaceFromCoords(actCol, actRow);
    	
    	// Whether the move is a pawn movement or a wall placement, the first
    	// two characters in the string will designate a space with a letter and a number
    	int col = ((int) move.charAt(0)) - 97;
		int row = ((int) move.charAt(1)) - 49;
		
		// In order for the move to be potentially legal, the first character must be
		// a letter between 'a' and 'i', and the second character must be a number 
		// between '1' and '9'
		if((col < 0 || col > 8) || (row < 0 || row > 8)) {
			throw new IllegalArgumentException("The move submitted is malformed");
		}
		
		// The space the active player is attempting to either move to or
		// place a wall relative to
		Space moveSpace = getSpaceFromCoords(col, row);
    	
    	checkLegalMove:
	    	// A move string with length 2 is an attempted pawn move
	    	if(move.length() == 2) {    		
	    		// Case 1: The space is more than 2 spaces away which is never legal
	    		// or the space is already occupied by another player
	    		if((distance(actSpace, moveSpace) > 2) || (isOccupied(moveSpace))) {
	    			isLegal = false;
	    			break checkLegalMove;
	    		}
	    		
	    		// NOTE: From this point on in the method, we have a guarantee that
	    		// the space the player is attempting to move to is definitely unoccupied
	    			
	    		// Case 2: The player is attempting a standard move to an adjacent space
	    		// which may or may not be blocked by an existing wall
	    		if(distance(actSpace, moveSpace) == 1) {
	    			if(isBlocked(actSpace, moveSpace)) {
	    				isLegal = false;
	    				break checkLegalMove;
	    			} else {
	    				isLegal = true;
	    				break checkLegalMove;
	    			}
	    		}
	    		
	    		// Case 3: The space is exactly 2 spaces away which is legal only when
	    		// a pawn will be jumped to get there
	    		if(distance(actSpace, moveSpace) == 2) {
	    			if(isLegalJump(moveSpace)) {
	    				isLegal = true;
	    				break checkLegalMove;
	    			} else {
	    				isLegal = false;
	    				break checkLegalMove;
	    			}
	    		}	
	    	}
		
		checkLegalWall:
			// A move string with length 3 is an attempted wall placement
			if(move.length() == 3) {
				// The third character in a wall placement move is 'v' or 'h', designating
				// the direction of the wall (vertical or horizontal) we use 0 for vertical
				// and 1 for horizontal
				int dir;
				switch(move.charAt(2)) {
					case 'v': dir = 0;
							  break;
					case 'h': dir = 1;
							  break;
					default: throw new IllegalArgumentException("Direction of wall must be 'v' or 'h'");
				}
				// Case 1: The player is attemping to place a wall when
				// that player has no more walls left to place
				if(active.walls == 0) {
					isLegal = false;
					break checkLegalWall;
				}
				
				// Case 2: The player is attempting to place a wall relative
				// to a space on row 9 or column 9 which is always illegal
				if(col == 8 || row == 8) {
					isLegal = false;
					break checkLegalWall;
				}
				
				// Case 3: The player is attempting to place a wall with a
				// designator space that already designates a wall on the board
				if(isDesignated(moveSpace)) {
					isLegal = false;
					break checkLegalWall;
				}
				
				// Case 4: The player is attempting to place a wall which
				// would intersect an existing wall
				if(willIntersect(moveSpace, dir)) {
					isLegal = false;
					break checkLegalWall;
				}
				
				// Case 5: The player is attempting to place a wall that would
				// cut off another player's only route to reaching their goal
				for(Player p : players) {
					if(! p.canReachGoal(moveSpace, dir)) {
						isLegal = false;
						break checkLegalWall;
					}
				}
				
				// Case 6: The wall placement is legal
				isLegal = true;
			}
    	
    	return isLegal;
    }
    
    /**
     * Checks the legality of a player move (not wall placement) and places
     * the player at that position if the move would be legal and just returns
     * false if not.  NOTE this method only checks legality of single space moves
     * and ignores whether the space is occupied by another player.  A move will
     * only be considered illegal here if it is blocked by a wall
     * 
     * @param playerNum one of the players in the game
     * @param col       column of space to move to
     * @param row       row of space to move to
     * @return true if the player can move there legally and false if not
     */
    public boolean isLegalMove(int playerNum, int col, int row) {
    	Player p = getPlayerFromNum(playerNum);
    	Space curr = getSpaceFromCoords(p.getColumn(), p.getRow());
    	Space goal = getSpaceFromCoords(col, row);
    	if(! isBlocked(curr, goal)) {
    		placePlayer(p, col, row);
    		return true;
    	}
    	return false;
    }
    
    /**
     * Executes a move on the board from the given move string.  NOTE when this method
     * is called, we have a guarantee that the move was legal
     * 
     * @param move string representing a legal player move
     */
    public void executeMove(String move) {
    	move = move.toLowerCase();
    	
    	// Whether the move is a pawn movement or a wall placement, the first
    	// two characters in the string will designate a space with a letter and a number
    	int col = ((int) move.charAt(0)) - 97;
		int row = ((int) move.charAt(1)) - 49;
		Space moveSpace = getSpaceFromCoords(col, row);
		
		// Pawn movement
    	if(move.length() == 2) {
    		placePlayer(active, col, row);
    	
    	// Wall placement
    	} else if(move.length() == 3) {
    		int dir;
			switch(move.charAt(2)) {
				case 'v': dir = 0;
						  break;
				case 'h': dir = 1;
						  break;
				default: throw new IllegalArgumentException("Direction of wall must be 'v' or 'h'");
			}
			placeWall(moveSpace, dir);
    	}
    }
    
    /**
     * Places a player at a specific space on the board
     * 
     * @param p   the player to be placed
     * @param col the column in which the player will be placed
     * @param row the row in which the player will be placed
     */
    public void placePlayer(Player p, int col, int row) {
    	// The player number of player p
    	int pNum = p.getPlayerNum();
    	
    	// The space at position [col, row] in spaces will be at position [9*row + col]
    	spaces[9*row + col].occupy(pNum);
    	
    	// We need to ensure that we unoccupy the space that the player just moved from,
    	// assuming that they moved at all
    	int[] oldPos = players[pNum-1].getPos();
    	if(! Arrays.equals(oldPos, new int[] {col, row})) {
	    	int oldCol = oldPos[0];
	    	int oldRow = oldPos[1];
	    	spaces[9*oldRow + oldCol].unoccupy();
    	}
    	
    	// Finally, set the position of the player to the new coordinates
    	players[pNum-1].setPos(col, row);
    }
    
    /**
     * NOTE that this method assumes the board only has ONE player on it.  Places
     * the player at the specified coordinates on the board
     * 
     * @param col the column in which the player will be placed
     * @param row the row in which the player will be placed
     */
    public void placePlayer(int col, int row) {
    	// This method assumes only ONE player is one the board
    	if(players.length != 1) {
    		throw new UnsupportedOperationException("Only boards with exactly one player may call");
    	}
    	
    	// The space at position [col, row] in spaces will be at position [9*row + col]
    	spaces[9*row + col].occupy(players[0].getPlayerNum());
    	
    	// We need to ensure that we unoccupy the space that the player just moved from,
    	// assuming that they moved at all
    	int[] oldPos = players[0].getPos();
    	if(! Arrays.equals(oldPos, new int[] {col, row})) {
    		int oldCol = oldPos[0];
    		int oldRow = oldPos[1];
    		spaces[9*oldRow + oldCol].unoccupy();
    	}
    	
    	// Finally, set the position of the player to the new coordinates
    	players[0].setPos(col, row);
    }
    
    /**
     * Places a wall relative to a designator space on the board: the space
     * directly northwest of the wall center from player 1's perspective.  The
     * direction of the wall is determined by the second argument:
     * 0 - vertical
     * 1 - horizontal
     * 
     * @param designator the space directly northwest of the wall center
     * @param dir        designates the direction of the wall, 0 for vertical and 1 for horizontal
     */
    public void placeWall(Space designator, int dir) {
    	// The coordinates of the designator space
    	int[] coords = designator.getCoords();
    	
    	// The index of the designator in the spaces array
    	int index = 9*coords[1] + coords[0];
    	
    	// Map spaces[index] to dir in the walls map
    	walls.put(spaces[index], dir);
    	
    	// Reduce the number of walls the active player has by 1
    	if(active != null) {
    		active.spendWall();
    		Space[] nearby = findNearSpaces(spaces[index]);
        	spaces[index].placeWall(dir);
        	spaces[index].placeCornerWall();
        	
        	switch(dir) {
        		case 0: nearby[2].placeWall(0);
        				break;
        		case 1: nearby[1].placeWall(1);
        				break;
        	}
        	placeCornerWall(designator);
    	}	
    }
    
    /**
     * When a new wall has been placed, this method checks to see which,
     * if any, corner wall panels should be darkened to completely fill in
     * any gaps in contiguous walls
     * 
     * @param designator space designating the new wall placement
     */
    public void placeCornerWall(Space designator) {
    	// The coordinates of the designator space
    	int[] coords = designator.getCoords();
    	
    	// The index of the designator space in the spaces array
    	int index = 9*coords[1] + coords[0];
    	
    	// The direction of the newly placed wall (0 for vertical and 1 for horizontal)
    	int dir = walls.get(spaces[index]);
    	
    	// The four spaces adjacent to the designator
    	Space[] nearby = findNearSpaces(spaces[index]);
    	
    	switch(dir) {
    		// The wall is vertical
    		case 0: // The designator is not in the topmost row
    				if(nearby[0] != null) {
    					// Fill in the corner of the upper space if either of its wall panels are placed
    					if(nearby[0].isPlaced(0) || nearby[0].isPlaced(1)) {
    						nearby[0].placeCornerWall();
    					}
    					
    					// The spaces adjacent to the space above the designator
    					// NOTE: The space above the designator cannot be in the rightmost
    					// column, and so the space to the right of it must be not null
    					Space[] nearUpper = findNearSpaces(nearby[0]);
    					
    					// Fill in the corner of the upper space if the horizontal wall panel
    					// of the space to its right has been placed
    					if(nearUpper[1].isPlaced(1)) {
    						nearby[0].placeCornerWall();
    					}
    				}
    				
    				// The space below the designator is not in the bottom row
    				if(nearby[2].getRow() != 8) {
    					
    					// The spaces adjacent to the space below the designator
    					// NOTE: The space below the designator cannot be in the rightmost
    					// column, and so the space to its right must be not null
    					// Moreover, the space below the designator is not in the bottom row,
    					// and so the space below it must be not null
    					Space[] nearBelow = findNearSpaces(nearby[2]);
    					
    					// Fill in the corner of the bottom space if the horizontal
    					// wall panel of the space to its right has been placed
    					if(nearBelow[1].isPlaced(1)) {
    						nearby[2].placeCornerWall();
    					}
    					
    					// Fill in the corner of the bottom space if the vertical
    					// wall panel of the space below that has been placed
    					if(nearBelow[2].isPlaced(0)) {
    						nearby[2].placeCornerWall();
    					}
    				}
    				break;
    				
    		// The wall is horizontal
    		case 1: // The designator is not in the leftmost column
    				if(nearby[3] != null) {
    					// Fill in corner of left space if either of its wall panels are placed
    					if(nearby[3].isPlaced(0) || nearby[3].isPlaced(1)) {
    						nearby[3].placeCornerWall();
    					}
    					
    					// The spaces adjacent to the space to the left of the designator
    					// NOTE: The space to the left of the designator cannot be on the bottom
    					// row, and so the space below that must be not null
    					Space[] nearLeft = findNearSpaces(nearby[3]);
    					
    					// Fill in corner of left space if the vertical wall panel
    					// of the space below that space has been placed
						if(nearLeft[2].isPlaced(0)) {
							nearby[3].placeCornerWall();
						}	
    				}
    				
    				// The space to the right of the designator is not in the rightmost column
    				if(nearby[1].getColumn() != 8) {
    					
    					// The spaces adjacent to the space to the right of the designator
    					// NOTE: The space to the right of the designator cannot be on the bottom
    					// row, and so the space below that must be not null
    					// Moreover, the space to the right of the designator is not in the
    					// rightmost column, and so the space to its right must be not null
    					Space[] nearRight = findNearSpaces(nearby[1]);
    					
    					// Fill in the corner of the right space if the horizontal
    					// wall panel of the space to the right of that space has been placed
    					if(nearRight[1].isPlaced(1)) {
    						nearby[1].placeCornerWall();
    					}
    					
    					// Fill in the corner of the right space if the vertical
    					// wall panel of the space below that space has been placed
    					if(nearRight[2].isPlaced(0)) {
    						nearby[1].placeCornerWall();
    					}
    				}
    				break;
    	}
    }
    
    /**
     * Getter for the currently active player
     * 
     * @return the active player
     */
    public Player getActivePlayer() {
    	return active;
    }
    
    /**
     * Sets the active player to the player whose number is turn mod numPlayers,
     * so on turn = 0, active = players[0], on turn = 1, active = players[1 % 2] = players[1] etc
     */
    public void setActivePlayer() {
    	active = getPlayerFromNum((turn % numPlayers) + 1);
    }
    
    /**
     * Getter for the map of walls on the board
     * 
     * @return the map of walls currently in play
     */
    public Map<Space, Integer> getWalls() {
    	return walls;
    }
    
    /**
     * Setter for the walls on the board
     * 
     * @param newWalls walls mapping to set on the board
     */
    public void setWalls(HashMap<Space, Integer> newWalls) {
    	walls = newWalls;
    }
    
    /**
     * Returns the space on the board with the given index in spaces
     * 
     * @param index the index of a space on the board in spaces, in the interval [0, 81)
     * @return spaces[index]
     */
    public Space getSpace(int index) {
    	return spaces[index];
    }
    
    /**
     * Checks whether a space on the board is currently occupied by a player
     * 
     * @param s the space to check
     * @return true if the space is occupied and false if it is not
     */
    public boolean isOccupied(Space s) {
    	int index = 9*s.getRow() + s.getColumn();
    	return spaces[index].isOccupied();
    }
    
    /**
     * Checks whether the space at the given column and row is currently occupied by a player
     * 
     * @param col the column of the space
     * @param row the row of the space
     * @return true if the space is occupied and false if it is not
     */
    public boolean isOccupied(int col, int row) {
    	return spaces[9*row + col].isOccupied();
    }
    
    /**
     * Returns the player on the board with the specified player number
     * 
     * @param playerNum the number of the player to retrieve
     * @return the player whose number is playerNum
     */
    public Player getPlayerFromNum(int playerNum) {
    	return players[playerNum-1];
    }
    
    /**
     * Returns the player on the board (if there is one) at spaces[index]
     * 
     * @param index the index of the space in spaces to check
     * @return the player at spaces[index] or null
     */
    public Player getPlayerFromIndex(int index) {
    	try {
    		if(spaces[index].isOccupied()) {
    			return getPlayerFromNum(spaces[index].getPlayerNum());
    		}
    	} catch(IndexOutOfBoundsException e) {
    		System.out.println(index + " is out of bounds");
    	}
    	return null;
    }
    
    /**
     * Returns the space at the specified coordinates
     * 
     * @param col the column of the space
     * @param row the row of the space
     * @return the space in the grid at position [col, row]
     */
    public Space getSpaceFromCoords(int col, int row) {
    	return spaces[9*row + col];
    }
    
    /**
     * Returns the space at the specified coordinates
     * 
     * @param coords array of the coordinates of the space (may be null)
     * @return the space in the grid with the coordinates specified, or null if given null
     */
    public Space getSpaceFromCoords(int[] coords) {
    	if(coords == null) {
    		return null;
    	} else {
    		return spaces[9*coords[1] + coords[0]];
    	}
    }
    
    /**
     * Returns spaces at the position occupied by a player on the board
     * 
     * @param p any player
     * @return spaces at the player's current position
     */
    public Space getSpaceFromPlayer(Player p) {
    	int index = 9*p.getRow() + p.getColumn();
    	return spaces[index];
    }
    
    /**
     * Finds how many spaces away one space is from another
     * 
     * @param s1 the first space
     * @param s2 the other space
     * @return an integer of the number of spaces away s1 is from s2 on the board
     */
    public int distance(Space s1, Space s2) {
    	// The column and row of the first space
    	int col1 = s1.getColumn();
    	int row1 = s1.getRow();
    	
    	// The column and row of the second space
    	int col2 = s2.getColumn();
    	int row2 = s2.getRow();
    	
    	// The difference in the columns and rows of the two spaces
    	int colDif = Math.abs(col2 - col1);
    	int rowDif = Math.abs(row2 - row1);
    	
    	return colDif + rowDif;
    }
    
    /**
     * Returns an array of the four spaces around a player: [above, right, below, left]
     * 
     * @param p any player on the board
     * @return an array containing the four spaces around the player: [above, right, below, left]
     */
    public Space[] findNearSpaces(Player p) {
    	Space[] nearby = new Space[4];
    	int[] above = p.findSpaceCoords(0);
    	int[] right = p.findSpaceCoords(1);
    	int[] below = p.findSpaceCoords(2);
    	int[] left  = p.findSpaceCoords(3);
    	
    	nearby[0] = getSpaceFromCoords(above);
    	nearby[1] = getSpaceFromCoords(right);
    	nearby[2] = getSpaceFromCoords(below);
    	nearby[3] = getSpaceFromCoords(left);
    	
    	return nearby;
    }
    
    /**
     * Returns an array of the four spaces around a player: [above, right, below, left]
     * 
     * @param pNum the number of any player on the board
     * @return an array containing the four spaces around the player: [above, right, below, left]
     */
    public Space[] findNearSpaces(int pNum) {
    	Space[] nearby = new Space[4];
    	Player p = getPlayerFromNum(pNum);
    	int[] above = p.findSpaceCoords(0);
    	int[] right = p.findSpaceCoords(1);
    	int[] below = p.findSpaceCoords(2);
    	int[] left  = p.findSpaceCoords(3);
    	
    	nearby[0] = getSpaceFromCoords(above);
    	nearby[1] = getSpaceFromCoords(right);
    	nearby[2] = getSpaceFromCoords(below);
    	nearby[3] = getSpaceFromCoords(left);
    	
    	return nearby;
    }
    
    /**
     * Returns an array of the four spaces around a space: [above, right, below, left]
     * 
     * @param s any space on the board
     * @return an array containing the four spaces around the space: [above, right, below, left]
     */
    public Space[] findNearSpaces(Space s) {
    	return s.getNeighbors();
    }
    
    /**
     * Returns an array of the four spaces around the space with the
     * given coordinates: [above, right, below, left]
     * 
     * @param coords coordinates of a space on the board in the form: [col, row]
     * @return an array containing the four spaces around the coords: [above, right, below, left]
     */
    public Space[] findNearSpaces(int[] coords) {
    	Space s = getSpaceFromCoords(coords);
    	return s.getNeighbors();
    }
    
    /**
	 * Returns an array of the four spaces around the space with the
     * given coordinates: [above, right, below, left]
     * 
     * @param col the column of a space on the board
     * @param row the row of a space on the board
     * @return an array containing the four spaces around the space: [above, right, below, left]
     */
    public Space[] findNearSpaces(int col, int row) {
    	Space s = getSpaceFromCoords(col, row);
    	return s.getNeighbors();
    }
    
    /**
     * Returns an ArrayList of all the players that occupy spaces next to a player
     * 
     * @param p any player on the board
     * @return list of any players next to the player
     */
    public Set<Player> findNearPlayers(Player p) {
    	Set<Player> nearPlayers = new HashSet<Player>();
    	
    	// Get an array of all spaces around the space occupied by the player
    	Space[] nearSpaces = findNearSpaces(p);
    	for(int i = 0; i < 4; i++) {
    		if(nearSpaces[i] != null && nearSpaces[i].isOccupied()) {
    			nearPlayers.add(getPlayerFromNum(nearSpaces[i].getPlayerNum()));
    		}
    	}
    	return nearPlayers;
    }
    
    /**
     * Returns an array list of any players that are next to a space
     * 
     * @param col the column of the space to check near
     * @param row the row of the space to check near
     * @return list of any players next to the space
     */
    public Set<Player> findNearPlayers(int col, int row) {
    	Set<Player> nearPlayers = new HashSet<Player>();
    	
    	// Get an array of the nearby spaces
    	Space[] nearSpaces = findNearSpaces(col, row);
    	for(int i = 0; i < 4; i++) {
    		if(nearSpaces[i] != null && nearSpaces[i].isOccupied()) {
    			nearPlayers.add(getPlayerFromNum(nearSpaces[i].getPlayerNum()));
    		}
    	}
    	return nearPlayers;
    }
    
    /**
     * Determines whether or not two spaces are adjacent or not, that is,
     * the distance between them is exactly 1
     * 
     * @param s1 a space
     * @param s2 a space
     * @return true if the distance between s1 and s2 is exactly 1 and false otherwise
     */
    public boolean areAdjacent(Space s1, Space s2) {
    	if(distance(s1, s2) == 1) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Returns an integer representing the direction that one space is from another
     * on the board, relative to the first.  Note that it will throw an exception 
     * if the two spaces are not adjacent to one another:
     * 0 - s2 is above s1
     * 1 - s2 is right of s1
     * 2 - s2 is below s1
     * 3 - s2 is left of s1
     * 
     * @param s1 a space
     * @param s2 a space
     * @return the direction of s2 relative to s1
     */
    public int getDirection(Space s1, Space s2) {
    	// Ensure the spaces are adjacent first
    	if(! areAdjacent(s1, s2)) {
    		throw new IllegalArgumentException("The two spaces must be adjacent");
    	}
    	
    	int colDif = s1.getColumn() - s2.getColumn();
    	int rowDif = s1.getRow() - s2.getRow();
    	int dir = -1;
    	
    	switch(rowDif) {
    		case  1: dir = 0;
    				 break;
    		case -1: dir = 2;
    				 break;
    	}
    	switch(colDif) {
    		case  1: dir = 3;
    				 break;
    		case -1: dir = 1;
    				 break;
    	}
    	return dir;
    }
    
    /**
     * Determines whether or not a potential move by the currently active player
     * would be a legal jump or not
     * 
     * @param jumpSpace the space attempting to be jumped to
     * @return true if the jump would be legal and false if not
     */
    private boolean isLegalJump(Space jumpSpace) {
    	// First we get a list of any and all players that occupy spaces
    	// next to the active player
    	Set<Player> nearPlayers = findNearPlayers(active);
    	
    	// If there are no players nearby the active player, there is no legal jump
    	if(nearPlayers.isEmpty()) {
    		return false;
    	}
    	
    	int jumpType = jumpType(jumpSpace, nearPlayers);
    	boolean jumpLegal = false;
    	
    	switch(jumpType) {
    		// The jump is diagonal
    		case  1: jumpLegal = isDiagJumpLegal(jumpSpace, nearPlayers);
    				 break;
    		// The jump is direct
    		case  0: jumpLegal = !isDirectJumpBlocked(jumpSpace);
    				 break;
    		// No pawn could be jumped to reach the jump space
    		case -1: jumpLegal = false;
    				 break;    	
    	}
    	
    	return jumpLegal;
    }
    
    /**
     * Method which determines what type of jump the currently active player is 
     * attempting to make: direct jump or diagonal jump.  The method returns an
     * integer which represents the jump type:
     * -1 - illegal jump type, there is no pawn the player could jump to reach the space
     * 0  - direct
     * 1  - diagonal
     * 
     * @param jumpSpace   the space the player is attempting to jump to
     * @param nearPlayers the players that are adjacent to the active player
     * @return an integer representing the type of jump, -1 for illegal, 0 for direct and 1 for diagonal
     */
    private int jumpType(Space jumpSpace, Set<Player> nearPlayers) {    	
    	// A set of all neighboring spaces of the nearby players
    	Set<Space> nearbySpaces = new HashSet<Space>();
    	
    	// Populate the set with all the spaces that neighbor the nearby players
		for(Player p : nearPlayers) {
			Space[] nearby = findNearSpaces(p);
			for(int i = 0; i < 4; i++) {
				nearbySpaces.add(nearby[i]);
			}
		}
		
		// If the jump space is not one of those spaces adjacent to an adjacent player
		// then the jump being attempted must be illegal
		if(! nearbySpaces.contains(jumpSpace)) {
			return -1;
		}
		
		// If the active player and the jump space have the same column or the same
		// row, then the jump must be direct
		if((active.getColumn() - jumpSpace.getColumn() == 0) || 
		   (active.getRow() - jumpSpace.getRow() == 0)) {
			return 0;
		}
		
		// The only other possible option at this point is a diagonal jump
    	return 1;
    }
    
    /**
     * Determines whether or not a direct jump being attempted by the active player
     * is blocked by a wall or not
     * 
     * @param jumpSpace   the space the active player is attempting to jump a pawn to get to
     * @param nearPlayers the players who are adjacent to the active player
     * @return true if the jump is blocked and false if it is not
     */
    private boolean isDirectJumpBlocked(Space jumpSpace) {
    	// The difference in column and row between the active player's current position
    	// and that of the jump space.  Since the jump is direct one will be 2/-2 and the
    	// other will be 0.  This determines the direction of the jump.
    	int colDif = active.getColumn() - jumpSpace.getColumn();
    	int rowDif = active.getRow() - jumpSpace.getRow();
    	
    	// The spaces around the active player
    	Space[] nearActive = findNearSpaces(active);

    	// Vertical jump
    	if(colDif == 0) {
    		switch(rowDif) {
    			// The jump is up
    			case 2: if(isBlocked(nearActive[0], jumpSpace)) {
    						return true;
    					}
    					break;
    			// The jump is down
    			case -2: if(isBlocked(nearActive[2], jumpSpace)) {
    						 return true;
    					 }
    					 break;
    		}
    	// Horizontal jump
    	} else if(rowDif == 0) {
    		switch(colDif) {
    			// The jump is left
    			case 2: if(isBlocked(nearActive[3], jumpSpace)) {
    						return true;
    					}
    					break;
    			// The jump is right
    			case -2: if(isBlocked(nearActive[1], jumpSpace)) {
							return true;
						 }
						 break;
    		}
    	} 
    	// If we are here then the direct jump must be legal
    	return false;
    }
    
    /**
     * Determines whether or not a diagonal jump being attempted by the active player
     * would be legal or not
     * 
     * @param jumpSpace   the space the active player is attempting a diagonal jump to
     * @param nearPlayers the players that are adjacent to the current player
     * @return true if the diagonal jump would be legal and false if not
     */
    private boolean isDiagJumpLegal(Space jumpSpace, Set<Player> nearPlayers) {
    	// The players which are adjacent to the active player that are
    	// also adjacent to the jump space, note that this will have a max size of 2
    	Set<Player> jumpablePlayers = new HashSet<Player>();
    	for(Player p : nearPlayers) {
    		int index = 9*p.getRow() + p.getColumn();
    		if(areAdjacent(jumpSpace, spaces[index])) {
    			jumpablePlayers.add(p);
    		}
    	}
    	
    	Space activeSpace = getSpaceFromPlayer(active);
    	
    	for(Player p : jumpablePlayers) {
    		Space pSpace = getSpaceFromPlayer(p);
    		Space[] nearP = findNearSpaces(p);
    		
    		// The direction of the jumpable player's space from the active player's
    		int dir = getDirection(activeSpace, pSpace);
    		
    		// We simulate a direct jump over jumpable player p to see if such a jump
    		// would be prevented by another player or a wall, in either case we then
    		// check to ensure that the diagonal jump would not be impeded by a wall
    		if((isOccupied(nearP[dir]) || isDirectJumpBlocked(nearP[dir])) &&
    				(! isBlocked(pSpace, jumpSpace))) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks whether there is a wall on the board between two spaces, in this method
     * we always assume that start and end are exactly one space apart
     * 
     * @param start the starting space
     * @param end   the ending space
     * @return true if there is a wall between the spaces and false if not
     */
    public boolean isBlocked(Space start, Space end) {
    	int[] startCoords = start.getCoords();
    	int[] endCoords = end.getCoords();
    	
    	// The spaces which are adjacent to start, end must be one of them
    	Space[] nearStart = findNearSpaces(startCoords);
    	Space[] nearEnd = findNearSpaces(endCoords);
    	
    	// The spaces which, if designating walls of a certain direction,
    	// will block movement from start to end
    	Space[] blockers = new Space[2];
    	
    	// The differences between the columns and rows and the two spaces
    	// since start and end are exactly one space away, one of these
    	// differences must be zero
    	int colDif = startCoords[0] - endCoords[0];
    	int rowDif = startCoords[1] - endCoords[1];
    	
    	// Two cases are possible:
    	// Case 1: start is below end or start is left of end
    	// Case 2: start is above end or start is right of end
    	if(rowDif == 1 || colDif == -1) {
    		blockers[0] = nearEnd[3];
    		blockers[1] = nearStart[0];
    	} else if(rowDif == -1 || colDif == 1) {
    		blockers[0] = nearStart[3];
    		blockers[1] = nearEnd[0];
    	} else {
    		throw new IllegalArgumentException("Start and end must be exactly one space apart");
    	}
    	
    	// For each potential blocker we compare each of the existing designators,
    	// if the blocker is also a designator then we check to see what direction
    	// of wall it designates
    	for(Space blocker : blockers) {
	    	for(Space designator : walls.keySet()) {
	    		int dir = walls.get(designator).intValue();
	    		if(blocker != null && blocker.equalsSpace(designator)) {
	    			// start and end are vertical from each other and the wall is horizontal
	    			if(colDif == 0 && dir == 1) {
	    				return true;
	    			}
	    			// start and end are horizontal from each other and the wall is vertical
	    			if(rowDif == 0 && dir == 0) {
	    				return true;
	    			}
	    		}
	    	}
    	} 	
    	return false;
    }
    
    /**
     * Checks whether or not a space has already had a wall placed relative to it
     * 
     * @param designator the space which designates a wall placement
     * @return true if the space already designates a wall, false if not
     */
    private boolean isDesignated(Space designator) {
    	for(Space s : walls.keySet()) {
    		if(designator.equalsSpace(s)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks whether or not an attempted wall placement would intersect an existing wall
     * 
     * @param designator the space which designates a wall placement
     * @param dir        the direction of the wall, 0 for vertical and 1 for horizontal
     * @return true if the wall would intersect an existing wall and false if not
     */
    private boolean willIntersect(Space designator, int dir) {
    	int[] coords = designator.getCoords();
    	int index = 9*coords[1] + coords[0];
    	
    	// Get the spaces around the designator first
    	Space[] nearby = findNearSpaces(coords);
    	
    	// If the wall would be vertical then we need to check to see
    	// if either of the spaces above or below the designator are in the
    	// walls key set designating a vertical wall
    	if(dir == 0) {
    		for(Space s : walls.keySet()) {
    			if((nearby[0] != null && s.equalsSpace(nearby[0])) || 
    					(nearby[2] != null && s.equalsSpace(nearby[2]))) {
    				if(walls.get(s).intValue() == 0) {
    					return true;
    				}
    			}
    		}
    	}
    	
    	// If the wall would be horizontal then we need to check to see
    	// if either of the spaces to the right or left of the designator
    	// are in the walls key set designating a horizontal wall
    	if(dir == 1) {
    		for(Space s : walls.keySet()) {
    			if((nearby[1] != null && s.equalsSpace(nearby[1])) || 
    					(nearby[3] != null && s.equalsSpace(nearby[3]))) {
    				if(walls.get(s).intValue() == 1) {
    					return true;
    				}
    			}
    		}
    	}    	
    	return false;
    }
    
}





















