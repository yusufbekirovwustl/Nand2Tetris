import java.io.*;

/*  HackVmTranslator v1.1
 * 	Author: Yusuf Bekirov
 * 
 *  The Parser class serves to open .vm file
 *  and parse given commands in the file into lexical components
 *  of type and arguements, if existing for the particular command type.
 *  This parsed command is placed into the public ArrayList initialized in the main 
 *  class, which will be used by the CodeWriter during writing assembly commands
 *  
 */
interface ParserFile {
	public void close();
	public boolean hasMoreCommands();
	public void nextLine();
}
interface VMCMD {
	public void setCommandtype(String commandtype);
	public void setArg1(String arg1) ;
	public void setArg2(int arg2);
	
} 
public class Parser implements ParserFile {
	BufferedReader vmReader = null;
	String currentLine = null;
	
	public Parser(String fileName) throws IOException {
		
			vmReader = new BufferedReader(new FileReader(fileName));
			currentLine = vmReader.readLine();
    }
	
	public void close() {
		try {
			if(this.vmReader != null) {
				this.vmReader.close();
			}
		}
		catch(IOException ioe) {
			System.out.println("Error in closing Buffered Reader");
		}
	}
	
	public boolean hasMoreCommands() {
		if(currentLine == null) {
			return false;
		} else{
			return true;
		}
		
	}
	
	public void nextLine() {
		try {
			currentLine = vmReader.readLine();
		} catch (IOException e) {
			currentLine = null;
		}
	}
	
	//vmCommand class used to instantiate, set and aquire different VM fields
	public static class VmCommand implements VMCMD {
		public String fullCMD;
		public String commandtype;
		public String arg1;
		public int arg2;
		
		public VmCommand(String command){
			this.fullCMD = command;
			this.commandtype = null;
			this.arg1 = null;
			this.arg2 = 0;
		}

		public void setCommandtype(String commandtype) {
			this.commandtype = commandtype;
		}

		public void setArg1(String arg1) {
			this.arg1 = arg1;
		}


		public void setArg2(int arg2) {
			this.arg2 = arg2;
		}
		
	}
}

