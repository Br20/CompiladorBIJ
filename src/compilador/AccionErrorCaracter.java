package compilador;

public class AccionErrorCaracter extends AccionSemantica {
	
	public int accionar (StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		
		if (buffer.charAt(0)=='.'){ 
			pos[0]--; //se recupera el char actual si es punto ya que puede ser que se haya querido pasar un float, pero igual no se entrega el token de float
		}
		Parser.escribirError("Error lexico caracter extra, cerca de linea:" + Parser.nLinea);
		Lexico.erroresL++;
		return -1;
	}

}
