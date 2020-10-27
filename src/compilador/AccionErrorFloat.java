package compilador;

public class AccionErrorFloat extends AccionSemantica {
	
	public int accionar (StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		//si no se completa correctamente un float se recupera el caracter actual para la generacion del proximo token, y se acomoda el 
		//valor para pasar un token float y que se pueda seguir con la compilacion
		pos[0]--; 
		int posF = buffer.indexOf("f");
		String flotante = (buffer.substring(0, posF));
		Parser.escribirError("Error lexico flotante mal escrito, cerca de linea:" + Parser.nLinea);
		Lexico.erroresL++;
		lex[0] = true;
		buffer.delete(0, buffer.length());
		buffer.append(flotante);
		return Parser.CTE_FLOAT;
	}

}