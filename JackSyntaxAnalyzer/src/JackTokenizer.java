import java.io.*;
import java.util.ArrayList;

/*
 *  JackTokenizer serves as a utility function for
 *  the iteration of available tokens in the tokenList
 *  ArrayList provided by the JackTokenizerIdentifier.
 *  CompilationEngine uses these functions to construct
 *  a formatted xml file based on type of tokens.
 * 
 */
public class JackTokenizer {
	Token currentToken;
	JackTokenizerIdentifier tokenizer;
	JackTokenizerWriter tokenWriter;
	int currentIndex;
	
	public JackTokenizer(String fileName) {
		tokenizer = new JackTokenizerIdentifier(fileName);
		String tokenFileName = fileName.split(".jack")[0];
		tokenWriter = new JackTokenizerWriter(tokenFileName + "T.xml", tokenizer.tokenList);
		currentToken = tokenizer.tokenList.get(0);
		currentIndex = 0;
	}
	
	public boolean hasMoreTokens() {
		if(currentIndex < (tokenizer.tokenList.size()-1)) {
			return true;
		}
		return false;
	}
	
	public void advance() {
		if(hasMoreTokens()) {		
			currentIndex++;
			currentToken = tokenizer.tokenList.get(currentIndex);
			//System.out.println("Looking at token value: " + currentToken.getValue());
		}
	}
	
	public String tokenType() {
		return currentToken.getType();
		
	}
	
	public String keyword() {
		if(tokenType().equals("keyword")) {
			return currentToken.getValue();
		}
		return null;
		
	}
	
	public char symbol() {
			return currentToken.getValue().charAt(0);	
	}
	public String identifier() {
			return currentToken.getValue();
	}
	
	public int intVal() {
			return Integer.parseInt(currentToken.getValue());	
	}
	
	public String stringVal() {
		return currentToken.getValue();
		
	}
}
