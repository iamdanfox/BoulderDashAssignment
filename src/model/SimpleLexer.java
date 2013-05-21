package model;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.awt.Color;

public class SimpleLexer {


    /**
     * Converts a String representation into a Cave.
     * @param source
     * @return
     * @throws LexerException 
     */
    public static Cave lex(String text) throws LexerException{
        String[] rows = text.split("[\\r\\n]+");          
        
        int dy=1; // lines in file before cave starts
        int height = rows.length - dy;
        int width = rows[dy].length() / 2;
        
        Cave cave = new Cave(width,height);
        
        // find diamond target
        try {
            int diamondTarget = Integer.parseInt(rows[0].replaceAll("\\s", ""));
            cave.setDiamondTarget(diamondTarget);
        } catch (NumberFormatException e) {
            throw new LexerException("Couldn't set Diamond Target");
        }
        
        // populate cave
        for (int y = 0; y+dy < rows.length; y++) {
            char[] row = rows[y+dy].toCharArray();
            for (int x = 0; x < row.length / 2; x++) {
                char c = row[x * 2];
                CaveElement e = charToCaveElem(c);
                if (e != null)
                    if (!cave.setElementAt(new Point(x, y), e.cloneToCave(cave)))
                        throw new LexerException("Couldn't place element in cave");
            }
        }
        
        return cave;
    }

    protected static CaveElement charToCaveElem(Character c) {
        Map<Character, CaveElement> elemMap = new HashMap<Character, CaveElement>();
        
        elemMap.put('o', new Boulder(null));
        elemMap.put('*', new Diamond(null));
        elemMap.put('+', new Dirt(null));
        elemMap.put('#', new Wall(null));
        elemMap.put('R', new Wall(null, Color.red));
        elemMap.put('G', new Wall(null, Color.green));
        elemMap.put('B', new Wall(null, Color.blue));
        elemMap.put('>', new Player(null));
        
        return elemMap.get(c);
    }

    /**
     * Converts a cave to a simple string representation.
     * @param cave
     * @return
     */
    public static String antiLex(Cave cave) {
        StringBuilder sb = new StringBuilder();

        sb.append(cave.getDiamondTarget());
        for (int y = 0; y < cave.getHeight(); y++) {
            sb.append('\n');
            for (int x = 0; x < cave.getWidth(); x++) {
                CaveElement e = cave.getElementAt(new Point(x, y));
                // execute for non blank squares
                sb.append(caveElemToChar(e));
                sb.append(' ');
            }
        }

        return sb.toString();
    }

    protected static Character caveElemToChar(CaveElement e) {
        Map<String, Character> charMap = new HashMap<String, Character>();
        charMap.put("Boulder", 'o');
        charMap.put("Diamond", '*');
        charMap.put("Dirt", '+');
        charMap.put("Wall[Red]", '#');
        charMap.put("Wall[Green]", 'G');
        charMap.put("Wall[Blue]", 'B');
        charMap.put("Player[RIGHT]", '>');
        
        return (e == null) ? '.' : charMap.get(e.getClass().getSimpleName());
    }
    
    @SuppressWarnings("serial")
    public static class LexerException extends Exception{
        public LexerException(String msg){
            super(msg);
        }
    }
}
