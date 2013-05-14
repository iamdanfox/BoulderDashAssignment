/**
 * 
 */
package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author danfox
 * Allows the convenient representation of Cave Grids in text format.
 */
public class LevelStorer {
    
    public final static String DIRECTORY = "./levels";
    
    /**
     * Converts a String representation into a Cave.
     * @param source
     * @return
     */
    public static Cave lex(String source){
        source = '\n' + source;
        Pattern linesPattern = Pattern.compile("\n[^\n]*");
        
        Pattern lexParamPattern = Pattern.compile("\\[.*\\]$");
        Pattern caveParamPattern = Pattern.compile("\\{.*\\}$");
        Matcher lines = linesPattern.matcher(source);
        Integer caveLineStart = null;
        Integer caveStart = null;
        
        Integer diamondTarget=null;

        Map<Character,CaveElement> charMap = new HashMap<Character,CaveElement>();
        
        int lineno = 0;
        for (lineno = 0; lines.find(); lineno++) {
            String rawline = source.substring(lines.start() + 1, lines.end());
            String line = rawline.replaceAll("\\s","");
            
            //System.out.println("'"+line+"'");
            
            if (lexParamPattern.matcher(line).find()){ // is Lexer parameter
                String s = line.replaceAll("[\\[\\]\\s]", "");
                String key = s.substring(0, s.indexOf(':'));
                String val = s.substring(s.indexOf(':')+1);
                
                if (key.equals("Version")) {
                    if (!val.equals("1")) throw new RuntimeException("Can't parse anything other than version 1");
                } else if (key.equals("Boulder")){
                    charMap.put(val.charAt(0),new Boulder(null));                    
                } else if (key.equals("Dirt")){
                    charMap.put(val.charAt(0),new Dirt(null));
                }else if (key.equals("Player")){
                    charMap.put(val.charAt(0),new Player(null));
                }else if (key.equals("Wall")){
                    charMap.put(val.charAt(0),new Wall(null));
                }else if (key.equals("Diamond")){
                    charMap.put(val.charAt(0),new Diamond(null));
                }else if (key.equals("Default")){
                    charMap.put(val.charAt(0),null);
                }else {
                    throw new RuntimeException("Didn't understand Lexer Parameter: "+key+"="+val);
                }
            } else if (caveParamPattern.matcher(line).find()){
                String s = line.replaceAll("[\\{\\}\\s]", "");
                String key = s.substring(0, s.indexOf(':'));
                String val = s.substring(s.indexOf(':')+1);
                if (key.equals("diamondTarget"))
                    diamondTarget = Integer.parseInt(val);
                else
                    throw new RuntimeException("Didn't understand Cave Parameter: "+s);
            } else if(caveStart == null && line.length()>0) {
                // found the first line of the cave
                caveLineStart = lineno;
                caveStart = lines.start()+1;
            }
            
        }
        int gridHeight = lineno-caveLineStart-1;
        String gridStr = source.substring(caveStart);
        //System.out.println(gridHeight);
        int gridWidth = gridStr.indexOf('\n');
        gridWidth = (gridWidth==-1) ? gridStr.length()/2 : gridWidth/2;
        
        Cave cave = new Cave(gridWidth,gridHeight);
        cave.setDiamondTarget(diamondTarget);
        
        gridStr = '\n'+gridStr;
        Matcher caveLines = linesPattern.matcher(gridStr);
        for (int y = 0; caveLines.find(); y++) {
            String row = gridStr.substring(caveLines.start() + 1, caveLines.end()).replaceAll("\\s", "");
            for (int x =0;x<row.length();x++){
                char ch = row.charAt(x);
                CaveElement e =  charMap.get(ch);
                if (e!=null) cave.setElementAt(new Point(x,y),e.cloneToCave(cave));
            }
        }
        return cave;
    }
    
    /**
     * Converts a cave to a string representation.
     * @param cave
     * @return
     */
    public static String antiLex(Cave cave){
        StringBuilder sb = new StringBuilder();
        
        @SuppressWarnings("serial")
        TreeMap<String, Character> ch = new TreeMap<String,Character>(){{
            put(Boulder.class.getSimpleName(), 'o'); 
            put(Diamond.class.getSimpleName(), '*');
            put(Dirt.class.getSimpleName(), '+');
            put(Wall.class.getSimpleName(), '#'); // store colour (r,g,b)
            put(Player.class.getSimpleName(), '<'); // store direction (<,>,^,v)
            put("Default",'.');
        }};
        
        
        sb.append("[Version: 1]\n");
        for (Map.Entry<String,Character> entry : ch.entrySet()){
            sb.append("["+entry.getKey()+": "+entry.getValue()+"]\n");
        }
             
        sb.append("{diamondTarget: "+cave.getDiamondTarget()+"}\n");
        
        for(int y=0;y<cave.height;y++){
            for(int x=0;x<cave.width;x++){
                CaveElement e = cave.getElementAt(new Point(x,y));
                // execute for non blank squares
                if (e != null) {
                    Character c = ch.get(e.getClass().getSimpleName()); 
                    if (c!=null) sb.append(c); else sb.append('?');
                }else
                    sb.append(ch.get("Default"));
                sb.append(' ');
            }
            sb.append('\n');
        }

        return sb.toString();
    }
    
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
     */
    public static void writeToFile(String contents){
        writeToFile(findUnusedFilename(),contents);
    }
    
    /**
     * Saves a string to a specified filename.
     * @param fileName
     * @param contents
     */
    public static void writeToFile(String fileName, String contents) {        
        try {
            PrintWriter out = new PrintWriter(DIRECTORY+"/"+fileName+".txt");
            out.println(contents);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static String readFromFile(String fileName) throws FileNotFoundException{
        BufferedReader reader = new BufferedReader( new FileReader (DIRECTORY+"/"+fileName));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while( ( line = reader.readLine() ) != null ) {
                stringBuilder.append( line );
                stringBuilder.append( ls );
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
    
    public static Cave loadCaveFromFile(String fileName) throws FileNotFoundException{
        return lex(readFromFile(fileName));
    }
}
