import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompilationEngine {
	JackTokenizer tokens;
	FileWriter xmlfileWriter;
	VMWriter vmfileWriter;
	SymbolTable symTab;
	String className;
	String subroutineName;
	String subroutineType;
	int flowStateCount;
	int subroutineParameters;
	
	public CompilationEngine(String fileName) throws IOException {
		
		tokens = new JackTokenizer(fileName);
	    symTab = new SymbolTable(); // Creates symbolTable with empty class and subroutine hashtables
		String xmlFileName = fileName.split(".jack")[0];
		vmfileWriter = new VMWriter(xmlFileName);
		try {
			File xmlfile = new File(xmlFileName + ".xml");
			if (xmlfile.createNewFile()) {
			        System.out.println("File created: " + xmlfile.getName());
			      } else {
			        System.out.println("File "+ "'" + fileName + " already exists.");
			      }
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
		}	
		 xmlfileWriter = null;	
		try {
			xmlfileWriter = new FileWriter(xmlFileName + ".xml");
			xmlWrapper("class");
			
		}catch (IOException e) { 
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		finally{
			try {
				xmlfileWriter.close();
			}catch(IOException ioe) {
				System.out.println("Error in closing File Writer");
			}
		}
	}
	public void compileClass() throws IOException {
		
		//System.out.println("Looking at token value: " + tokens.currentToken.getValue());
		vertifier("class", null);
		className = tokens.currentToken.getValue();
		vertifier(null, "identifier");//class name
		vertifier("{", null);
		if(tokens.currentToken.getValue().equals("static") || tokens.currentToken.getValue().equals("field")) {//may or may not appear
			xmlWrapper("classVarDec");
			while(tokens.currentToken.getValue().equals("static") || tokens.currentToken.getValue().equals("field")) { //Run if more ClassVarDec
				xmlWrapper("classVarDec");
			}
		}
		if(tokens.currentToken.getValue().equals("constructor") || tokens.currentToken.getValue().equals("function") || 
				tokens.currentToken.getValue().equals("method")) {//may or may not appear
			
			xmlWrapper("subroutineDec");
			while(tokens.currentToken.getValue().equals("constructor") || tokens.currentToken.getValue().equals("function") ||
					tokens.currentToken.getValue().equals("method")) { //Run if more SubRoutineDec
				xmlWrapper("subroutineDec");
			}
		}
		vertifier("}", null);
		vmfileWriter.vmfileWriter.close();
		System.out.println("Compilation Complete");
		
	}
	public void compileClassVarDec() throws IOException { //Class variable declaration
		
		String name;
		String type;
		String kind;
		
		kind = tokens.currentToken.getValue();
		if(kind.equals("static")) {//Check if static or field
			vertifier("static", null);
		}
		else if(kind.equals("field")) {
			vertifier("field", null);
		}else {
			System.out.println("Error when looking at: " + tokens.currentToken.getValue() + " , supposed to match Value: field or static");
		}
		
		type = tokens.currentToken.getValue();
		compileType();
		
		name = tokens.currentToken.getValue();
		symTab.define(name, type, kind);
		vertifier(null, "identifier"); //varName
		if(tokens.currentToken.getValue().equals(",")) { //may or may not appear
			vertifier(",", null);
			name = tokens.currentToken.getValue();
			symTab.define(name, type, kind);
			vertifier(null, "identifier"); 
			while(tokens.currentToken.getValue().equals(",")) {//more than one
				vertifier(",", null);
				name = tokens.currentToken.getValue();
				symTab.define(name, type, kind);
				vertifier(null, "identifier"); 
			}
		}
		
		vertifier(";", null);
		
	}
	public void compileSubRoutineDec() throws IOException {
		
		String name;
		String type;
		String kind;
		flowStateCount = -1;
		
		symTab.startSubroutine();
		subroutineType = tokens.currentToken.getValue();
		
		if(tokens.currentToken.getValue().equals("constructor")) { //may or may not appear
			vertifier("constructor", null);
		}
		else if(tokens.currentToken.getValue().equals("function")) {
			vertifier("function", null);
		}
		else if(tokens.currentToken.getValue().equals("method")) {
			name = "this";
			type = className;
			kind = "arg";
			symTab.define(name, type, kind); //If method, define 'this' in subroutine symbol table
				
			vertifier("method", null);
		}else {
			System.out.println("Error when looking at Value: " + tokens.currentToken.getValue() + " , supposed to match Value: constructor, function, method"); //Assumes that each class should have at least one subroutine
		}
		
		if(tokens.currentToken.getValue().equals("void")) { //Return type
			vertifier("void", null);
		}else {
			compileType();
		}
		subroutineName = tokens.currentToken.getValue();
		
		vertifier(null, "identifier"); //subRoutineName
		vertifier("(", null);
		xmlfileWriter.write("<paramerList>\n");
		compileParameterList(); //updates subroutine symbol table


		xmlfileWriter.write("</paramerList>\n");
		vertifier(")", null);
		xmlWrapper("subroutineBody");
		
	}
	public void compileParameterList() throws IOException { //Defines parameters in subroutine symbol table
		String name;
		String type;
		String kind;
		
		subroutineParameters = 0;
		
		if(!(tokens.currentToken.getValue().equals(")"))) {
			subroutineParameters++;
			kind = "arg";
			type = tokens.currentToken.getValue();
			compileType();
			name = tokens.currentToken.getValue();
			symTab.define(name, type, kind);
			vertifier(null, "identifier"); //varName	
			if(tokens.currentToken.getValue().equals(",")) { //may or may not appear
				subroutineParameters++;
				vertifier(",", null);
				type = tokens.currentToken.getValue();
				compileType();
				name = tokens.currentToken.getValue();
				symTab.define(name, type, kind);
				vertifier(null, "identifier"); 
				while(tokens.currentToken.getValue().equals(",")) {//more than one
					subroutineParameters++;
					vertifier(",", null);
					type = tokens.currentToken.getValue();
					compileType();
					name = tokens.currentToken.getValue();
					symTab.define(name, type, kind);
					vertifier(null, "identifier"); 
				}
			}
		}
	}
	public void compileSubroutineBody() throws IOException {
		vertifier("{", null);
		if(tokens.currentToken.getValue().equals("var")) {
			xmlWrapper("varDec");
			while(tokens.currentToken.getValue().equals("var")) {
				xmlWrapper("varDec");
			}
		}
		vmfileWriter.writeFunction(className + "." + subroutineName, symTab.varCount("var"));
		if(subroutineType.equals("constructor")) { //if constructor, allocate memoryspace for parameters
			vmfileWriter.writePush("constant", symTab.varCount("field"));
			vmfileWriter.writeCall("Memory.alloc", 1);
			vmfileWriter.writePop("pointer", 0); //set base address of allocated space to 'this' object
		}
		if(subroutineType.equals("method")) { //if method, set 'this' subroutine object in arguement 0 to pointer 0 
			vmfileWriter.writePush("argument", 0);
			vmfileWriter.writePop("pointer", 0);
		}
		xmlWrapper("statements");
		vertifier("}", null);
	}
	public void compileVarDec() throws IOException {
		
		String name;
		String type;
		String kind;
		kind = tokens.currentToken.getValue();
		vertifier("var", null);
		
		type = tokens.currentToken.getValue();
		compileType();
		
		name = tokens.currentToken.getValue();
		symTab.define(name, type, kind);
		vertifier(null, "identifier"); //varName
		if(tokens.currentToken.getValue().equals(",")) { //may or may not appear
			vertifier(",", null);
			name = tokens.currentToken.getValue();
			symTab.define(name, type, kind);
			vertifier(null, "identifier"); 
			while(tokens.currentToken.getValue().equals(",")) {//more than one
				vertifier(",", null);
				name = tokens.currentToken.getValue();
				symTab.define(name, type, kind);
				vertifier(null, "identifier"); 
			}
		}
		vertifier(";", null);	
	}
	public void compileStatements() throws IOException {
		if(isStatement(tokens.currentToken.getValue())) {
			if(tokens.currentToken.getValue().equals("let")) {
				xmlWrapper("letStatement");
			}
			else if(tokens.currentToken.getValue().equals("if")) {
				xmlWrapper("ifStatement");
			}
			else if(tokens.currentToken.getValue().equals("while")) {
				xmlWrapper("whileStatement");
			}
			else if(tokens.currentToken.getValue().equals("do")) {
				xmlWrapper("doStatement");
			}
			else if(tokens.currentToken.getValue().equals("return")) {
				xmlWrapper("returnStatement");
			}
			while(isStatement(tokens.currentToken.getValue())) {
				if(tokens.currentToken.getValue().equals("let")) {
					xmlWrapper("letStatement");
				}
				else if(tokens.currentToken.getValue().equals("if")) {
					xmlWrapper("ifStatement");
				}
				else if(tokens.currentToken.getValue().equals("while")) {
					xmlWrapper("whileStatement");
				}
				else if(tokens.currentToken.getValue().equals("do")) {
					xmlWrapper("doStatement");
				}
				else if(tokens.currentToken.getValue().equals("return")) {
					xmlWrapper("returnStatement");
				}
				else {
					System.out.println("Error when looking at Value: " + tokens.currentToken.getValue() + " , supposed to match Value: let, if, while, do, or return");
				}
			}
		}
		else {
			System.out.println("Error when looking at Value: " + tokens.currentToken.getValue() + " , supposed to match Value: let, if, while, do, or return");
		}
			
	}
	public void compileLet() throws IOException {
		vertifier("let", null);
		String name = tokens.currentToken.getValue();
		vertifier(null, "identifier");
		
		String nameKind = symTab.kindOf(name);
		int nameNo = symTab.indexOf(name);
		
		if(tokens.currentToken.getValue().equals("[")) { //if left side array access, need special case to handle if right side also array access
			vmfileWriter.writePush(nameKind, nameNo);
			vertifier("[", null);
			
			xmlWrapper("expression");
			vertifier("]", null);
			vmfileWriter.writeArithmetic("+"); //Adds base array address to array index
			
			vertifier("=", null);
			xmlWrapper("expression"); // code to write right side of expression
			vertifier(";", null);
			
			vmfileWriter.writePop("temp", 0); //save val of right side of expression
			vmfileWriter.writePop("pointer", 1); //save val of left side of expression in 'this' pointer(save calculated address in pointer 1)
			vmfileWriter.writePush("temp", 0); // push val of right side of expression to stack
			vmfileWriter.writePop("that", 0); //save val of right side of expression to left side of expression(assignment)
			
		}
		else { // if left side not array access, just save expression value to left side of expression
			vertifier("=", null);
			xmlWrapper("expression");
			vertifier(";", null);
			
			vmfileWriter.writePop(nameKind, nameNo);
		}
		
		
	}
	public void compileIf() throws IOException {
		flowStateCount++;
		int currentflowStateCount = flowStateCount; //perserve value between nesting
		vertifier("if", null);
		vertifier("(", null);
		xmlWrapper("expression"); //compile expression
		//vmfileWriter.writeArithmetic("not");
		vmfileWriter.writeIf("IF_TRUE" + currentflowStateCount);
		vmfileWriter.writeGoTo("IF_FALSE" + currentflowStateCount);
		vertifier(")", null);
		vertifier("{", null);
		vmfileWriter.writeLabel("IF_TRUE" + currentflowStateCount);
		xmlWrapper("statements"); //compile statements
		//vmfileWriter.writeGoTo("IF_FALSE" + currentflowStateCount);
		vertifier("}", null);
		//vmfileWriter.writeLabel("IF_TRUE" + currentflowStateCount);
		if(tokens.currentToken.getValue().equals("else")) { //even if else doesnt exist, flow will work correctly
			vmfileWriter.writeGoTo("IF_END" + currentflowStateCount);
			vmfileWriter.writeLabel("IF_FALSE" + currentflowStateCount);
			vertifier("else", null);
			vertifier("{", null);
			xmlWrapper("statements");
			vertifier("}", null);
			vmfileWriter.writeLabel("IF_END" + currentflowStateCount);
			
		}else {
			vmfileWriter.writeLabel("IF_FALSE" + currentflowStateCount); 
		}
		
		
	}
	public void compileWhile() throws IOException {	
		flowStateCount++;
		int currentflowStateCount = flowStateCount; //perserve value between nesting
		vmfileWriter.writeLabel("WHILE_EXP" + currentflowStateCount);
		vertifier("while", null);
		vertifier("(", null);
		xmlWrapper("expression"); //compile expression
		vmfileWriter.writeArithmetic("not");
		vmfileWriter.writeIf("WHILE_END" + currentflowStateCount);
		vertifier(")", null);
		vertifier("{", null);
		xmlWrapper("statements"); //compile statements
		vmfileWriter.writeGoTo("WHILE_EXP" + currentflowStateCount);
		vertifier("}", null);
		vmfileWriter.writeLabel("WHILE_END" + currentflowStateCount); 
		
		
	}
    public void compileDo() throws IOException { //a void return let statment
    	vertifier("do", null);
    	String name = tokens.currentToken.getValue();
    	vertifier(null, "identifier"); //Calling it before due to special case with term
    	compileSubroutineCall(name); // will handle parameter pushing
    	vertifier(";", null);
    	vmfileWriter.writePop("temp", 0); //dump void value
	}
    public void compileReturn() throws IOException {
    	vertifier("return", null);
    	if(!(tokens.currentToken.getValue().equals(";"))){ // if exists a non-void return
    		xmlWrapper("expression");
    	}else { //return calls must return a value, push to return value to be dumped later
    		vmfileWriter.writePush("constant", 0);
    	}
    	vertifier(";", null);
    	vmfileWriter.writeReturn();
	}
    public void compileExpression() throws IOException {
    	xmlWrapper("term");
    	if(isOp(tokens.currentToken.getValue())) {
    		String symbolVal = tokens.currentToken.getValue();
    		vertifier(null, "symbol");   		
    		xmlWrapper("term");
    		vmfileWriter.writeArithmetic(symbolVal);
        	while(isOp(tokens.currentToken.getValue())) {
        		symbolVal = tokens.currentToken.getValue();
        		vertifier(null, "symbol");
        		xmlWrapper("term");
        		vmfileWriter.writeArithmetic(symbolVal);
        	}
    	}
    }
    public void compileTerm() throws IOException { //For expression assignment
    	if(tokens.currentToken.getType().equals("integerConstant")) {
    		vmfileWriter.writePush("constant", Integer.parseInt(tokens.currentToken.getValue())); // push constant integer
    		vertifier(null, "integerConstant");
    	}
    	else if(tokens.currentToken.getType().equals("stringConstant")) {
    		String constant = tokens.currentToken.getValue();
    		
    		vmfileWriter.writePush("constant", constant.length()); // push length of String as arguement to String instantiation
    		vmfileWriter.writeCall("String.new", 1); //Instantiate a String with a length parameter
    		
    		for(int i = 0; i< constant.length(); i++) {
    			vmfileWriter.writePush("constant", (int) constant.charAt(i)); // push constant of char value
    			vmfileWriter.writeCall("String.appendChar", 2); //call String.append to current String object and char(as unicode int)
    		}
    		
    		vertifier(null, "stringConstant");
    	}
    	else if(isKeywordConstant(tokens.currentToken.getValue())){
    		String keyWord = tokens.currentToken.getValue();
    		if(keyWord.equals("true")) {
    			vmfileWriter.writePush("constant", 0); // push 1 integer
    			vmfileWriter.writeArithmetic("not"); // make 1 into -1 for true val
    		}
    		else if(keyWord.equals("false") || keyWord.equals("null")) {
    			vmfileWriter.writePush("constant", 0); // push 0 integer
    		}
    		else if(keyWord.equals("this")) {
    			vmfileWriter.writePush("pointer", 0); // pushes the pointer to 'this' object
    		}
    		vertifier(null, "keyword");
    	}
    	else if(tokens.currentToken.getType().equals("identifier")) { // either a varName, varName[expression], or subroutineCall
    		Token currentToken = tokens.currentToken;
    		String name = tokens.currentToken.getValue();
    		tokens.advance();//look ahead
    		Token nextToken = tokens.currentToken;
    		if(!(nextToken.getValue().equals("(") || nextToken.getValue().equals("."))) { //varName[expression]
        		
        		String type = symTab.typeOf(currentToken.getValue());
        		String kind = symTab.kindOf(currentToken.getValue());
        		int index = symTab.indexOf(currentToken.getValue());
        		
        		if(kind.equals("var")) { //Vm mapping of variables
        			vmfileWriter.writePush("local", index);
        		}
        		else if(kind.equals("field")) {
        			vmfileWriter.writePush("this", index); //assumption: pointer 0 set to 'this' object
        		}
        		else {
        			vmfileWriter.writePush(kind, index); //arguement and static types
        		}
    		}
    		
    		tokens.currentToken = currentToken; //Revert look-ahead
    		tokens.currentIndex--;
    		
    		vertifier(null, "identifier");//If only varName
    		if(tokens.currentToken.getValue().equals("[")) {//If only varName[expression]
    			vertifier("[", null);
    			xmlWrapper("expression");
    			vertifier("]", null);
    			
    			vmfileWriter.writeArithmetic("+"); //adds array name pointer and expression within []
    			vmfileWriter.writePop("pointer", 1); //set as new address for 'that' array pointer
    			vmfileWriter.writePush("that", 0);  //save val of identifier[expression] in stack
    			
    		}
    		else if(!(tokens.currentToken.getValue().equals(";"))){//If call to subroutine
    			compileSubroutineCall(name); //name is name of subroutine before name() or name.secondName()		
    		}
    		
    	}
        else if(tokens.currentToken.getValue().equals("(")){
        	vertifier("(", null);
        	xmlWrapper("expression");
			vertifier(")", null);
    	}
        else if(isUnaryOp(tokens.currentToken.getValue())){
        	String unaryOp = tokens.currentToken.getValue();
        	
        	vertifier(null, "symbol");
        	xmlWrapper("term");
        	if(unaryOp.equals("-")) { //Set after pushing value for single operative
        		vmfileWriter.writeArithmetic("neg"); 
        	}else {
        		vmfileWriter.writeArithmetic("not"); 
        	}
    	}else {
    		System.out.println("Error when looking at Value: " + tokens.currentToken.getValue() + " , supposed to match Term values");
        }
    		
    	
    }
    public void compileSubroutineCall(String routineName) throws IOException{
    	if(tokens.currentToken.getValue().equals("(")) { //subroutineName(expressionList), a method
			vertifier("(", null);
			vmfileWriter.writePush("pointer", 0); 
			xmlfileWriter.write("<expressionList>\n");
			int expNo = compileExpressionList();
			xmlfileWriter.write("</expressionList>\n");
			vertifier(")", null);
			expNo++;
			vmfileWriter.writeCall(className + "." + routineName, expNo); //method call
		}
		else if(tokens.currentToken.getValue().equals(".")) { //(className|varName).subroutineName(expressionList), if classname constructor/function, if varname then its method
			vertifier(".", null);
			String methodName = tokens.currentToken.getValue(); //While it is method name, should be type of oject if . afterwards
			String objectType = symTab.typeOf(routineName);
			String objectKind = symTab.kindOf(routineName); //static, field, arg, or var
			if(objectType == null) {
				methodName = routineName + "." + methodName; //A function call/constructor for a class
			}else {
				methodName = objectType + "." + methodName;//A method call for an instantiated class name, pushes 'this' object as parameter
				vmfileWriter.writePush(objectKind, symTab.indexOf(routineName)); 
			}
				
			vertifier(null, "identifier");//subRoutine Name
			vertifier("(", null);
			xmlfileWriter.write("<expressionList>\n");
			int expNo = compileExpressionList();
			xmlfileWriter.write("</expressionList>\n");
			vertifier(")", null);
			if(!(objectKind == null)) {
				expNo++; //For count of object arguement
			}
			vmfileWriter.writeCall(methodName, expNo);
		}
    }
    public int compileExpressionList() throws IOException{
    	int returnInt = 0;
    	if(!(tokens.currentToken.getValue().equals(")"))) {
    		xmlWrapper("expression");
    		returnInt++;
    		if(tokens.currentToken.getValue().equals(",")) {
    			vertifier(",", null);
    			xmlWrapper("expression");
    			returnInt++;
    			while(tokens.currentToken.getValue().equals(",")) {
    				vertifier(",", null);
    				xmlWrapper("expression");
    				returnInt++;
    			}
    		}
    	}
		return returnInt;
    }
    
    public void compileType() throws IOException {
    	String value = tokens.currentToken.getValue();
    	String type = tokens.currentToken.getType();
    	if(value.equals("int")) {//Check type declaration
			vertifier("int", null);
		}
		else if(value.equals("char")) {
			vertifier("char", null);
		}
		else if(value.equals("boolean")) {
			vertifier("boolean", null);
		}
		else if(type.equals("identifier")) {
			vertifier(null, "identifier");
		}else {
			System.out.println("Error when looking at Value: " + tokens.currentToken.getValue() + " , supposed to match Value: int,char,boolean or Type: identifier ");
		}
    }
    
    private void vertifier(String nameString, String typeString) throws IOException {
    	String tokenVal = tokens.currentToken.getValue();
    	String tokenValStr = tokens.currentToken.toString();
    	String tokenType =tokens.currentToken.getType();
    	if(tokenVal.equals("<")) {
			tokenValStr = "<" + tokenType + "> &lt; </" + tokenType + ">";
		}
		if(tokenVal.equals(">")) {
			tokenValStr = "<" + tokenType + "> &gt; </" + tokenType + ">";
		}
		if(tokenVal.equals("\"")) {
			tokenValStr = "<" + tokenType + "> &quote; </" + tokenType + ">";
		}
		if(tokenVal.equals("&")) {
			tokenValStr = "<" + tokenType + "> &amp; </" + tokenType + ">";
		}
		if(nameString == null) {
			if(tokens.currentToken.getType().equals(typeString)) {
	    		xmlfileWriter.write(tokenValStr + "\n");
	    		tokens.advance();
	    	}else {
	    		System.out.println("Error when looking at: " + tokens.currentToken.getValue() + " , supposed to match Type: " + typeString);
	    	}
		} else {
			if(tokenVal.equals(nameString)) {
	    		xmlfileWriter.write(tokenValStr + "\n");
	    		tokens.advance();
	    	}else {
	    		System.out.println("Error when looking at: " + tokens.currentToken.getValue() + " , supposed to match Value: " + nameString);
	    	}
		}
    }
    
    private boolean isStatement(String s) {
    	if(s.equals("let") || s.equals("if") || s.equals("while") || s.equals("do") ||
    			s.equals("return")) {
    		return true;
    	}
    	return false;
    }
    
    private boolean isOp(String s){
    	if(s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") ||
    			s.equals("&") || s.equals("|") || s.equals("<") || s.equals(">") || s.equals("=")) {
    		return true;
    	}
    	return false;
    }
    
    private boolean isUnaryOp(String s){
    	if(s.equals("-") || s.equals("~")) {
    		return true;
    	}
    	return false;
    }
    
    private boolean isKeywordConstant(String s){
    	if(s.equals("true") || s.equals("false") || s.equals("null") || s.equals("this")) {
    		return true;
    	}
    	return false;
    }  
    
    public void xmlWrapper(String wrapName) throws IOException {
    	
    	xmlfileWriter.write("<" + wrapName + ">\n");
    	
    	if(wrapName.equals("class")) {
    		compileClass();
    	}
    	else if(wrapName.equals("classVarDec")) {
    		compileClassVarDec();
    	}
    	else if(wrapName.equals("subroutineDec")) {
    		compileSubRoutineDec();
    	}
    	else if(wrapName.equals("subroutineBody")) {
    		compileSubroutineBody();
    	}
    	else if(wrapName.equals("varDec")) {
    		compileVarDec();
    	}
    	else if(wrapName.equals("statements")) {
    		compileStatements();
    	}
    	else if(wrapName.equals("letStatement")) {
    		compileLet();
    	}
    	else if(wrapName.equals("ifStatement")) {
    		compileIf();
    	}
    	else if(wrapName.equals("whileStatement")) {
    		compileWhile();
    	}
    	else if(wrapName.equals("doStatement")) {
    		compileDo();
    	}
    	else if(wrapName.equals("returnStatement")) {
    		compileReturn();
    	}
    	else if(wrapName.equals("expression")) {
    		compileExpression();
    	}
    	else if(wrapName.equals("term")) {
    		compileTerm();
    	}
    	//else if(wrapName.equals("expressionList")) {
    	//	compileExpressionList();
    	//}
    	
    	xmlfileWriter.write("</" + wrapName + ">\n");
    }
}
