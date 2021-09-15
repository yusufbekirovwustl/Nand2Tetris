import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*  HackVmTranslator v1.1
 * 	Author: Yusuf Bekirov
 * 
 *  The CodeWiter class allows for the decoding of Hack vm code into Hack assembly counterparts,
 *  which are then written into a output file ending in .asm.
 *  Currently supports Arithmetic and push/pop command functionality.
 * 
 */
public class CodeWriter {
	//Constructor opens an output file to write to
	public CodeWriter(String filename, String fromFile, boolean append, boolean init){
		System.out.println("Adding to file " + filename + " from file " + fromFile);
		try {
			File asmfile = new File(filename);
			if (asmfile.createNewFile()) {
			        System.out.println("File created: " + asmfile.getName());
			      } else {
			        //System.out.println("File "+ "'" + filename + ".asm' already exists.");
			      }
			} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		FileWriter asmfileWriter = null;
		try {
			asmfileWriter = new FileWriter(filename, append);
			int cmdno = 0;
			int retno = 0;
			
			if(!init) {
				for(Parser.VmCommand vmcom: Main.commandlist) {
					String asmCommand = null;
					String formattedFileName = (filename.split("\\."))[0];
					System.out.println("Detect Command " + vmcom.commandtype);
					if(vmcom.commandtype.equals("C_ARIMETIC")) {
						String vmArithmeticType = ((vmcom.fullCMD).split("[;\\/]"))[0];
						asmCommand = CodeWriterIdentifier.writeArithmetic(vmArithmeticType, cmdno);
					}
					else if(vmcom.commandtype.equals("C_POP") || vmcom.commandtype.equals("C_PUSH")) {
						String[] vmValueType = (vmcom.fullCMD).split("[;\\/]");
						//String formattedFileName = (fromFile.split("."))[0];
						asmCommand = CodeWriterIdentifier.writePushPop(vmValueType[0],vmcom.arg1, vmcom.arg2, fromFile);
					}
					else if(vmcom.commandtype.equals("C_GOTO")) {
						asmCommand = CodeWriterIdentifier.writeGoto(vmcom.arg1, formattedFileName);
					}
					else if(vmcom.commandtype.equals("C_IF") ) {
						asmCommand = CodeWriterIdentifier.writeIf(vmcom.arg1, formattedFileName);
					}
					else if(vmcom.commandtype.equals("C_LABEL") ) {

						asmCommand = CodeWriterIdentifier.writeLabel(vmcom.arg1, formattedFileName);
					}
					else if(vmcom.commandtype.equals("C_FUNCTION") ) {

						asmCommand = CodeWriterIdentifier.writeFunction(vmcom.arg1, formattedFileName, vmcom.arg2);
					}
					else if(vmcom.commandtype.equals("C_CALL") ) {

						asmCommand = CodeWriterIdentifier.writeCall(vmcom.arg1,vmcom.arg2,cmdno,formattedFileName);
					}
					else if(vmcom.commandtype.equals("C_RETURN") ) {

						asmCommand = CodeWriterIdentifier.writeReturn();
					}
					asmfileWriter.write(asmCommand + "\n");
					cmdno++;
				}
				//System.out.println("Successfully wrote to the file.");
			}else {
				System.out.println("DetectInit");
				asmfileWriter.write(CodeWriterIdentifier.writeInit(filename, cmdno));
				cmdno++;
			}
			
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally {
			try {
				asmfileWriter.close();	
			}
			catch(IOException ioe) {
				System.out.println("Error in closing File Writer");
			}
		}
	}
	
}
