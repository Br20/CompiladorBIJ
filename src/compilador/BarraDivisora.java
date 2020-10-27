package compilador;

public class BarraDivisora extends AccionSemantica {

	@Override
	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		//Se llama luego de una barra si no se inicia comentario, ya que representa el token de barra sola
		char c = '/';
		return (int)c;
	}

}
