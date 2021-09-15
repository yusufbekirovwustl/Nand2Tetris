import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * The JackTokenizerWriter class recieves a filename input
 * to write to a output token file, where the tokens to be formatted
 * exist in an arrayList exists as a static member of the JackTokenizer 
 * class. This arrayList is iterated through and added to the output token file.
 * 
 */
public class JackTokenizerWriter {
	
	public JackTokenizerWriter(String fileName, ArrayList<Token> tokenList){
		System.out.println("Hello");
		try {
			File tokenfile = new File(fileName);
			System.out.println("Done");
			if (tokenfile.createNewFile()) {
			        System.out.println("File created: " + tokenfile.getName());
			      } else {
			        System.out.println("File "+ "'" + fileName + " already exists.");
			      }
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		}
		
		FileWriter tokenfileWriter = null;
		
		try {
			tokenfileWriter = new FileWriter(fileName);
			tokenfileWriter.write("<tokens>\n");
			for(Token token: tokenList) {
				tokenfileWriter.write(token.toString() + "\n");
			}
			tokenfileWriter.write("</tokens>");
	
			
		}catch (IOException e) { 
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally{
			try {
				tokenfileWriter.close();
			}catch(IOException ioe) {
				System.out.println("Error in closing File Writer");
			}
		}	
	}
}
