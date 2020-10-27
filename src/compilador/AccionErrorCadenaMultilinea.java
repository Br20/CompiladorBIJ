
package compilador;

public class AccionErrorCadenaMultilinea extends AccionSemantica {
	
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		
		pos[0]--; //se recupera el caracter que representa el salto de linea para que luego se llame a accion semantica new line 
		Parser.escribirError("Error lexico cadena multilinea o no cierra cadena antes de fin de programa, cerca de linea:" + Parser.nLinea);
		Lexico.erroresL++;
		lex[0] = true;
		return Parser.CADENA;
	}

}
