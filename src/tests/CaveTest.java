package tests;

import static org.junit.Assert.*;
import java.awt.*;

import model.*;

import org.junit.Test;

public class CaveTest {
    
    @Test
    public void caveElemTests(){
        Player p = new Player(null);
        assertEquals("Player[RIGHT]", p.toString());
        
        assertEquals("Wall[Red]", new Wall(null).toString());
        assertEquals("Wall[Red]", new Wall(null, Color.red).toString());
        assertEquals("Wall[Green]", new Wall(null, Color.green).toString());
        assertEquals("Wall[Blue]", new Wall(null, Color.blue).toString());
    }

	@Test
	public void checkCaveBounds() {
		Rectangle rect = new Rectangle (0,0,10,2);
		assertEquals(rect.contains(new Point(9,1)),true);
		assertEquals(rect.contains(new Point(10,2)),false);
	}
	
	@Test
	public void testOneStoreyCave(){
		Cave cave1 = new Cave(6,1);
		cave1.setFrozen(false);
		cave1.setDiamondTarget(2);
		
		cave1.setElementAt(new Point(0,0), new Wall(cave1));
		//blank square at (1,0)
		Boulder myBoulder = new Boulder(cave1);
		cave1.setElementAt(new Point(2,0), myBoulder);
		cave1.setElementAt(new Point(3,0), new Diamond(cave1));
		Player myPlayer = new Player(cave1);
		cave1.setElementAt(new Point(4,0), myPlayer);
		cave1.setElementAt(new Point(5,0), new Dirt(cave1));
		
		assertEquals(myPlayer.attemptMove(Direction.RIGHT),true); // move right consuming a square of dirt
		assertEquals(myPlayer.attemptMove(Direction.LEFT),true);  // move left into blank square
		assertEquals(myPlayer.attemptMove(Direction.LEFT),true); // consume a diamond
		
		assertEquals(cave1.getDiamondTarget(), 1); // check diamond was consumed
		
		assertEquals(myPlayer.attemptMove(Direction.LEFT), true); // push myBoulder into blank square, (1,0)
		
	}
	
	@Test 
	public void testCaveBounds(){
		Cave cave1 = new Cave(1,1);
		Player myPlayer = new Player(cave1);
		cave1.setElementAt(new Point(0,0), myPlayer);
		assertEquals(myPlayer.attemptMove(Direction.UP),false);
		assertEquals(myPlayer.attemptMove(Direction.DOWN),false);
		assertEquals(myPlayer.attemptMove(Direction.LEFT),false);
		assertEquals(myPlayer.attemptMove(Direction.RIGHT),false); 
	}
	
	@Test
	public void testPlayerPushingBoulders(){ // only works with no falling rules.
		Cave cave1 = new Cave(3,1);
		cave1.setFrozen(false);
		Player myPlayer = new Player(cave1);
		cave1.setElementAt(new Point(2,0), myPlayer); 
		Boulder myBoulder = new Boulder(cave1);
		cave1.setElementAt(new Point(1,0), myBoulder);
		assertEquals(myPlayer.attemptMove(Direction.LEFT), true); // player can push boulder left into space
		assertEquals(cave1.getElementAt(new Point(0,0)),myBoulder); // check boulder was actually moved
		assertEquals(myPlayer.attemptMove(Direction.LEFT), false); // player cannot push boulder against wall
		
		Cave cave2 = new Cave(3,1);
		Player myPlayer2 = new Player(cave2);
		cave2.setElementAt(new Point(0,0), myPlayer2); 
		cave2.setElementAt(new Point(1,0), new Boulder(cave2)); 
		assertEquals(myPlayer.attemptMove(Direction.RIGHT), true); // player can push boulder left into space
		assertEquals(myPlayer.attemptMove(Direction.RIGHT), false); // player cannot push boulder against wall
		
		Cave cave3 = new Cave(1,5);
		Player player3 = new Player(cave3);
		cave3.setElementAt(new Point(0,2), player3); 
		cave3.setElementAt(new Point(0,1), new Boulder(cave3));
		cave3.setElementAt(new Point(0,3), new Boulder(cave3)); 
		assertEquals(myPlayer.attemptMove(Direction.UP), false); 
		assertEquals(myPlayer.attemptMove(Direction.DOWN), false);
	}
	
	@Test
	public void testPlayerCrushedToDeath(){

		final Cave cave1 = new Cave(1,3);
		cave1.setFrozen(false);
		final Boulder boulder = new Boulder(cave1);
		cave1.setElementAt(new Point(0,0), boulder);
		Player player = new Player(cave1);
		cave1.setElementAt(new Point(0,1), player); 
		
		player.attemptMove(Direction.DOWN);
		cave1.addCaveListener(new CaveListener(){

			@Override
			public void wonStateChanged() { }

			@Override
			public void lostStateChanged() {
				// boulder should fall				 
				// player should eventually be crushed
				assertEquals(boulder, cave1.getElementAt(new Point(0,1)));
				
			}

			@Override
			public void diamondTargetChanged() { }

			@Override
			public void gridChanged() { }

			@Override
			public void frozenStateChanged() { }

			@Override
            public void dimensionsChanged() {  }
		});
		
		
	}
	
	@Test
	public void testCaveListeners(){
		final Cave cave1 = new Cave(1,1);
		cave1.setDiamondTarget(2);

		class TestListener implements CaveListener{
			public Boolean function1Executed = false;
			public Boolean function2Executed = false;

			@Override
            public void wonStateChanged() {
				assertEquals(cave1.getDiamondTarget(),0);
				function1Executed = true;
			}

			@Override
            public void diamondTargetChanged() {
				function2Executed = true;
			}

			@Override
			public void lostStateChanged() { }

			@Override
			public void gridChanged() { }

			@Override
			public void frozenStateChanged() { }

			@Override
            public void dimensionsChanged() {  }
		}
		
		TestListener myListener = new TestListener();
		cave1.addCaveListener(myListener);
		
		cave1.setDiamondTarget(cave1.getDiamondTarget() - 1);
		cave1.setDiamondTarget(cave1.getDiamondTarget() - 1);
		
		assertEquals(myListener.function1Executed, true);
		assertEquals(myListener.function2Executed, true);
	}
}
