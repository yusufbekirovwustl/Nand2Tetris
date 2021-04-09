import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CInstruction {
	private String dest;
	private String comp;
	private String jump;
	
	public CInstruction(String dest, String comp, String jump) {
		this.dest = dest;
		this.comp = comp;
		this.jump = jump;
	}
	
    //translateCInstruction, when given a particular CInstruction sub-type,
	//finds and returns the equivalent binary form
    private String translateCInstruction(String type) {
    	
    	String thisType = null;
    	if (type == "dest") {
    		thisType = this.dest;
    	}
    	else if(type == "comp"){
    		thisType = this.comp;
    	}
    	else if(type == "jump"){
    		thisType = this.jump;
    	}
    	else {
    		System.out.println("Type does not Exist in C instruction!");
    	}
    	//System.out.println("This type value:" + thisType);
    	BufferedReader instTypeReader = null;
		try {
			instTypeReader = new BufferedReader(new FileReader(type + "Encode"));
			if(type.equals("dest")) {
				//Bad form, used to reformat to fit textfile format, better to place priority values 
				//to calculate binary value
				if((thisType.contains("M")) && (thisType.contains("D")) && !(thisType.contains("A"))) {
					thisType = "MD";
				}
				else if((thisType.contains("M")) && !(thisType.contains("D")) && (thisType.contains("A"))) {
					thisType = "AM";
				}
				else if(!(thisType.contains("M")) && (thisType.contains("D")) && (thisType.contains("A"))) {
					thisType = "AD";
				}
				else if((thisType.contains("M")) && (thisType.contains("D")) && (thisType.contains("A"))) {
					thisType = "AMD";
				}
			}
			String contentLine = instTypeReader.readLine();
			  while (contentLine != null) {
			     //System.out.println(contentLine);
			     String[] contentParts = contentLine.split(",");
			     //System.out.println(contentParts[0] +"\n"+ contentParts[1]);
			     if(contentParts[0].equals(thisType)) {
			    	 return contentParts[1];
			     }
			     contentLine = instTypeReader.readLine();
			  }
			  System.out.println("Type in C instruction was not found, invalid type value for "+ type + "of value" + thisType);
		}
		
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		finally {
			try {
				if(instTypeReader != null) {
					instTypeReader.close();
				}
			}
			catch(IOException ioe) {
				System.out.println("Error in closing Buffered Reader");
			}
		}
		return null;
    }
  //Given an CInstruction object, encodeCInstruction converts it into binary form
    public String encodeCInstruction() {
    	String destEncode = translateCInstruction("dest");
    	String compEncode = translateCInstruction("comp");
    	String jumpEncode = translateCInstruction("jump");
    	if(destEncode == null || compEncode == null || jumpEncode == null) {
    		System.out.println("One of the types in C instruction incorrectly encoded, returning string"
    				+ "0");
    		return "0";
    	}
    	return ("111" + compEncode + destEncode + jumpEncode);
    }

	public String toString() {
		String msg = "C instruction: \n Dest:" + dest + "\n Comp:" + comp + "\n Jump:" + jump;
		return msg;
	}
	
	public static void main(String[] args) {
		CInstruction test = new CInstruction("D", "0", "null");
		String encode = test.encodeCInstruction();
		System.out.println(encode);		
	}
}
