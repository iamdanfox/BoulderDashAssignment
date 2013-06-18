package tests;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.Collection;

import org.junit.Test;

import controller.LevelStorer;

public class LevelStorerTest {

    @Test
    public void testFileReading() throws FileNotFoundException {
        // correctness
        Collection<String> takenNames = LevelStorer.getLevelFiles();
        String nextName = LevelStorer.findUnusedLevelname()+".txt";
        assertEquals(false, takenNames.contains(nextName));
        
        //System.out.println(LevelStorer.readFromFile("level0.txt"));
    }
    
//    @Test 
//    public void testStringReading() throws FileNotFoundException {
//        System.out.println(LevelStorer.readFromFile("demo.txt"));
//    }
    
}
