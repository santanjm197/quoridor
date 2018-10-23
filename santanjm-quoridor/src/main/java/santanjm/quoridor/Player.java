package santanjm.quoridor;

import santanjm.quoridor.*;
import java.util.*;

/**
 * Class which represents a player in the game of Quoridor.  There will be either 2 or 4 players depending
 * on the starting conditions of the game.  If there are 2 players, each player will have 10 walls,
 * if there are 4 players then each player will have 5 walls.
 * @author santanjm
 */
public class Player {
	// The board the player will be playing on
	Board board;
	
    // The number of this player
    public int playerNum;
    
    // The number of walls the player has left
    int walls;

    // The current position of the player on the board: [column, row]
    int[] pos;

    // The spaces that, if reached by the player, will be victory
    Space[] goalSpaces = new Space[9];
    
    // Boolean which tells the player whether they have won or not
    boolean hasWon;
    
    // Value of Integer.MAX_VALUE so it can be referred to more quickly
    public static final double INF = Double.POSITIVE_INFINITY;
    
    /**
     * Constructs a new Player with a player number and a number of walls, then determine
     * the player's starting position based on their player number 
     * 
     * @param gameBoard the board on which this player will be playing quoridor
     * @param pNum     	the number of player, used to determine starting position and turn order
     * @param numWalls 	the number of walls this player will start the game with
     */
    public Player(Board gameBoard, int pNum, int numWalls) {
    	board = gameBoard;
    	switch (pNum) {
    		case  1: playerNum = 1;
    				 pos = new int[] {4, 8};
    				 for(int i = 0; i < 9; i++) {
    					 goalSpaces[i] = board.getSpace(i);
    				 }
    				 break;
    		case  2: playerNum = 2;
    				 pos = new int[] {4, 0};
    				 for(int i = 0; i < 9; i++) {
    					 goalSpaces[i] = board.getSpace(i + 72);
    				 }
    				 break;
    		case  3: playerNum = 3;
    				 pos = new int[] {0, 4};
    				 for(int i = 0; i < 9; i++) {
    					 goalSpaces[i] = board.getSpace(9*i + 8);
    				 }
    				 break;
    		case  4: playerNum = 4;
    				 pos = new int[] {8, 4};
    				 for(int i = 0; i < 9; i++) {
    					 goalSpaces[i] = board.getSpace(9*i);
    				 }
    				 break;
    		default: throw new IllegalArgumentException("Player number must be between 1 and 4");
    	}
    	walls = numWalls;
    }
    
    /**
     * Constructor for a player with no board, just a number and a number of walls
     * 
     * @param pNum     the number of player, used to determine starting position and turn order
     * @param numWalls the number of walls this player will start the game with
     */
    public Player(int pNum, int numWalls) {
    	this(new Board(), pNum, numWalls);
    	board.addPlayer(this);
    }
    
    /**
     * Creates a new player at the specified coordinates
     * 
     * @param coords the coordinates of this player
     */
    public Player(int[] coords) {
    	pos = coords;
    }
    
    /**
     * Updates the game board to its current state
     * 
     * @param newBoard the updated game board
     */
    public void updateBoard(Board newBoard) {
    	board = newBoard;
    }
    
