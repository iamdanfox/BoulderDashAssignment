package model;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

public class SimpleLexer {


    /**
     * Converts a String representation into a Cave.
     * @param source
     * @return
     */
    public static Cave lex(String text){
        String[] rows = text.split("[\\r\\n]+");     
        
        int dy=1; // lines in file before cave starts
        int height = rows.length - dy;
        int width = rows[dy].length() / 2;
        
        Cave cave = new Cave(width,height);
        
        // find diamond target
        int diamondTarget = Integer.parseInt(rows[0].replaceAll("\\s", ""));
        cave.setDiamondTarget(diamondTarget);
        
        // populate cave
        for (int y = 0; y+dy < rows.length; y++) {
            char[] row = rows[y+dy].toCharArray();
            for (int x = 0; x < row.length / 2; x++) {
                char c = row[x * 2];
                CaveElement e = charToCaveElem(c);
                if (e != null)
                    cave.setElementAt(new Point(x, y), e.cloneToCave(cave));
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
        elemMap.put('<', new Player(null));
        
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
        charMap.put(Boulder.class.getSimpleName(), 'o');
        charMap.put(Diamond.class.getSimpleName(), '*');
        charMap.put(Dirt.class.getSimpleName(), '+');
        charMap.put(Wall.class.getSimpleName(), '#'); // TODO store colour (r,g,b)
        charMap.put(Player.class.getSimpleName(), '<'); // TODO store direction
        
        return (e == null) ? '.' : charMap.get(e.getClass().getSimpleName());
    }
}
