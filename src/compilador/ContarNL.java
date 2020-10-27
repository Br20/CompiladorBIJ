package compilador;

public class ContarNL extends AccionSemantica{

	public int accionar(StringBuffer buffer, char actual, int[] pos, boolean[] lex) {
		// TODO Auto-generated method stub
		//Se llama con los saltos de lineas y con los retornos de carro, si es salto de linea se incrementa en uno
		if (actual == '\n') {
			Parser.nLinea++;
		}
		return -1;
	}
}
