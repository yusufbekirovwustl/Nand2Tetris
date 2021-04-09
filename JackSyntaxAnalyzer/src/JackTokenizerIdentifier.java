import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * JackTokenizerIdentifier reads a .jack file and identifies
 * the type and value of tokens placed into a tokenList
 * 
 */
public class JackTokenizerIdentifier {
	BufferedReader jackReader;
	ArrayList<Token> tokenList;
	
	public JackTokenizerIdentifier(String fileName) {
		
		jackReader = null;
	    tokenList=new ArrayList<Token>();
		
		try {
			jackReader = new BufferedReader(new FileReader(fileName));
			
			String symbols = "{}()[].,;+-*/&|<>=~";
			String digits = "0123456789";
			
			String Str =  jackReader.readLine();			
			while (Str != null) {
				String strS = Str.trim();
				if(strS.startsWith("/**")){
					while(!(strS.contains("*/"))) {
						Str =  jackReader.readLine();
						strS = Str.trim();
					}
					Str =  jackReader.readLine(); // Go to line after API command
				}

				if(!(Str.startsWith("//"))) { //checks for comment and API lines
					if(Str.contains("//")) { //checks for inline comments within valid code
						Str = Str.split("//")[0];
					}
					String str = Str.trim();
					str =  str.replaceAll("\t", "");// clear tabs and leading/ending whitespace
					
					String tokenBuilder = "";
					for(int i= 0; i<str.length(); i++) { // goes through characters in the line
						char currentChar = str.charAt(i);
						tokenBuilder = tokenBuilder + currentChar;
						
						if(tokenBuilder.equals("class") || tokenBuilder.equals("constructor") || tokenBuilder.equals("function") || tokenBuilder.equals("method") || tokenBuilder.equals("field") ||
								tokenBuilder.equals("static") || tokenBuilder.equals("var") || tokenBuilder.equals("int") || tokenBuilder.equals("char") || tokenBuilder.equals("boolean") ||
								tokenBuilder.equals("void") || tokenBuilder.equals("true") || tokenBuilder.equals("false") || tokenBuilder.equals("null") || tokenBuilder.equals("this") || 
								tokenBuilder.equals("let") || tokenBuilder.equals("do") || tokenBuilder.equals("if")|| tokenBuilder.equals("else") || tokenBuilder.equals("while") || 
								tokenBuilder.equals("return")) {
							
							Token keyword = new Token("keyword", tokenBuilder);
							tokenList.add(keyword);						
							tokenBuilder = ""; //reset token
						}
						
						else if(symbols.contains(tokenBuilder) && !(tokenBuilder == " ")){
							
							Token keyword = new Token("symbol", tokenBuilder);
							tokenList.add(keyword);
							tokenBuilder = "";
						}
												
						if((i+1)<str.length()) {
							if((str.charAt(i+1) == ' ' || (symbols.contains("" + str.charAt(i+1))))) { //If next character is a symbol or a space
								
								if(!tokenBuilder.equals("")) {
									String trimTokenBuilder = tokenBuilder.trim();
									if(isNumeric(trimTokenBuilder)) {
										Token keyword = new Token("integerConstant", tokenBuilder.trim());
										tokenList.add(keyword);
									}
									else if(isStringConst(trimTokenBuilder)) {
										Token keyword = null;
										int length = trimTokenBuilder.length();
										String startString = trimTokenBuilder.substring(1,length); //Starting word
										int startLength = startString.length();
										if(startString.charAt(startLength-1) == '"') {//If starting word has '"' at end
											keyword = new Token("stringConstant", startString.substring(0, length-1));
										}else {
											i++;
											while(str.charAt(i) != '"') {//Wait till end of String
												startString = startString + str.charAt(i);
												i++;
											}
											startLength = startString.length();
											keyword = new Token("stringConstant", startString.substring(0, startLength));
										}
											
										tokenList.add(keyword);
									
									}else {
											Token keyword = new Token("identifier", trimTokenBuilder);
											tokenList.add(keyword);
									}
									
								}							
								while(str.charAt(i+1) == ' ') {
									i++;
								}
								tokenBuilder = "";
							}
						}
						
					}
				}			
				Str =  jackReader.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	public static boolean isNumeric(String str) { 
		  try {  
		    Integer.parseInt(str) ; 
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
	}
	
	public static boolean isStringConst(String str) { 
		  int length= str.length();
		  if(length> 2) {
			  if(str.charAt(0) == '"') {
				  return true;  
			  }
		  }
		  
		  return false;
	}
	
	public void close() {
		try {
			jackReader.close();
		}catch(IOException ioe) {
			System.out.println("Error in closing File Reader");
		}
	}
}
