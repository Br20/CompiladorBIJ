package compilador;

public class VerificaKeyW extends AccionSemantica{

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		pos[0]--; //mantiene caracter actual
		lex[0]=true;
		switch(buffer.toString()) {
			case "IF":
				return Parser.IF;
			case "END_IF":
				return Parser.END_IF;
			case "ELSE":
				return Parser.ELSE;
			case "THEN":
				return Parser.THEN;
			case "OUT":
				return Parser.OUT;
			case "FUNC":
				return Parser.FUNC;
			case "RETURN":
				return Parser.RETURN;
			case "INTEGER":
				return Parser.INTEGER;
			case "FLOAT":
				return Parser.FLOAT;	
			case "WHILE":
				return Parser.WHILE;
			case "LOOP":
				return Parser.LOOP;
			case "NA":
				return Parser.NA;
			case "TRUE":
				return Parser.TRUE;
			case "FALSE":
				return Parser.FALSE;
			case "SHADOWING":
				return Parser.SHADOWING;
			case "PROC":
				return Parser.PROC;
			default:
				break;
		}
		lex[0]=false;
		Parser.escribirError("Error lexico, no se reconoce palabra reservada cerca de linea: " + Parser.nLinea);
		Lexico.erroresL++;
		return -1;
	}
	
	

}
