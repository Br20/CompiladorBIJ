package compilador;

public class AccionInicializaBuffer extends AccionSemantica {

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		buffer.delete(0,buffer.length());
		if (actual != '\'')
			buffer.append(actual);
		return -1;
	}

}
