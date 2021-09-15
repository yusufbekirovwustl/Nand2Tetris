
public class Token {
	private String tokenType;
	private String tokenValue;
	
	public Token(String tokenType, String tokenValue) {
		this.tokenType = tokenType;
		this.tokenValue = tokenValue;
	}
	
	public String toString() {
		return ("<" + tokenType + "> " + tokenValue + " </" + tokenType + ">");
	}
	
	public String getType(){
		return this.tokenType;
	}
	
	public String getValue(){
		return this.tokenValue;
	}
}
