package santanjm.quoridor;

import javax.swing.*;
import santanjm.quoridor.*;

/**
 * The main class for the Quoridor game, this is what is called to run the game. 
 * @author catsby
 */
public class Quoridor {

	public static void main(String[] args) {
		try {
			Board b = new Board(Integer.parseInt(args[0]));
			b.gameLoop();
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
}
