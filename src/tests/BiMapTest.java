package tests;

import static org.junit.Assert.assertEquals;
import model.BiMap;

import org.junit.Test;

public class BiMapTest {

	@Test
	public void test() {
		BiMap<Integer,String> numToWord = new BiMap<Integer,String>();
		numToWord.put(0, "Hello");
		numToWord.put(1, "my");
		numToWord.put(2, "name");
		numToWord.put(3, "is");
		numToWord.put(4, "Joe");
		
		assertEquals(numToWord.size(),5);
		
		assertEquals(numToWord.get(0),"Hello");
		assertEquals(numToWord.get(1),"my");
		assertEquals(numToWord.get(2),"name");
		assertEquals(numToWord.get(3),"is");
		assertEquals(numToWord.get(4),"Joe");
		
		numToWord.remove(2);
		numToWord.put(2, "surname");
		numToWord.remove(4);
		numToWord.put(4, "Blogs");
		
		assertEquals(numToWord.get(2),"surname");
		assertEquals(numToWord.get(4),"Blogs");
		
		numToWord.remove(0);
		assertEquals(numToWord.get(0),null);
		
		assertEquals(numToWord.size(),4);
		
	}
	
	@Test
	public void test2(){

		BiMap<Integer,String> numToWord = new BiMap<Integer,String>();
		numToWord.put(0, "Hello");
		numToWord.put(1, "my");
		numToWord.put(2, "name");
		numToWord.put(3, "is");
		numToWord.put(4, "Joe");
		
		BiMap<String,Integer> wordToNum = numToWord.inverse();
		assert(wordToNum.inverse() == numToWord);
		
		assertEquals(wordToNum.size(),5);
		assertEquals(wordToNum.get("Joe"),new Integer(4));
		Boolean thrown = false;
		try{
			wordToNum.put("Joe", 40);
		}
		catch (RuntimeException e){
			thrown=true;
		}
		assertEquals(true,thrown);
		
		assertEquals(wordToNum.get("Joe"), new Integer(4));
		
		wordToNum.put(".", 5);
		
		assertEquals(numToWord.get(5), ".");   // changes made to inverse affect natural direction
		
	}
	
	

}
