package compilador;

public class VerificarComparador extends AccionSemantica{

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		if (actual == '=') {
			buffer.append(actual);
			if (buffer.toString().equals("<="))
				return Parser.COMP_MENOR_IGUAL;
			if (buffer.toString().equals("=="))
				return Parser.COMP_IGUAL;
			if (buffer.toString().equals("!="))
				return Parser.DISTINTO;
			return Parser.COMP_MAYOR_IGUAL;
		}
		//si no se completa uno de los comparadores con el simbolo igual, se guarda el caracter
		pos[0]--;
		return (int)buffer.charAt(0);	
	}

}
