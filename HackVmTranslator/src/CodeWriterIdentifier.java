
public class CodeWriterIdentifier {
	
	static String functionName = "";
	
	//Bootstrap code for the VM to call Sys.init to initialize the VM
	public static String writeInit(String fileName, int cmdno) {
		String command = "//Looking at command Init \n";
		String formattedFileName = (fileName.split("\\."))[0];
		StringBuilder str = new StringBuilder((command + "@256\nD=A\n@SP\nM=D\n"));		
		System.out.println("Calling Sys.init function for file: " + formattedFileName);
		return (str.append(writeCall("Sys.init", 0, cmdno, formattedFileName)).toString());
	}
	//Returns the assembly code that 
	//implements the given arithmetic command
	public static String writeArithmetic(String arithmeticCMD, int commandno) {
		String command = "//Looking at command '" + arithmeticCMD +"'\n";
		
		String spDecrementerAssigner = "@SP\nAM=M-1\n";
		String spIncrementor = "@SP\nM=M+1\n";
		String dSaveMemory = "D=M\n";
		String memoryAssignPlus = "M=D+M\n";
		String memoryAssignMinus = "M=M-D\n";
		String memoryAssignNeg = "M=-M\n";
		String atlabeleq = "@EQCMD" + commandno + "\nD;JEQ\n";
		String atlabelgt = "@GTCMD" + commandno + "\nD;JGT\n";
		String atlabellt = "@LTCMD" + commandno + "\nD;JLT\n";
		String atlabelnot = "D=!M\n@NOTCMD" + commandno + "\nD;JNE\n";
		String atlabeland = "D=D&M\n@ANDCMD" + commandno + "\nD;JNE\n";
		String atlabelor = "D=D|M\n@ORCMD" + commandno + "\nD;JNE\n";
		String labeleq = "(EQCMD" + commandno + ")\n";
		String labelgt = "(GTCMD" + commandno + ")\n";
		String labellt = "(LTCMD" + commandno + ")\n";
		String labelnot = "(NOTCMD" + commandno + ")\n";
		String labeland = "(ANDCMD" + commandno + ")\n";
		String labelor = "(ORCMD" + commandno + ")\n";
		String falseAssigner = "@SP\nA=M\nM=0\n@SPINCMD"+ commandno + "\n0;JMP\n";
		String trueAssigner = "@SP\nA=M\nM=-1\n(SPINCMD"+ commandno + ")\n";
		String trueAssignerANDORNOT = "@SP\nA=M\nM=D\n(SPINCMD"+ commandno + ")\n";
		
		if(arithmeticCMD.equals("add")) {
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + memoryAssignPlus + spIncrementor);
		}else if(arithmeticCMD.equals("sub")) {
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + memoryAssignMinus + spIncrementor);
		}else if(arithmeticCMD.equals("neg")) {
			return (command + spDecrementerAssigner + memoryAssignNeg + spIncrementor);
		}else if(arithmeticCMD.equals("eq")) {
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + memoryAssignMinus + dSaveMemory + atlabeleq + falseAssigner + labeleq + trueAssigner + spIncrementor);
		}else if(arithmeticCMD.equals("gt")) {
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + memoryAssignMinus + dSaveMemory + atlabelgt + falseAssigner + labelgt + trueAssigner + spIncrementor);
		}else if(arithmeticCMD.equals("lt")) {
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + memoryAssignMinus + dSaveMemory + atlabellt + falseAssigner + labellt + trueAssigner + spIncrementor);
		}else if(arithmeticCMD.equals("and")) { 
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + atlabeland + falseAssigner + labeland + trueAssignerANDORNOT + spIncrementor);
		}else if(arithmeticCMD.equals("or")) {
			return (command + spDecrementerAssigner + dSaveMemory + spDecrementerAssigner + atlabelor + falseAssigner + labelor + trueAssignerANDORNOT + spIncrementor);
		}else if(arithmeticCMD.equals("not")) {
			return (command + spDecrementerAssigner + atlabelnot + falseAssigner +  labelnot + trueAssignerANDORNOT + spIncrementor);
		}
		return arithmeticCMD;
	}
	
	//Returns the assembly code that 
    //implements the given push/pop command
	public static String writePushPop(String pushpopCMD, String segmentType, int value, String filename) {
		String command = "//Looking at command '" + pushpopCMD +"' for segment " + segmentType + " and value " + value +" in file " + filename + "\n";
		
		if(pushpopCMD.equals("pop")) {
			
			String firstpart = "@SP\nM=M-1\n@" + value + "\nD=A\n";
			String secondpart = "D=D+M\n@SP\nA=M\nD=D+M\nA=D-M\nM=D-A";
			String intermediate = null;
			
			if ((segmentType.equals("local") || segmentType.equals("argument") || segmentType.equals("this") || segmentType.equals("that"))) {
				if(segmentType.equals("local")) {
					intermediate = "@LCL\n";
				}else if(segmentType.equals("argument")) {
					intermediate = "@ARG\n";
				}else if(segmentType.equals("this")) {
					intermediate = "@THIS\n";
				}else if(segmentType.equals("that")) {
					intermediate = "@THAT\n";
				}
				return (command + firstpart + intermediate + secondpart);
			}
			if(segmentType.equals("static")) {
		        return (command + "@SP\nAM=M-1\nD=M\n@" + filename + "." + value + "\nM=D\n");
			}else if(segmentType.equals("pointer")) {
				String thisthat = null;
				if(value == 0) {
					thisthat = "THIS";
				}else if (value == 1) {
					thisthat = "THAT";
				}
				return (command + "@SP\nAM=M-1\nD=M\n@" + thisthat + "\nM=D\n");
			}else if(segmentType.equals("temp")) {
				return (command + "@SP\nM=M-1\n@" + value + "\nD=A\n@5\nD=D+A\n@SP\nA=M\nD=D+M\nA=D-M\nM=D-A\n");
			}	
		}else if(pushpopCMD.equals("push")) {
			
			String firstpart = "@" + value + "\nD=A\n";
			String secondpart = "D=D+M\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
			String intermediate = null;
			
			if ((segmentType.equals("local") || segmentType.equals("argument") || segmentType.equals("this") || segmentType.equals("that"))) {
				if(segmentType.equals("local")) {
					intermediate = "@LCL\n";
				}else if(segmentType.equals("argument")) {
					intermediate = "@ARG\n";
				}else if(segmentType.equals("this")) {
					intermediate = "@THIS\n";
				}else if(segmentType.equals("that")) {
					intermediate = "@THAT\n";
				}
				return (command + firstpart + intermediate + secondpart);
			}
			if(segmentType.equals("constant")) {
				return (command + "@" + value + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
			}else if(segmentType.equals("static")) {
				return (command + "@" + filename + "." + value + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
			}else if(segmentType.equals("pointer")) {
				String thisthat = null;
				if(value == 0) {
					thisthat = "THIS";
				}else if (value == 1) {
					thisthat = "THAT";
				}
				return (command + "@" + thisthat + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
			}else if(segmentType.equals("temp")) {
				return (command + "@" + value + "\nD=A\n@5\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
			}
		}
		return (command + ": Returned NULL");			
	}
	
	//Returns assembley code corresponding to label command
	public static String writeLabel(String labelName, String fileName) {
		String command = "//Looking at command LABEL \n";
		String formattedLabelName =  (fileName + "." + functionName + "$" + labelName);
		return (command + "(" + formattedLabelName + ")\n");
	}
	
	public static String writeGoto(String labelName, String fileName) {
		String command = "//Looking at command GOTO \n";
		String formattedLabelName =  (fileName + "." + functionName + "$" + labelName);
		return (command + "@" + formattedLabelName + "\n0;JMP\n");
	}
	
	public static String writeIf(String labelName, String fileName) {
		String command = "//Looking at command IF \n";
		String formattedLabelName =  (fileName + "." + functionName + "$" + labelName);
		String spDecrementerAssigner = "@SP\nAM=M-1\n";
		String dSaveMemory = "D=M\n";
		return (command + spDecrementerAssigner + dSaveMemory + "@" + formattedLabelName + "\nD;JNE\n");
	}
	
	public static String writeFunction(String functionName, String fileName, int localVars) {
		String command = "\n//Looking at command Function: "+ functionName + " for file: " + fileName + "\n ";
		CodeWriterIdentifier.functionName = functionName;
		String formattedLabelName =  ("(" + fileName + "." + functionName + ")\n");
		for(int i = 0; i < localVars; i++) {
			formattedLabelName = formattedLabelName + writePushPop("push", "constant", 0 , fileName);
		}
		return (command + formattedLabelName);
	}
	
	public static String writeCall(String functionName, int argVal, int commandno, String fileName) {
		String command = "//Looking at command Call \n";
		String formattedfunctionName = (fileName + "." + functionName);
		String formattedReturnLabelName =  (formattedfunctionName + "$ret." + commandno);
		String pushAttachment = "@SP\nA=M\nM=D\n@SP\nM=M+1\n";
		String reposition = "@5\nD=A\n@" + argVal + "\nD=D+A\n@SP\nD=M-D\n@ARG\nM=D\n@SP\nD=M\n@LCL\nM=D\n";

		String returnString = (command + "@" + formattedReturnLabelName + "\nD=A\n" + pushAttachment
				+ "@LCL\nD=M\n" + pushAttachment + "@ARG\nD=M\n" + pushAttachment + "@THIS\nD=M\n" + pushAttachment 
				+"@THAT\nD=M\n" + pushAttachment + reposition + "@" + formattedfunctionName + "\n0;JMP\n(" 
				+ formattedReturnLabelName + ")\n");
		//System.out.println("formattedName: " + formattedfunctionName + "\n filename: " + fileName); 
		
		//str.append(writeGoto(formattedfunctionName, fileName));
		//String formattedLabelName =  (fileName + "." + formattedfunctionName);
		//(command + "@" + formattedLabelName + "\n0;JMP\n");
		//str.append("(" + formattedReturnLabelName + ")\n");
		return (returnString);
	}
	
	public static String writeReturn() {
		String command = "//Looking at command RETURN \n";
		String endFrameInit = "@LCL\nD=M\n@R13\nM=D\n";
		String atRETAddressInit = "@5\nD=A\n@R13\nA=M-D\nD=M\n@R14\nM=D\n";
		String spDecrementerAssigner = "@SP\nAM=M-1\n";
		String reposition = spDecrementerAssigner + "D=M\n@ARG\nA=M\nM=D\n@ARG\nD=M+1\n@SP\nM=D\n";
		String atTHATAddressInit = "@1\nD=A\n@R13\nA=M-D\nD=M\n@THAT\nM=D\n";
		String atTHISAddressInit = "@2\nD=A\n@R13\nA=M-D\nD=M\n@THIS\nM=D\n";
		String atARGAddressInit = "@3\nD=A\n@R13\nA=M-D\nD=M\n@ARG\nM=D\n";
		String atLCLAddressInit = "@4\nD=A\n@R13\nA=M-D\nD=M\n@LCL\nM=D\n";
		String goReturn = "@R14\nA=M\n0;JMP\n";
		return(command + endFrameInit + atRETAddressInit + reposition + atTHATAddressInit
				+ atTHISAddressInit + atARGAddressInit + atLCLAddressInit + goReturn);
		
	}
	
	
}
