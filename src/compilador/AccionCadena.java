package compilador;

public class AccionCadena extends AccionSemantica{

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		lex[0] = true;
		return Parser.CADENA;
	}
	
}
