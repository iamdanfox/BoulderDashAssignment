package tests;


import static org.junit.Assert.*;


import java.io.FileNotFoundException;
import java.util.Set;



import org.junit.Test;

import controller.LevelStorer;

public class LevelStorerTest {

    @Test
    public void testFileReading() throws FileNotFoundException {
        // correctness
        Set<String> takenNames = LevelStorer.getLevelFiles();
        String nextName = LevelStorer.findUnusedFilename();
        assertEquals(false, takenNames.contains(nextName));
        
        //System.out.println(LevelStorer.readFromFile("level0.txt"));
    }
    
//    @Test 
//    public void testStringReading() throws FileNotFoundException {
//        System.out.println(LevelStorer.readFromFile("demo.txt"));
//    }
    
}
