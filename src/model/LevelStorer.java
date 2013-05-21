/**
 * 
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author danfox
 * Allows the convenient representation of Cave Grids in text format.
 */
public class LevelStorer {
    
    public final static String DIRECTORY = "./levels";
    
    /**
     * 
     * @return The names of all files ending .txt or .TXT in DIRECTORY.
     */
    @SuppressWarnings("serial")
    public static Set<String> getLevelFiles(){
        return new HashSet<String>(){{
                // relative to project root
                File[] files = (new File(DIRECTORY)).listFiles();
                for (int i = 0; i < files.length; i++)
                    if (files[i].isFile()) {
                        String fname = files[i].getName();
                        if (fname.endsWith(".txt") || fname.endsWith(".TXT"))
                            add(fname);
                    }
            }};
    }
    
    /**
     * 
     * @return a string in the format leveli.txt where i is the smallest unused
     *         number in DIRECTORY
     */
    public static String findUnusedFilename(){
        Set<String> takenNames = getLevelFiles();
        int i=0;
        while (takenNames.contains("level"+i+".txt")) i++;
        return "level"+i+".txt";
    }
    
    /**
     * Saves a string to the first unused leveli.txt filename
     * @param contents
     * @return the filename that was written
     */
    public static String writeToFile(String contents){
        String fname = findUnusedFilename();
        writeToFile(fname,contents);
        return fname;
    }
    
    /**
     * Saves a string to a specified filename.
     * @param fileName
     * @param contents
     */
    public static void writeToFile(String fileName, String contents) {        
        try {
            PrintWriter out = new PrintWriter(DIRECTORY+"/"+fileName);
            out.println(contents);
            out.close();
        } catch (FileNotFoundException e) {
            System.err.println("This should never happen");
            e.printStackTrace();
        }
    }
    
    public static String readFromFile(String fileName) throws FileNotFoundException{
        return new Scanner(new File(DIRECTORY+"/"+fileName)).useDelimiter("\\Z").next();
    }
}
