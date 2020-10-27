package compilador;

public class VerificaRangoInt extends AccionSemantica{

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		//System.out.println("Buffer antes de verificar rango int: " + buffer);
		int aux = Integer.parseInt(buffer.toString());
		if (aux > 32768) {
			//si no cumple, se alerta pero de igual forma se devuelve un token de int con un lexema por defecto para seguir procesando el codigo
			Parser.escribirError("Error lexico constante entera fuera de rango cerca de linea: " + Parser.nLinea);
			Lexico.erroresL++;
			buffer.delete(0, buffer.length());
			buffer.append("32768");
		}
		lex[0] = true;
		return Parser.CTE_INT;
	}

}
