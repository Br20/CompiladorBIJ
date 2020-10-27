package compilador;

public class AccionErrorComentario extends AccionSemantica{

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		Parser.escribirError("Error lexico comentario no cerrado antes de fin de programa, cerca de linea:" + Parser.nLinea); //se informa que no se cerro comentario
		Lexico.erroresL++;
		return 0;
	}

}
