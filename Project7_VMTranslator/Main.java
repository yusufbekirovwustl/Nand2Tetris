import java.io.*;
import java.util.ArrayList;


/*  HackVmTranslator v1.1
 * 	Author: Yusuf Bekirov
 * 
 *  The main class serves the main driver for the HackVmTranslator
 *  When given a .vm input filename generated from a Jack compiler, the
 *  main method will allow for the Parser to parse and seperate vm commands
 *  and the CodeWriter class will allow for the vm commands to be converted 
 *  into Hack assembly commands, which are placed into a asm file placed in
 *  the same directory and of same name as the .vm input file
 * 
 */
interface MainInterface {
	
}

public class Main implements MainInterface {
	
    static ArrayList<Parser.VmCommand> commandlist=new ArrayList<Parser.VmCommand>();
	static Parser.VmCommand vmContent = null;
	
	// When given a valid Jack .vm file as arguement, drives
	//the process to convert it to a Hack .asm file
	public static void main(String[] args) throws IOException {
		if(args[0].endsWith(".vm")) {
			Parser parseVM = new Parser(args[0]);
			while(parseVM.hasMoreCommands()) {				
				vmContent = ParserSeperator.seperator(parseVM.currentLine);
				if(vmContent != null) { // If vmContent is only a valid vmCOntent object decoded by ParserSeperator 
					commandlist.add(vmContent);
				}
				parseVM.nextLine();
			}			
			String formattedFileName = (args[0].split("\\."))[0];
			CodeWriter writeASM = new CodeWriter(formattedFileName+ ".asm", formattedFileName, false, false);
		}else{
			File folder = new File(args[0]);
			String formattedFileName = (args[0].split("\\."))[0];
			CodeWriter writeASMInit = new CodeWriter(formattedFileName+ ".asm",args[0], false, true);
			for(File file : folder.listFiles()) {
				//System.out.println("File found:" + file.getName()); 
				if((file.getName()).endsWith(".vm")) {
					System.out.println("Current File : " + file.getName());
					Parser parseVM = new Parser(args[0] + "/" +file.getName());
					while(parseVM.hasMoreCommands()) {
						vmContent = ParserSeperator.seperator(parseVM.currentLine);
						if(vmContent != null) { // If vmContent is only a valid vmCOntent object decoded by ParserSeperator 
							commandlist.add(vmContent);
						}
						parseVM.nextLine();
					}
					String formattedFromFileName = (file.getName().split("\\."))[0];
					CodeWriter writeASM = new CodeWriter(formattedFileName + ".asm", formattedFromFileName, true, false);
				    commandlist.clear();
				}	
			}
		
		}
		
	}
}
