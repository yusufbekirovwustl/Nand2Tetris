
interface ParserSeperate {
	
}
public class ParserSeperator implements ParserSeperate {
	private static String contentLine = null;
	
	public static Parser.VmCommand seperator(String currentline) {
		contentLine = currentline.replaceAll("\\s", ";");
		
		Parser.VmCommand vmReturn = new Parser.VmCommand(contentLine);
		//System.out.println(contentLine);
		String[] value = contentLine.split("[;\\/]");
		 //System.out.println("Contents:  " + contentLine);
		 if((contentLine.startsWith(" ", 0) || contentLine.startsWith(";", 0) || contentLine.contentEquals("") 
				 || contentLine.startsWith("//", 0) || contentLine.startsWith("("))) {
			return null; //Comment or whitespace detected	
		 }
		 else if(contentLine.startsWith("add") || contentLine.startsWith("neg") || contentLine.startsWith("gt")
				 || contentLine.startsWith("and") || contentLine.startsWith("not") || contentLine.startsWith("sub")
				 || contentLine.startsWith("eq") || contentLine.startsWith("lt") || contentLine.startsWith("or")) {
			 vmReturn.setCommandtype("C_ARIMETIC");
		 }
		 else if(contentLine.startsWith("if-goto")) {
			 vmReturn.setCommandtype("C_IF");
			 vmReturn.setArg1(value[1]);
		 }
		 else if(contentLine.startsWith("goto")) {
			 vmReturn.setCommandtype("C_GOTO");
			 vmReturn.setArg1(value[1]);
		 }
		 else if(contentLine.startsWith("label")) {
			 vmReturn.setCommandtype("C_LABEL");
			 vmReturn.setArg1(value[1]);
		 }
		 else if(contentLine.startsWith("function")) {
			 vmReturn.setCommandtype("C_FUNCTION");
			 vmReturn.setArg1(value[1]);
			 int val2 = Integer.parseInt(value[2]);
			 vmReturn.setArg2(val2);
		 }
		 else if(contentLine.startsWith("call")) {
			 vmReturn.setCommandtype("C_CALL");
			 vmReturn.setArg1(value[1]);
			 int val2 = Integer.parseInt(value[2]);
			 vmReturn.setArg2(val2);
		 }
		 else if(contentLine.startsWith("return")) {
			 vmReturn.setCommandtype("C_RETURN");
		 }
		 else if(contentLine.startsWith("pop")) {
			 vmReturn.setCommandtype("C_POP");
			 vmReturn.setArg1(value[1]);
			 int val2 = Integer.parseInt(value[2]);
			 vmReturn.setArg2(val2);
		 }
		 else if(contentLine.startsWith("push")) {
			 //System.out.println("Push Detect");
			 vmReturn.setCommandtype("C_PUSH");
			 vmReturn.setArg1(value[1]);
			 int val2 = Integer.parseInt(value[2]);
			 //System.out.println("Value of push: " + val2);
			 vmReturn.setArg2(val2);
		 }	 
		//System.out.println(vmReturn.commandtype + vmReturn.arg1 + vmReturn.arg2); 
		return vmReturn;
	}

}
