import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
	FileWriter vmfileWriter = null;
	
	public VMWriter(String fileName) {		
		try {
			File vmfile = new File(fileName + ".vm");
			if (vmfile.createNewFile()) {
			        System.out.println("File created: " + vmfile.getName());
			      } else {
			        System.out.println("File "+ "'" + vmfile + " already exists.");
			      }
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		 vmfileWriter = null;	
			try {
				vmfileWriter = new FileWriter(fileName + ".vm");
				System.out.println(5);
				
			}catch (IOException e) { 
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
	}
	
	
	public void writePush(String seg, int indx) throws IOException {
		if(seg.equals("var")) {
			seg = "local";
		}
		if(seg.equals("arg")) {
			seg = "argument";
		}
		if(seg.equals("field")) {
			seg = "this";
		}
		System.out.print("push " + seg + " " + indx + "\n");
		vmfileWriter.write("push " + seg + " " + indx + "\n");
	}
	
	public void writePop(String seg, int indx) throws IOException {
		if(seg.equals("var")) {
			seg = "local";
		}
		if(seg.equals("arg")) {
			seg = "argument";
		}
		if(seg.equals("field")) {
			seg = "this";
		}
		System.out.print("pop " + seg + " " + indx + "\n");
		vmfileWriter.write("pop " + seg + " " + indx + "\n");
	}
	
	public void writeArithmetic(String cmd) throws IOException {
		if(cmd.equals("*")) {
			writeCall("Math.multiply", 2);
		}
		else if(cmd.equals("/")) {
			writeCall("Math.divide", 2);
		}
		else if(cmd.equals("|")) {
			System.out.print("or\n");
			vmfileWriter.write("or\n");
		}
		else if(cmd.equals("-")) {
			System.out.print("sub\n");
			vmfileWriter.write("sub\n");
		}
		else if(cmd.equals("~")) {
			System.out.print("not\n");
			vmfileWriter.write("not\n");
		}
		else if(cmd.equals("+")) {
			System.out.print("add\n");
			vmfileWriter.write("add\n");
		}
		else if(cmd.equals("<")) {
			System.out.print("lt\n");
			vmfileWriter.write("lt\n");
		}
		else if(cmd.equals(">")) {
			System.out.print("gt\n");
			vmfileWriter.write("gt\n");
		}
		else if(cmd.equals("&")) {
			System.out.print("and\n");
			vmfileWriter.write("and\n");
		}
		else if(cmd.equals("=")) {
			System.out.print("eq\n");
			vmfileWriter.write("eq\n");
		}
		else if(cmd.equals("not")) { //stupid ik, but testing for valid inputs
			System.out.print("not\n");
			vmfileWriter.write("not\n");
		}
		else if(cmd.equals("neg")) {
			System.out.print("neg\n");
			vmfileWriter.write("neg\n");
		}
		else {
			System.out.print("INVALID ARITHMETIC: " + cmd + "\n");
		}	
	}
	
	public void writeLabel(String label) throws IOException {
		System.out.print("label " + label + "\n");
		vmfileWriter.write("label " + label + "\n");
	}
	
	public void writeGoTo(String label) throws IOException {
		System.out.print("goto " + label + "\n");
		vmfileWriter.write("goto " + label + "\n");
	}
	
	public void writeIf(String label) throws IOException {
		System.out.print("if-goto " + label + "\n");
		vmfileWriter.write("if-goto " + label + "\n");
	}
	
	public void writeCall(String name, int nArgs) throws IOException {
		System.out.print("call " + name + " " + nArgs + "\n");
		vmfileWriter.write("call " + name + " " + nArgs + "\n");
	}
	
	public void writeFunction(String name, int nLocals) throws IOException {
		System.out.print("function " + name + " " + nLocals + "\n");
		vmfileWriter.write("function " + name + " " + nLocals + "\n");
	}
	
	public void writeReturn() throws IOException {
		System.out.print("return\n");
		vmfileWriter.write("return\n");
	}
}
