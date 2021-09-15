public class AInstruction {
	private String value;
	
	public AInstruction(String value) {
		this.value = value;
	}
	/*
	 * Given an AInstruction object, encodeAInstruction converts it into binary form
	*  If value in AInstruction is of decimal form, just binary form of that value is encoded
	*  if value is symbol already, then value recieved from symboltree, otherwise added to symboltree
	*  in form of variable
	*/
	public String encodeAInstruction() {
		try {
			int valueInt = Integer.parseInt(this.value);
			String valueBin = Integer.toBinaryString(valueInt);
			int digitsLeft = 16 - valueBin.length();
			String returnString = "";
			for(int i = 0; i< digitsLeft; i++) {
				returnString = returnString + "0";
			}
			return (returnString + valueBin); 
		} catch (NumberFormatException nfe) { //If A value not a string, look up in symbol table
			//System.out.println("Symbol detect, variable counter at " +  HackAssembler.variableCounter);
			if(HackAssembler.symbolTable.containsKey(this.value)){
				//System.out.println("Symbol found in SymbolTable Hashmap, treated as label");
				int valueInt = Integer.parseInt(HackAssembler.symbolTable.get(this.value));
				String valueBin = Integer.toBinaryString(valueInt);
				int digitsLeft = 16 - valueBin.length();
				String returnString = "";
				for(int i = 0; i< digitsLeft; i++) {
					returnString = returnString + "0";
				}
				
				return (returnString + valueBin); 
			}else {
				//System.out.println("Symbol '" + this.value + "' not found in SymbolTable Hashmap, treated as variable");
				int valueInt = HackAssembler.variableCounter;
				HackAssembler.symbolTable.put(this.value, Integer.toString(valueInt));
				HackAssembler.variableCounter++;
				String valueBin = Integer.toBinaryString(valueInt);
				int digitsLeft = 16 - valueBin.length();
				String returnString = "";
				for(int i = 0; i< digitsLeft; i++) {
					returnString = returnString + "0";
				}
				return (returnString + valueBin);
				
			}
		}

	}
		
	public String toString() {
		String msg = "A instruction: \n Value:" + value;
		return msg;
	}
	
	public static void main(String[] args) {
		AInstruction test = new AInstruction("1000");
		String encode = test.encodeAInstruction();
		System.out.println(encode);
		
	}
}
