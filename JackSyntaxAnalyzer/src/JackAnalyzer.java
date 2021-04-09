import java.io.File;
import java.io.IOException;

/*  JackAnalyzer v1.0
 * 	Author: Yusuf Bekirov
 * 
 *  The JackAnalyzer class serves the main driver for the JackSyntaxAnalyzer 
 *  Program, a part of the overall Jack Compiler. When given a valid .jack file,
 *  the JackAnalyzer instantiates JackTokenizer to write to an output .xml file,
 *  in accordance to the data read as input into the JackTokenizer, the output is
 *  wraped in a <tokens><\tokens> wrap. This output is then sent into the 
 *  Compilation Engine to format into a complete .xml files. Current version wont
 *  support expressions. The first arguement should be the filename or foldername.
 * 
 */
public class JackAnalyzer {
	
	public static void main(String[] args) throws IOException {
		CompilationEngine engineNow = null;
		//if(args[0].endsWith(".jack")) {
		//CompilationEngine engineNow = new CompilationEngine("Main.jack");
		//}else {
        	File folder = new File("Pong");
        	//System.out.println("Folder");
			for(File file : folder.listFiles()) {
				if((file.getName()).endsWith(".jack")) {
					System.out.println("File: " + file.getName());
					 engineNow = new CompilationEngine(file.getAbsolutePath());
					 engineNow = null;
				}	
			}
		//}
	//}
	}
}
	
