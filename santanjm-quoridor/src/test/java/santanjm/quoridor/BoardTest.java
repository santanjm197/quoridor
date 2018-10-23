package santanjm.quoridor;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import santanjm.quoridor.Board;
import santanjm.quoridor.Player;
import santanjm.quoridor.Space;

/**
 * Test class for the board
 */
public class BoardTest {
	
	@Test
	public void testLegalConstruct() {
		Board legal1 = new Board(2);
		assertEquals(legal1.numPlayers, 2);
		
		Board legal2 = new Board(4);
		assertEquals(legal2.numPlayers, 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalConstruct() {
		Board illegal = new Board(3);
	}
    
	@Test
	public void testTwoPlayerStartingPos() {
		Board b = new Board(2);
		assertTrue(b.isOccupied(4, 0));
		assertTrue(b.isOccupied(4, 8));
		assertFalse(b.isOccupied(0, 4));
		assertFalse(b.isOccupied(8, 4));
	}
	
	@Test
	public void testFourPlayerStartingPos() {
		Board b = new Board(4);
		assertTrue(b.isOccupied(4, 0));
		assertTrue(b.isOccupied(4, 8));
		assertTrue(b.isOccupied(0, 4));
		assertTrue(b.isOccupied(8, 4));
	}
	
	@Test
	public void testPlacePlayer() {
		Board b = new Board(2);
		Player p1 = b.getPlayerFromNum(1);
		b.placePlayer(p1, 4, 7);
		int[] newPos = new int[] {4, 7};
		assertArrayEquals(b.getPlayerFromNum(1).getPos(), newPos);
		assertFalse(b.isOccupied(4, 8));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalMove1() {
		Board b = new Board(2);
		b.isLegalMove("asdf94.");
	}
	
	@Test
	public void testIsLegalMove1() {
		// Test that moving 2 spaces without a jump is illegal
		Board b = new Board(4);
		Player p1 = b.getPlayerFromNum(1);
		assertFalse(b.isLegalMove("g9"));
		
		Player p2 = b.getPlayerFromNum(2);
		Player p3 = b.getPlayerFromNum(3);
		b.placePlayer(p2, 4, 7);
		b.placePlayer(p3, 5, 8);
		
		b.isLegalMove("g9");
	}
	
	@Test
	public void testIsLegalMove2() {
		// Test that moving onto occupied space is illegal but moving around it is legal
		Board b = new Board(2);
		Player p1 = b.getPlayerFromNum(1);
		b.placePlayer(p1, 4, 1);
		assertFalse(b.isLegalMove("e1"));
		assertTrue(b.isLegalMove("f2"));
	}
	
	@Test
	public void testIsLegalMove3() {
		// Test that jumping works
		Board b = new Board(4);
		Player p1 = b.getPlayerFromNum(1);
		assertFalse(b.isLegalMove("e7"));
		
		Player p2 = b.getPlayerFromNum(2);
		Player p3 = b.getPlayerFromNum(3);
		b.placePlayer(p1, 2, 3);
		b.placePlayer(p2, 3, 3);
		b.placePlayer(p3, 2, 4);
		
		// Attempt an illegal jump to the space above player 2, [3, 2]
		assertFalse(b.isLegalMove("d3"));
		
		// Jump to space below player 3, [2, 5]
		assertTrue(b.isLegalMove("c6"));
		
		// Attempt an illegal jump 2 spaces to the left of player 1, [0, 3]
		assertFalse(b.isLegalMove("a4"));
	}
	
	@Test
	public void testIsLegalMove4() {
		// Test the legality of diagonal jumps
		Board b = new Board(4);
		Player p1 = b.getPlayerFromNum(1);
		Player p2 = b.getPlayerFromNum(2);
		Player p3 = b.getPlayerFromNum(3);
		Player p4 = b.getPlayerFromNum(4);
		
		b.placePlayer(p1, 4, 4);
		b.placePlayer(p2, 4, 3);
		b.placePlayer(p3, 5, 4);
		b.placePlayer(p4, 3, 4);
		Space s1 = b.getSpaceFromCoords(5, 3);
		b.placeWall(s1, 0);
		b.isLegalMove("f4");
		
		Space s2 = b.getSpaceFromCoords(4, 3);
		b.placeWall(s2, 1);
		b.isLegalMove("f4");
	}
	
	@Test
	public void testIsLegalMove5() {
		// Test that single space move blocked by a wall is illegal
		Board b1 = new Board(2);
		Player p1 = b1.getPlayerFromNum(1);
		assertTrue(b1.isLegalMove("e8"));
		
		Space designator = b1.getSpaceFromCoords(3, 7);
		b1.placeWall(designator, 1);
		assertFalse(b1.isLegalMove("e8"));
		assertTrue(b1.isLegalMove("f9"));
		assertTrue(b1.isLegalMove("d9"));
		
		b1.placePlayer(p1, 4, 7);
		assertFalse(b1.isLegalMove("e9"));
		assertTrue(b1.isLegalMove("f8"));
		assertTrue(b1.isLegalMove("d8"));
			
		designator = b1.getSpaceFromCoords(4, 4);
		b1.placeWall(designator, 0);
		b1.placePlayer(p1, 4, 4);
		assertFalse(b1.isLegalMove("f5"));
		assertTrue(b1.isLegalMove("e4"));
		assertTrue(b1.isLegalMove("e6"));
		
		b1.placePlayer(p1, 4, 5);
		assertFalse(b1.isLegalMove("f6"));
		assertTrue(b1.isLegalMove("e5"));
		assertTrue(b1.isLegalMove("e7"));
		
		Board b2 = new Board(2);
		Player p2 = b2.getPlayerFromNum(1);
		assertTrue(b2.isLegalMove("e8"));
		
		designator = b2.getSpaceFromCoords(4, 7);
		b2.placeWall(designator, 1);
		assertFalse(b2.isLegalMove("e8"));
		assertTrue(b2.isLegalMove("f9"));
		assertTrue(b2.isLegalMove("d9"));
		
		b2.placePlayer(p2, 4, 7);
		assertFalse(b2.isLegalMove("e9"));
		assertTrue(b2.isLegalMove("f8"));
		assertTrue(b2.isLegalMove("d8"));
	}
	
	@Test
	public void testIsLegalMove6() {
		// Tests the alternate isLegalMove method
		Board b = new Board(2);
		Player p1 = b.getPlayerFromNum(1);
		Space designator = b.getSpaceFromCoords(4, 6);
		b.placeWall(designator, 1);
		
		// First check a single upward legal move
		assertTrue(b.isLegalMove(1, 4, 7));
		
		// Now check the illegal wall-crossing move upwards
		assertFalse(b.isLegalMove(1, 4, 6));
	}
	
	@Test
	public void testPlaceWall() {
		Board b = new Board(2);
		Player p1 = b.getPlayerFromNum(1);
		assertTrue(p1.getWalls() == 10);
		assertTrue(b.getWalls().isEmpty());
		
		Space designator = b.getSpaceFromCoords(1, 4);
		b.placeWall(designator, 0);
		assertFalse(b.getWalls().isEmpty());
		assertTrue(p1.getWalls() == 9);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIsLegalWall1() {
		// Ensures that attempting to pass a direction character other than v or h
		// will throw an illegal argument exception
		Board b = new Board(2);
		b.isLegalMove("b3t");
	}
	
	@Test
	public void testIsLegalWall2() {
		// Test that walls can't be placed relative to row 9 or column 9
		Board b = new Board(2);
		assertFalse(b.isLegalMove("d9v"));
	}
	
	@Test
	public void testIsLegalWall3() {
		// Test that multiple walls cannot be placed with the same designating space
		Board b = new Board(2);
		Space designator = b.getSpaceFromCoords(1, 4);
		b.placeWall(designator, 0);
		assertFalse(b.isLegalMove("b5h"));
		assertFalse(b.isLegalMove("b5v"));
	}
	
	@Test
	public void testIsLegalWall4() {
		// Test that walls cannot be placed that would intersect an existing wall
		Board b = new Board(2);
		
		// First we test vertical intersections
		Space designator = b.getSpaceFromCoords(1, 4);
		b.placeWall(designator, 0);
		assertFalse(b.isLegalMove("b4v"));
		assertFalse(b.isLegalMove("b6v"));
		
		// Then we test horizontal intersections
		designator = b.getSpaceFromCoords(4, 4);
		b.placeWall(designator, 1);
		assertFalse(b.isLegalMove("f5h"));
		assertFalse(b.isLegalMove("d5h"));
	}
	
	@Test
	public void testFindNumSpaces() {
		Board b = new Board(2);
		Space s1 = b.getSpaceFromCoords(3, 1);
		Space s2 = b.getSpaceFromCoords(3, 2);
		Space s3 = b.getSpaceFromCoords(5, 2);
		int dist1 = b.distance(s1, s2);
		int dist2 = b.distance(s1, s3);
		int dist3 = b.distance(s2, s3);
		assertEquals(dist1, 1);
		assertEquals(dist2, 3);
		assertEquals(dist3, 2);
	}
	
	@Test
	public void testFindNearSpaces1() {
		Board b = new Board(2);
		Player p1 = b.getPlayerFromNum(1);
		
		Space[] nearby = b.findNearSpaces(p1);
		Space[] expected = new Space[4];
		expected[0] = new Space(4, 7);
		expected[1] = new Space(5, 8);
		expected[3] = new Space(3, 8);
		
		assertTrue(nearby[0].equalsSpace(expected[0]));
		assertTrue(nearby[1].equalsSpace(expected[1]));
		assertTrue(nearby[3].equalsSpace(expected[3]));
		assertEquals(nearby[2], null);
	}
	
	@Test
	public void testFindNearSpaces2() {
		Board b = new Board(2);
		
		Space[] nearby = b.findNearSpaces(1);
		Space[] expected = new Space[4];
		expected[0] = new Space(4, 7);
		expected[1] = new Space(5, 8);
		expected[3] = new Space(3, 8);
		
		assertTrue(nearby[0].equalsSpace(expected[0]));
		assertTrue(nearby[1].equalsSpace(expected[1]));
		assertTrue(nearby[3].equalsSpace(expected[3]));
		assertEquals(nearby[2], null);
	}
	
	@Test
	public void testFindNearSpaces3() {
		Board b = new Board(2);
		Space s = b.getSpaceFromCoords(2, 3);
		Space[] nearby = b.findNearSpaces(s);
		Space[] expected = new Space[4];
		expected[0] = new Space(2, 2);
		expected[1] = new Space(3, 3);
		expected[2] = new Space(2, 4);
		expected[3] = new Space(1, 3);
		
		assertTrue(nearby[0].equalsSpace(expected[0]));
		assertTrue(nearby[1].equalsSpace(expected[1]));
		assertTrue(nearby[2].equalsSpace(expected[2]));
		assertTrue(nearby[3].equalsSpace(expected[3]));
	}
	
	@Test
	public void testFindNearSpaces4() {
		Board b = new Board(2);
		int[] coords = new int[] {2, 3};
		Space[] nearby = b.findNearSpaces(coords);
		Space[] expected = new Space[4];
		expected[0] = new Space(2, 2);
		expected[1] = new Space(3, 3);
		expected[2] = new Space(2, 4);
		expected[3] = new Space(1, 3);
		
		assertTrue(nearby[0].equalsSpace(expected[0]));
		assertTrue(nearby[1].equalsSpace(expected[1]));
		assertTrue(nearby[2].equalsSpace(expected[2]));
		assertTrue(nearby[3].equalsSpace(expected[3]));
	}
	
	@Test
	public void testFindNearSpaces5() {
		Board b = new Board(2);
		Space[] nearby = b.findNearSpaces(2, 3);
		Space[] expected = new Space[4];
		expected[0] = new Space(2, 2);
		expected[1] = new Space(3, 3);
		expected[2] = new Space(2, 4);
		expected[3] = new Space(1, 3);
		
		assertTrue(nearby[0].equalsSpace(expected[0]));
		assertTrue(nearby[1].equalsSpace(expected[1]));
		assertTrue(nearby[2].equalsSpace(expected[2]));
		assertTrue(nearby[3].equalsSpace(expected[3]));
	}
	
	@Test
	public void testFindNearPlayers() {
		Board b = new Board(4);
		Set<Player> nearPlayers = new HashSet<Player>();
		
		nearPlayers = b.findNearPlayers(4, 4);
		assertTrue(nearPlayers.isEmpty());
		
		Player p1 = b.getPlayerFromNum(1);
		Player p2 = b.getPlayerFromNum(2);
		b.placePlayer(p1, 4, 5);
		b.placePlayer(p2, 3, 4);
		
		Set<Player> expected = new HashSet<Player>(Arrays.asList(p1, p2));
		
		nearPlayers = b.findNearPlayers(4, 4);
		assertFalse(nearPlayers.isEmpty());
		assertEquals(nearPlayers, expected);	
	}
	
	@Test
	public void testGetDirection() {
		Board b = new Board(2);
		Player p1 = b.getPlayerFromNum(1);
		Player p2 = b.getPlayerFromNum(2);
		
		b.placePlayer(p1, 4, 4);
		Space p1Space = b.getSpaceFromPlayer(p1);
		
		// Above
		b.placePlayer(p2, 4, 3);	
		Space p2Space = b.getSpaceFromPlayer(p2);
		assertEquals(b.getDirection(p1Space, p2Space), 0);
		
		// Right
		b.placePlayer(p2, 5, 4);
		p2Space = b.getSpaceFromPlayer(p2);
		assertEquals(b.getDirection(p1Space, p2Space), 1);
		
		// Below
		b.placePlayer(p2, 4, 5);
		p2Space = b.getSpaceFromPlayer(p2);
		assertEquals(b.getDirection(p1Space, p2Space), 2);
		
		// Left
		b.placePlayer(p2, 3, 4);
		p2Space = b.getSpaceFromPlayer(p2);
		assertEquals(b.getDirection(p1Space, p2Space), 3);
	}
}



















