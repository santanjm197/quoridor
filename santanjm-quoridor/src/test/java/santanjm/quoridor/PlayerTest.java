package santanjm.quoridor;
import org.junit.Test;
import static org.junit.Assert.*;
import santanjm.quoridor.Player;

/**
 * Test class for the Player
 */
public class PlayerTest {

		@Test
		public void testLegalConstruct() {
			Player legal1 = new Player(1, 5);
			Player legal2 = new Player(2, 5);
			Player legal3 = new Player(3, 5);
			Player legal4 = new Player(4, 5);
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void testIllegalConstruct() {
			Player illegal = new Player(5, 5);
		}
		
		@Test
		public void testSetPos() {
			Player p = new Player(1, 10);
			p.setPos(6, 7);
			int[] newPos = new int[] {6, 7};
			assertArrayEquals(p.getPos(), newPos);
		}
		
		@Test
		public void testFindSpaceCoords1() {
			// Test for spaces around a space not against any walls
			Player p = new Player(1, 10);
			p.setPos(2, 3);
			int[] expAbove = new int[] {2, 2};
			int[] expRight = new int[] {3, 3};
			int[] expBelow = new int[] {2, 4};
			int[] expLeft  = new int[] {1, 3};
			
			assertArrayEquals(p.findSpaceCoords(0), expAbove);
			assertArrayEquals(p.findSpaceCoords(1), expRight);
			assertArrayEquals(p.findSpaceCoords(2), expBelow);
			assertArrayEquals(p.findSpaceCoords(3), expLeft);
		}
		
		@Test
		public void testFindSpaceCoords2() {
			// Test for spaces around a space against walls
			Player p = new Player(1, 10);
			int[] expAbove = new int[] {4, 7};
			int[] expRight = new int[] {5, 8};
			int[] expLeft  = new int[] {3, 8};
			
			assertArrayEquals(p.findSpaceCoords(0), expAbove);
			assertArrayEquals(p.findSpaceCoords(1), expRight);
			assertArrayEquals(p.findSpaceCoords(3), expLeft);
			
			assertEquals(p.findSpaceCoords(2), null);
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void testFindSpaceCoords3() {
			// Test for an illegal direction
			Player p = new Player(1, 10);
			p.findSpaceCoords(5);
		}
		
		@Test
		public void testCanReachGoal1() {
			Board b = new Board(2);
			Player p1 = b.getPlayerFromNum(1);
			Player p2 = b.getPlayerFromNum(2);
			
			Space designator = b.getSpaceFromCoords(3, 7);
			
			assertTrue(p1.canReachGoal(designator, 0));
			assertTrue(p2.canReachGoal(designator, 0));
		}
		
		@Test
		public void testCanReachGoal2() {
			Board b = new Board(2);
			Player p1 = b.getPlayerFromNum(1);
			Player p2 = b.getPlayerFromNum(2);
			
			Space designator = b.getSpaceFromCoords(3, 7);
			
			assertTrue(p1.canReachGoal(designator, 1));
			assertTrue(p2.canReachGoal(designator, 1));
		}
		
		@Test
		public void testCanReachGoal3() {
			Board b = new Board(2);
			Player p1 = b.getPlayerFromNum(1);
			Player p2 = b.getPlayerFromNum(2);
			
			Space designator1 = b.getSpaceFromCoords(3, 7);
			Space designator2 = b.getSpaceFromCoords(4, 7);
			b.placeWall(designator1, 0);
			assertTrue(p1.canReachGoal(designator2, 1));
			assertTrue(p2.canReachGoal(designator2, 1));
		}
		
		@Test
		public void testCanReachGoal4() {
			Board b = new Board(2);
			Player p1 = b.getPlayerFromNum(1);
			Player p2 = b.getPlayerFromNum(2);
			
			Space designator1 = b.getSpaceFromCoords(3, 7);
			Space designator2 = b.getSpaceFromCoords(4, 7);
			Space designator3 = b.getSpaceFromCoords(5, 7);
			b.placeWall(designator1, 0);
			b.placeWall(designator2, 1);
			
			assertFalse(p1.canReachGoal(designator3, 0));
			assertTrue(p2.canReachGoal(designator3, 0));
		}
}











