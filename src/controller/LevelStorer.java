/**
 * 
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

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
    public static ArrayList<String> getLevelFiles(){
        return new ArrayList<String>(){{
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
     * @return a string in the format leveli where leveli.txt is the first
     *         unused filename in DIRECTORY
     */
    public static String findUnusedLevelname(){
        Collection<String> takenNames = getLevelFiles();
        int i=0;
        while (takenNames.contains("level"+i+".txt")) i++;
        return "level"+i;
    }
    
    /**
     * Saves a string to the first unused leveli.txt filename
     * @param contents
     * @return the filename that was written
     */
    public static String writeToFile(String contents){
        String fname = findUnusedLevelname()+".txt";
        try {
            writeToFile(fname,contents);
        } catch (FileNotFoundException e) {
            // this should never happen
            e.printStackTrace();
        }
        return fname;
    }
    
    /**
     * Saves a string to a specified filename.
     * @param fileName
     * @param contents
     * @throws FileNotFoundException 
     */
    public static void writeToFile(String fileName, String contents) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(DIRECTORY + "/" + fileName);
        out.println(contents);
        out.close();
    }
    
    public static String readFromFile(String fileName) throws FileNotFoundException{
        return new Scanner(new File(DIRECTORY+"/"+fileName)).useDelimiter("\\Z").next();
    }
}
