package tests;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

import model.Boulder;
import model.Cave;
import model.Player;
import model.SimpleLexer;
import model.SimpleLexer.LexerException;

import org.junit.Test;

public class SimpleLexerTest {

    @Test
    public void converterFunctionsFwd(){
        @SuppressWarnings("unused")
        SimpleLexer testRig = new SimpleLexer(){{
            Character[] cs = new Character[5];
            cs[0] = 'o';
            cs[1] = '*';
            cs[2] = '+';
            cs[3] = '#';
            cs[4] = '<';
            
            for (int i=0;i<cs.length;i++)
                assertEquals(cs[i], SimpleLexer.caveElemToChar(SimpleLexer.charToCaveElem(cs[i])));
        }};
    }
    
    @Test
    public void lexTest() throws LexerException{
        String[] strs = new String[3];
        strs[0]= "3\n< o . \n# # # ";
        strs[1]= "0\n< o . ";
        strs[2]= "100000\n. . . \n+ + + ";
        
        for (int i=0;i<strs.length;i++){
            System.out.println("EXPECTED:\n"+strs[i]);
            Cave c = SimpleLexer.lex(strs[i]);      
            System.out.println("ACTUAL:\n"+SimpleLexer.antiLex(c));
            assertEquals(strs[i], SimpleLexer.antiLex(c));
            System.out.println("");
        }
    }
    
    @Test
    public void testAntiLexingOneLine(){
        Cave cave2 = new Cave(3,1);
        Player myPlayer2 = new Player(cave2);
        cave2.setElementAt(new Point(0,0), myPlayer2); 
        cave2.setElementAt(new Point(1,0), new Boulder(cave2)); 
        
        String intended = "0" +
        		          "\n< o . ";
        assertEquals(intended,SimpleLexer.antiLex(cave2));// bit too strict atm.
    }
    
    @Test
    public void testAntiLexingTwoLine(){
        Cave cave2 = new Cave(3,2);
        Player myPlayer2 = new Player(cave2);
        cave2.setElementAt(new Point(0,1), myPlayer2); 
        cave2.setElementAt(new Point(1,0), new Boulder(cave2)); 
        cave2.setDiamondTarget(10);
        String intended = "10" +
                          "\n. o . " + 
                          "\n< . . ";
        assertEquals(intended,SimpleLexer.antiLex(cave2));// bit too strict atm.
    }
}