    /**
     * Checks if this player has won by seeing if they are in one of their goal spaces
     * 
     * @return true if the player has won and false otherwise
     */
    public boolean checkHasWon() {
    	Space current = board.getSpaceFromCoords(pos);
    	for(Space goal : goalSpaces) {
    		if(current.equalsSpace(goal)) {
    			hasWon = true;
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Returns the current number of walls available to the player 
     *
     * @return the number of walls the player has left
     */
    public int getWalls() {
    	return walls;
    }

    /**
     * Returns the current position of the player as an array in the form [column, row]
     *
     * @return the position of the player on the board in the form [column, row]
     */
    public int[] getPos() {
    	return pos;
    }
    
    /**
     * Sets this player to a new position on the board
     * 
     * @param col the column of the player's new position
     * @param row the row of the player's new position
     */
    public void setPos(int col, int row) {
    	pos[0] = col;
    	pos[1] = row;
    }
    
    /**
     * Getter for the column of this player
     * 
     * @return the column of the space currently occupied by the player
     */
    public int getColumn() {
    	return pos[0];
    }
    
    /**
     * Getter for the row of this player
     * 
     * @return the row of the space currently occupied by the player
     */
    public int getRow() {
    	return pos[1];
    }
    
    /**
     * Getter for the number of this player
     * 
     * @return the player number
     */
    public int getPlayerNum() {
    	return playerNum;
    }

    /**
     * Finds the coordinates of the space that is one space away from this player's
     * current position on the board in the indicated direction:
     * 0 - above
     * 1 - right
     * 2 - below
     * 3 - left
     * 
     * @param dir the flag for which direction to return the coordinates from
     * @return the coordinates of the requested space, null if outside the grid
     */
    public int[] findSpaceCoords(int dir) {
    	// Make sure that the direction supplied is actually between 0 and 3
    	if(dir < 0 || dir > 3) {
    		throw new IllegalArgumentException("Direction must be 0 through 3");
    	}
    	
    	// The spaces on the board that are adjacent to this player's current position
    	Space[] nearby = board.findNearSpaces(pos);
    	if(nearby[dir] != null) {
    		return nearby[dir].getCoords();
    	}
    	return null;
    }
    
    /**
     * Reduces the number of walls this player has by 1
     */
    public void spendWall() {
    	walls--;
    }
    
    /**
     * Checks whether a proposed wall placement on the board would block
     * this player from reaching its goal on the opposite side of the board
     * 
     * @param designator the space northwest of the center of the wall
     * @param dir        the direction of the wall, 0 for vertical and 1 for horizontal
     * @return true if the wall placement would not prevent this player from reaching its goal
     */
    public boolean canReachGoal(Space designator, int dir) {
    	// A temporary board with only this player, all current walls and the proposed new wall
    	Map<Space, Integer> currentWalls = new HashMap<Space, Integer>(board.walls);
    	Board temp = new Board(this, currentWalls);
    	temp.placeWall(designator, dir);
    	
    	// The player's current position is where we start the path search
    	Space start = temp.getSpaceFromCoords(pos);
    	for(int i = 0; i < goalSpaces.length; i++) {
    		if(aStar(temp, start, goalSpaces[i])) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Classic A* shortest path algorithm for use in determining whether or not
     * a player will still be able to reach one of their goal spaces.  If the shortest
     * path found has length infinity then there would be no path from the player's
     * current position to a particular goal space.
     * 
     * @param temp  the temp board with the prospective wall placed and only this player
     * @param start the starting space to search from (this will be the player's position)
     * @param goal  any space on the board, typically one of the player's goal spaces
     * @return true if there is a path on the board from start to goal and false if not
     */
    public boolean aStar(Board temp, Space start, Space goal) {
    	// The set of nodes already evaluated
    	Set<Space> closedSet = new HashSet<Space>();
    	
    	// The set of currently discovered nodes that have not been evaluated yet
    	Set<Space> openSet = new HashSet<Space>(Arrays.asList(start));
    	
    	// For each node, which node it can be most efficiently be reached from
    	Map<Space, Space> cameFrom = new LinkedHashMap<Space, Space>();
    	
    	// For each node, the cost of getting from the start node to that node
    	Map<Space, Double> gScore = initScores(temp);
    	
    	// The cost of going from the starting space to the starting space is zero
    	gScore.put(temp.getSpaceFromCoords(start.getCoords()), 0.0);

    	// For each node, the total cost of getting from the start node to the
    	// goal by passing through that node.
    	Map<Space, Double> fScore = initScores(temp);
    	
    	// The cost from the starting node to the goal is entirely heuristic at first
    	fScore.put(start, heuristicFunction(start, goal));
    	
    	while(! openSet.isEmpty() ) {
    		Space current = minfScore(openSet, fScore);
    		
    		// If the node with the minimum f score is the goal node
    		// then a path has been found and we can stop
    		if(current.equalsSpace(goal)) {
    			return true;
    		}
    		
    		// If the current space (which is known to have minimum f score)
    		// has an f score of infinity, then there is no path and so return false
    		if(fScore.get(current) == INF) {
    			return false;
    		}
    		
    		// Remove the current from the list of discovered nodes
    		// and add it to the list of evaluated nodes
    		openSet.remove(current);
    		closedSet.add(current);
    		
    		Space[] currNeighbors = temp.findNearSpaces(current);
    		for(Space neighbor : currNeighbors) {
    			// Ignore the neighbor which is already evaluated
    			if(closedSet.contains(neighbor) || neighbor == null) {
    				continue;
    			}
    			
    			// The distance from the starting node to the neighbor
    			double tentativegScore = 0.0;		
    			if(temp.isBlocked(current, neighbor)) {
    				tentativegScore = INF;
    			} else {
    				tentativegScore = gScore.get(current) + temp.distance(current, neighbor);
    			}

    			// Discover a new node
    			if(! openSet.contains(neighbor)) {
    				openSet.add(neighbor);
    			} else if(tentativegScore >= gScore.get(neighbor)) {
    				continue;
    			}
    			
    			// This is the best path until now
    			cameFrom.put(neighbor, current);
    			gScore.put(neighbor, tentativegScore);
    			fScore.put(neighbor, gScore.get(neighbor) + heuristicFunction(neighbor, goal));
    		}
    		
    	}
    	
    	return false;
    }
    
    /**
     * Initializes a mapping for the temp board.  Used for initializing both
     * the gScore map and the fScore maps since both initially just map all nodes
     * to infinity.
     * 
     * @param temp the board being used in our path calculation
     * @return the initial gScore mapping, all nodes mapped to INF
     */
    private Map<Space, Double> initScores(Board temp) {
    	Map<Space, Double> score = new LinkedHashMap<Space, Double>();
    	for(int i = 0; i < 81; i++) {
    		score.put(temp.getSpace(i), INF);
    	}
    	return score;
    }
    
    /**
     * Heuristic function used in the A* path algorithm.  FOR NOW simply returns
     * how many spaces away on the grid the goal is from the node, ignoring walls
     * 
     * @param node a space on the grid, or a node in the graph
     * @param goal the goal space, or node, on the board
     * @return the heuristic cost estimate to get from the node to the goal
     */
    private double heuristicFunction(Space node, Space goal) {
    	return (double) node.distance(goal);
    }
    
    /**
     * Finds and returns the space (node) with the currently lowest f score
     * 
     * @param openSet the set of currently discovered nodes
     * @param fScore  the map of f scores
     * @return the space with the lowest f score
     */
    private Space minfScore(Set<Space> openSet, Map<Space, Double> fScore) {
    	// The space that we have discovered already with the minimum f score
    	Space min = null;
    	
    	// Start the minimum score at infinity
    	double minScore = INF;
    	
    	// For each known node, if that node has an f score less than the currently
    	// minimum f score, make that node the new min
    	for(Space known : openSet) {
    		if(fScore.get(known) <= minScore) {
    			min = known;
    			minScore = fScore.get(known);
    		}
    	} 	
    	return min;
    }
    
    /**
     * Returns a string representation of the player
     * 
     * @return a string representing this player
     */
    public String toString() {
    	char col = (char) (pos[0] + 97);
    	char row = (char) (pos[1] + 49);
    	return "Player " + playerNum + ": " + "Current pos: " + col + row + 
    			"\n          " + "Walls remaining: " + walls;	
    }
    
}















