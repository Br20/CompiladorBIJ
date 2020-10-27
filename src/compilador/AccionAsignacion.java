package compilador;

public class AccionAsignacion extends AccionSemantica {

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		if (actual == '=')
			return Parser.ASSIGN;
		Parser.escribirError("Error lexico no se completa asignacion");
		Lexico.erroresL++;
		return Parser.ASSIGN;  //Se informa del error pero de igual forma se entrega el token de asignacion para que se siga compilando
	}

	
}
