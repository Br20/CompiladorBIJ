package compilador;

public class VerificaRangoFloat extends AccionSemantica{

	private Double max = 3.40282347*Math.pow(10, 38);
	private Double min = 1.17549435*Math.pow(10, -38);
	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		pos[0]--;
		lex[0]=true;
		int posF = buffer.indexOf("f");
		double aux;
		if (posF > -1) {
			String exponente = (buffer.substring(posF+1, buffer.length()));
			String flotante = (buffer.substring(0, posF));
			aux = Double.parseDouble(flotante);
			int exp = Integer.parseInt(exponente);
			aux = aux*Math.pow(10, exp);
		}
		else {
			aux = Double.parseDouble(buffer.toString());
		}
		if ((aux < max) && (aux > min)) { // || (aux > Math.pow(-3.40282347, 38) && (aux < Math.pow(-1.17549435, -38)))) {
			//Si cumple, esta en rango y se devuelve el token con su lexema
			buffer.delete(0, buffer.length());
			buffer.append(Double.toString(aux));
			return Parser.CTE_FLOAT;
		}
		//si no cumple, se alerta pero de igual forma se devuelve un token de float con un lexema por defecto para seguir procesando el codigo
		Parser.escribirError("Error lexico constante flotante fuera de rango cerca de linea: "  + Parser.nLinea);
		Lexico.erroresL++;
		buffer.delete(0, buffer.length());
		buffer.append("3.40282347");
		//System.out.println(buffer);
		return Parser.CTE_FLOAT;
	}

}
