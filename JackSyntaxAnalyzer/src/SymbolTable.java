import java.util.HashMap;

public class SymbolTable {
	
	int indexStatic;
	int indexField;
	int indexArg;
	int indexVar;
	
	HashMap<String, SymbolTableProp> classMap;
	HashMap<String, SymbolTableProp> subroutineMap;
	
	public SymbolTable() {
		
		classMap = new HashMap<String, SymbolTableProp>();
		subroutineMap = new HashMap<String, SymbolTableProp>();
		
		indexStatic = 0;
		indexField = 0;
		indexArg = 0;
		indexVar = 0;
	
	}
	
	public void startSubroutine() {
		subroutineMap.clear();
		indexArg = 0;
		indexVar = 0;
	}
	
	public void define(String name, String type, String kind) {
		
		if(kind.equals("static")) {
			SymbolTableProp staticProp = new SymbolTableProp(name, type, kind, indexStatic);
			classMap.put(name, staticProp);
			indexStatic++;
			
		}
		else if(kind.equals("field")) {
			SymbolTableProp fieldProp = new SymbolTableProp(name, type, kind, indexField);
			classMap.put(name, fieldProp);
			indexField++;
		}
		else if(kind.equals("arg")) {
			SymbolTableProp argProp = new SymbolTableProp(name, type, kind, indexArg);
			subroutineMap.put(name, argProp);
			indexArg++;
		}
		else if(kind.equals("var")) {
			SymbolTableProp varProp = new SymbolTableProp(name, type, kind, indexVar);
			subroutineMap.put(name, varProp);
			indexVar++;
		}
	}
	
	public int varCount(String type) {
		if(type.equals("static")) {
			return indexStatic;			
		}
		else if(type.equals("field")) {
			return indexField;
		}
		else if(type.equals("arg")) {
			return indexArg;
		}
		else if(type.equals("var")) {
			return indexVar;
		}
		return 0;
	}
	
	public String kindOf(String name) {
		SymbolTableProp sub = subroutineMap.get(name);
		SymbolTableProp clas = classMap.get(name);
		if(sub == null && clas == null) {
			return null;
		}
		if(sub == null) {
			return clas.kind;
		}
		else if(clas == null) {
			return sub.kind;
		}
		return null;
		
	}
	
	public String typeOf(String name) {
		SymbolTableProp sub = subroutineMap.get(name);
		SymbolTableProp clas = classMap.get(name);
		if(sub == null && clas == null) {
			return null;
		}
		if(sub == null) {
			return clas.type;
		}
		else if(clas == null) {
			return sub.type;
		}
		return null;
		
	}
	
	public int indexOf(String name) {
		SymbolTableProp sub = subroutineMap.get(name);
		SymbolTableProp clas = classMap.get(name);
		if(sub == null && clas == null) {
			return 0;
		}
		if(sub == null) {
			return clas.no;
		}
		else if(clas == null) {
			return sub.no;
		}
		return 0;
		
	}
}
