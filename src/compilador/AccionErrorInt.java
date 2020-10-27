package compilador;

public class AccionErrorInt extends AccionSemantica{

	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex)
	{
		//si no se completa correctamente un int con _ se recupera el caracter actual para la generacion del proximo token, y se acomoda el 
		//valor para pasar un token int y que se pueda seguir con la compilacion
		pos[0]--;
		Parser.escribirError("Error lexico integer mal escrito, cerca de linea:" + Parser.nLinea);
		Lexico.erroresL++;
		lex[0] = true;
		return Parser.CTE_INT;
	}
	
}