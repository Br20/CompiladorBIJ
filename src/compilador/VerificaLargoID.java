package compilador;

public class VerificaLargoID extends AccionSemantica {

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos,  boolean[] lex) {
		// TODO Auto-generated method stub
		pos[0]--; //se guarda el ultimo caracter
		if (buffer.length() > 20) {
			//Si se excede el limite de los identificadores, se avisa y luego se corta al largo total
			Lexico.erroresL++;
			Parser.escribirError("Error lexico: identificador excede limite de largo, cerca de linea:"  + Parser.nLinea);
			buffer.delete(20,buffer.length());
		}
		lex[0] = true;
		//System.out.println(lex);
		return Parser.ID;
	}

}
