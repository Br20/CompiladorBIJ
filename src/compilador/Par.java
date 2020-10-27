package compilador;

public class Par {
	private int token;
	private String lexema;
	
	public Par(String l, int t) {
		token = t;
		lexema = l;
	}
	
	public String getKey() {
		return this.lexema;
	}
	
	public int getValue() {
		return this.token;
	}
	
}
