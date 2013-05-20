package tests;


import static org.junit.Assert.*;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.Set;

import model.Boulder;
import model.Cave;
import model.LevelStorer;
import model.Player;

import org.junit.Test;

public class LevelStorerTest {

    @Test
    public void testFileReading() throws FileNotFoundException {
        // correctness
        Set<String> takenNames = LevelStorer.getLevelFiles();
        String nextName = LevelStorer.findUnusedFilename();
        assertEquals(false, takenNames.contains(nextName));
        
        System.out.println(LevelStorer.readFromFile("level0.txt"));
    }
    
    @Test
    public void testAntiLexing(){
        Cave cave2 = new Cave(3,1);
        Player myPlayer2 = new Player(cave2);
        cave2.setElementAt(new Point(0,0), myPlayer2); 
        cave2.setElementAt(new Point(1,0), new Boulder(cave2)); 
        
        String intended="[Version: 1]\n" +
        		"[Boulder: o]\n" +
        		"[Default: .]\n" +
        		"[Diamond: *]\n" +
        		"[Dirt: +]\n" +
        		"[Player: <]\n" +
        		"[Wall: #]\n" +
        		"{diamondTarget: 0}\n" +
        		"< o . \n";
        assertEquals(intended,LevelStorer.antiLex(cave2));// bit too strict atm.
    }
    
    @Test
    public void lexTest() {
        Cave cave2 = new Cave(3,2);
        Player myPlayer2 = new Player(cave2);
        cave2.setElementAt(new Point(0,0), myPlayer2); 
        cave2.setElementAt(new Point(1,0), new Boulder(cave2));
        cave2.setElementAt(new Point(1,1), new Boulder(cave2));
        cave2.setDiamondTarget(3);
        String source = LevelStorer.antiLex(cave2);
        assertEquals(source,LevelStorer.antiLex(LevelStorer.lex(source)));
    }
}
