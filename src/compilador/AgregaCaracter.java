package compilador;

public class AgregaCaracter extends AccionSemantica{

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		
		buffer.append(actual);
		return -1;
	}

}
