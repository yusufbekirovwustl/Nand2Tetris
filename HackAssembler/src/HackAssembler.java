import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * HackAssembler v1.1
 * Author: Yusuf Bekirov
 * Given a valid Hack assemblyfilename with symbol functionality, 
 * HackAssembler will assemble it into Hack Binary machine language 
 * 
 */
public class HackAssembler {

	public static ArrayList<String> binlist=new ArrayList<String>();
	public static HashMap<String,String> symbolTable = new HashMap<String,String>();
	public static int variableCounter = 16;
	
	public static void main(String[] args) throws IOException {
		//We are assuming that we are provided a valid error-free .asm file as the arguement to main
		System.out.println("Assembler started");
		String filename = args[0];
		symbolResolver(filename);
		output(filename);
		System.out.println("Assembly complete");
		//System.out.println("filename: " + filename);
		 
		
	}
	/* 
	 * The Parser loads and reads the assembly file
	 * The instruction type in a particular instruction is identified
	 * and the seperate components of that instruction is identified,
	 * these components are then passed into specific instruction type Assemblers
	 * The specific instruction assemblys return a binary coded string which is added to an ArrayList
	 * 
	 */
	public static void Parser(String filename) throws IOException {
		BufferedReader asmReader = null;
		try {
			asmReader = new BufferedReader(new FileReader(filename));
			String contentLine = asmReader.readLine();
			  while (contentLine != null) {
				 contentLine = contentLine.replaceAll("\\s", "");
				 if((contentLine.startsWith(" ", 0) || contentLine.contentEquals("") || contentLine.startsWith("//", 0) || contentLine.startsWith("("))) {
					 //System.out.println("Whitespace Detected, Ignoring");
					//System.out.println("Comment Detected, Ignoring");
				 }
				 else if(contentLine.contains("@")) {//A type instruction
					 String[] value = contentLine.split("[@\\/]");
					 AInstruction aIns = new AInstruction(value[1]);
					 String encode = aIns.encodeAInstruction();
					 //System.out.println("encode value: " + encode);
					 binlist.add(encode);
				 }
				 else if(contentLine.contains("=")) {//C type instruction, where dest exists
					 String[] value = contentLine.split("[=\\/]");
					 String dest = value[0];
					 String comp = null;
					 String jump = "null";
					 if(value[1].contains(";")) { //C type has non-null jump instruction
						 String[] valueSecond = value[1].split("[;\\/]");
						 comp = valueSecond[0];
						 jump = valueSecond[1];
					 }else {
						 comp = value[1];
					 }
					 
					 CInstruction cIns = new CInstruction(dest,comp,jump); 
					 String encode = cIns.encodeCInstruction();
					 binlist.add(encode);					 
				} 
				else {
					 String dest = "null";
					 String comp = null;
					 String jump = "null";
					 if(contentLine.contains(";")) { //Jump is non-null
						 String[] value = contentLine.split("[;\\/]");
						 comp = value[0];
						 jump = value[1];
					 }
					 else if(contentLine != null) {
						 comp = contentLine;
					 }
					 
					 CInstruction cIns = new CInstruction(dest,comp,jump); 
					 String encode = cIns.encodeCInstruction();
					 binlist.add(encode);
					 
				 }
				 contentLine = asmReader.readLine();
			  }
		}
	
		finally {
			try {
				if(asmReader != null) {
					asmReader.close();
				}
			}
			catch(IOException ioe) {
				System.out.println("Error in closing Buffered Reader");
			}
		}
		 
	}
	/*
	 * symbolResolver, when given a filename of type Hack Assembly, initializes a Hashmap called symbolTable
	 * with predefined symbols included in the language, then finds label declarations in the Assembly file and adds to the symbolTable
	 */
	public static void symbolResolver(String filename) throws IOException {
		//Initialize Hashmap with predefined values
		BufferedReader symReader = null;
		try {
			symReader = new BufferedReader(new FileReader("predefinedSymbols"));
			String symLine = symReader.readLine();
			  while (symLine != null) {//Prepopulate symbolTable with predefined symbols
				 String[] symPart = symLine.split(",");
				 symbolTable.put(symPart[0], symPart[1]);
				 symLine = symReader.readLine();
			  }
			  //System.out.println(Arrays.asList(symbolTable));
		
		}
		finally {
			try {
				if(symReader != null) {
					symReader.close();
				}
			}
			catch(IOException ioe) {
				System.out.println("Error in closing Buffered Reader");
			}
		}
		
		//Try first pass to identify label symbols
		 BufferedReader asmReader = null;
		try {
		 	asmReader = new BufferedReader(new FileReader(filename));
		 	//System.out.println("First Pass");
			String contentLine = asmReader.readLine();
			int ins = 0;
			  while (contentLine != null) {
				 contentLine = contentLine.replaceAll("\\s", "");
				 //System.out.println("contentLine: " + contentLine + ",ins: " + ins);
				 if(contentLine.contains("(")) {
					 String key = contentLine.split("[\\(\\)]")[1];
					 //String[] key = contentPart[1].split(")");
					 //System.out.println("Label " + key +  " at instruction " + ins );
					 String insNum = Integer.toString((ins));
					 symbolTable.put(key, insNum);	 
				 }
				 contentLine = asmReader.readLine();
				 if(contentLine!= null) {
					 contentLine = contentLine.replaceAll("\\s", "");
					 //System.out.println("contentLine: " + contentLine);
					 if(!(contentLine.startsWith(" ", 0) || contentLine.contentEquals("") || contentLine.startsWith("//", 0) || contentLine.startsWith("("))) {						 
						 ins++; //Instruction number off by one but works to find val for symbol location
					 }
				 }
				 
			  }
			  //System.out.println(Arrays.asList(symbolTable));
		}
		finally {
			try {
				if(asmReader != null) {
					asmReader.close();
				}
			}
			catch(IOException ioe) {
				System.out.println("Error in closing Buffered Reader");
			}
		}
	}
	/*The output command calls the Parser command to allocate binary formatted strings
	 * to a public ArrayList. This ArrayList is used to format and create a Hack language file 
	 * to be placed in the directory in which the Hack Assembly file was aquired from.
	 */
	public static void output(String filename) throws IOException {
		Parser(filename);
		try {
			File hackfile = new File(filename + ".hack");
			 if (hackfile.createNewFile()) {
			        System.out.println("File created: " + hackfile.getName());
			      } else {
			        System.out.println("File "+ "'" + filename + ".hack' already exists.");
			      }
			    } catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		FileWriter hackfileWriter = null;
		try {
			hackfileWriter = new FileWriter(filename + ".hack");
			for(String str:binlist) {
				hackfileWriter.write(str + "\n");
			}
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally {
			try {
				hackfileWriter.close();	
			}
			catch(IOException ioe) {
				System.out.println("Error in closing File Writer");
			}
		}
		
	}
	
}
