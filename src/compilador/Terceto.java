package compilador;

import java.util.Hashtable;

public class Terceto {
	public static int id = 0;
	private int numTerceto; 
	private String op1;
	private String op2;
	private String operando;
	private String tipo = null;
	
	public Terceto(String op1, String op2, String operando, String tipo) {
		numTerceto = id;
		id++;
		this.op1 = op1; 
		this.op2 = op2;
		this.operando = operando;
	}
	
	
	public String getOp1() {
		return op1;
	}
	
	public void setOp1(String op1) {
		this.op1 = op1;
	}
	
	public String getOp2() {
		return op2;
	}
	
	public void setOp2(String op2) {
		this.op2 = op2;
	}
	
	public String getOperando() {
		return operando;
	}
	
	public void setOperando(String operando) {
		this.operando = operando;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getId() {
		return this.numTerceto;
	}
	
	public String toString() {
		String aux1;
		String aux2;
		if (op1 == null || op1.indexOf(".") == -1)
			aux1 = op1;
		else
			aux1 = op1.substring(0,op1.indexOf("."));
		if (op2 == null || op2.indexOf(".") == -1)
			aux2 = op2;
		else
			aux2 = op2.substring(0,op2.indexOf("."));
		return this.numTerceto + " ( " + this.operando + " , " + aux1 + " , " + aux2 + " )";
	}
}
