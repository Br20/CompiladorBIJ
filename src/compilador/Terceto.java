package compilador;

import java.util.Hashtable;

public class Terceto {
	public static int id = 0;
	private int numTerceto; 
	private String op1;
	private String op2;
	private String operador;
	private String tipo = null;
	private String reg = null;
	
	public Terceto(String op1, String op2, String operador, String tipo) {
		numTerceto = id;
		id++;
		this.op1 = op1; 
		this.op2 = op2;
		this.operador = operador;
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
	
	public String getOperador() {
		return operador;
	}
	
	public void setOperador(String operador) {
		this.operador = operador;
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
		if (op1 == null)
			aux1 = " ";
		else if(op1.indexOf(".p") == -1)
			aux1 = op1;
		else	
			aux1 = op1.substring(0,op1.indexOf("."));
		if (op2 == null)
			aux2 = "";
		else if(op2.indexOf(".p") == -1)
			aux2 = op2;
		else	
			aux2 = op2.substring(0,op2.indexOf("."));
		return this.numTerceto + " ( " + this.operador + " , " + aux1 + " , " + aux2 + " ) ";
	}
	


	public String getReg() {
		return this.reg;
	}

	
	public void setReg(String reg) {
		this.reg = reg;
	}
}
