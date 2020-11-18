package compilador;

import java.util.Hashtable;
import java.util.List;

public class GeneradorAssembler {

	String salida;
	Hashtable<String, Integer> registros = null;
	String regLibre= "";
	List<Terceto> tercetos = null;
	int numAux = 0;
	public GeneradorAssembler() {
		registros =  new Hashtable<String, Integer>();
		salida = "";
		registros.put("AX", 0);
		registros.put("BX", 0);
		registros.put("CX", 0);
		registros.put("DX", 0);
		
	}
	
	private void liberar(String reg) {
		int aux = registros.get(reg);
		salida += "MOV @aux" + numAux + ", " + reg + "\n";
		tercetos.get(aux-1).setReg("@aux"+numAux);
		numAux ++;
	}
	
	
	
	private void toAssByOpSR(Terceto t, String op) {

		if (t.getOp1().charAt(0)=='[') {
			int i = Integer.parseInt(t.getOp1().substring(t.getOp1().indexOf("[")+1,t.getOp1().indexOf("]")));
			if (t.getOp2().charAt(0)=='[') {
				int i2 = Integer.parseInt(t.getOp2().substring(t.getOp2().indexOf("[")+1,t.getOp2().indexOf("]")));
				salida += op + " "+ tercetos.get(i).getReg() + ", " + tercetos.get(i2).getReg() + "\n";
				registros.replace(tercetos.get(i2).getReg(), 0);
				t.setReg(tercetos.get(i).getReg());
			}else {
				if(Character.isDigit(t.getOp2().charAt(0)))
					salida += op + " " + tercetos.get(i).getReg() + ", " + t.getOp2() + "\n";
				else
					salida += op + " " + tercetos.get(i).getReg() + ", _" + t.getOp2() + "\n";
				t.setReg(tercetos.get(i).getReg());
			}
		}else {
			if (t.getOp2().charAt(0)=='[') {
				int i2 = Integer.parseInt(t.getOp2().substring(t.getOp2().indexOf("[")+1,t.getOp2().indexOf("]")));
				if(Character.isDigit(t.getOp1().charAt(0)))
					salida += op + " " + tercetos.get(i2).getReg() + ", " + t.getOp1() + "\n";
				else
					salida += op + " " + tercetos.get(i2).getReg() + ", _" + t.getOp1() + "\n";
				t.setReg(tercetos.get(i2).getReg());
			}else {
				String libre = this.getRegLibreOtros();
				if (libre!= null) { 
					salida += "MOV " + libre + ", _" + t.getOp1() + "\n";
					salida += op + " " + libre + ", _" + t.getOp2() + "\n";
					registros.replace(libre, t.getId());
					t.setReg(libre);
				}else {
					liberar("BX");
					if(Character.isDigit(t.getOp1().charAt(0)))
						salida += "MOV BX, " + t.getOp1() + "\n";
					else
						salida += "MOV BX, _" + t.getOp1() + "\n";
					
					if(Character.isDigit(t.getOp2().charAt(0)))
						salida += op +" BX, " + t.getOp2() + "\n";
					else
						salida += op +" BX, _" + t.getOp2() + "\n";
					registros.replace("BX", t.getId());
					t.setReg("BX");
				}
			}
		}
	
	}
	
	private void toAssByOpMD(Terceto t, String op) {

		if (t.getOp1().charAt(0)=='[') {
			int i = Integer.parseInt(t.getOp1().substring(t.getOp1().indexOf("[")+1,t.getOp1().indexOf("]")));
			if (t.getOp2().charAt(0)=='[') {
				int i2 = Integer.parseInt(t.getOp2().substring(t.getOp2().indexOf("[")+1,t.getOp2().indexOf("]")));
				salida += op + " "+ tercetos.get(i).getReg() + ", " + tercetos.get(i2).getReg() + "\n";
				registros.replace(tercetos.get(i2).getReg(), 0);
				t.setReg(tercetos.get(i).getReg());
			}else {
				if(Character.isDigit(t.getOp2().charAt(0)))
					salida += op + " " + tercetos.get(i).getReg() + ", " + t.getOp2() + "\n";
				else
					salida += op + " " + tercetos.get(i).getReg() + ", _" + t.getOp2() + "\n";
				t.setReg(tercetos.get(i).getReg());
			}
		}else {
			if (t.getOp2().charAt(0)=='[') {
				int i2 = Integer.parseInt(t.getOp2().substring(t.getOp2().indexOf("[")+1,t.getOp2().indexOf("]")));
				if(Character.isDigit(t.getOp1().charAt(0)))
					salida += op + " " + tercetos.get(i2).getReg() + ", " + t.getOp1() + "\n";
				else
					salida += op + " " + tercetos.get(i2).getReg() + ", _" + t.getOp1() + "\n";
				t.setReg(tercetos.get(i2).getReg());
			}else {
				String libre = this.getRegLibreMD();
				if (libre!= null) {
					if(Character.isDigit(t.getOp1().charAt(0)))
						salida += "MOV " + libre + ", " + t.getOp1() + "\n";
					else
						salida += "MOV " + libre + ", _" + t.getOp1() + "\n";
					if(Character.isDigit(t.getOp2().charAt(0)))
						salida += op + " " + libre + ", " + t.getOp2() + "\n";
					else
						salida += op + " " + libre + ", _" + t.getOp2() + "\n";
					registros.replace(libre, t.getId());
					t.setReg(libre);
				}else {
					liberar("BX");
					if(Character.isDigit(t.getOp1().charAt(0)))
						salida += "MOV AX, " + t.getOp1() + "\n";
					else
						salida += "MOV AX, _" + t.getOp1() + "\n";
					
					if(Character.isDigit(t.getOp2().charAt(0)))
						salida += op +" AX, " + t.getOp2() + "\n";
					else
						salida += "MOV AX, _" + t.getOp2() + "\n";
					registros.replace("AX", t.getId());
					t.setReg("AX");
				}
			}
		}
	
	}
	
	
	public void toAssembler(List<Terceto> terce) {
		tercetos = terce;
		for (Terceto t: tercetos) {
			switch (t.getOperador()) {
			case "+":
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpSR(t,"ADD");
				}else {
					
				}
				break;
			case "-":
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpSR(t,"SUB");
				}else {
					
				}
				break;
			case "*":
				System.out.println(t.getId()+ t.getTipo());
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpMD(t,"MUL");
				}else {
					
				}
				break;
			case "/":
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpMD(t,"DIV");
				}else {
					
				}
				break;
			case ":=":
				if (t.getTipo().equals("INTEGER")) {
					if (t.getOp2().charAt(0) == '[') {
						int i = Integer.parseInt(t.getOp2().substring(t.getOp2().indexOf("[")+1,t.getOp2().indexOf("]")));
						if(Character.isDigit(t.getOp1().charAt(0)))
							salida += "MOV " + t.getOp1() + ", " + tercetos.get(i).getReg() + "\n";
						else
							salida += "MOV _" + t.getOp1() + ", " + tercetos.get(i).getReg() + "\n";
					}else {
						String libre = getRegLibreOtros();
						if (libre != null) {
							if(Character.isDigit(t.getOp2().charAt(0)))
								salida += "MOV " + libre + ", " + t.getOp2() + "\n";
							else
								salida += "MOV " + libre + ", _" + t.getOp2() + "\n";
							
							if(Character.isDigit(t.getOp1().charAt(0)))
								salida += "MOV " + t.getOp1() + ", " + libre + "\n";
							else
								salida += "MOV _" + t.getOp1() + ", " + libre + "\n";
							registros.replace(libre, t.getId());
						}else {
							liberar("BX");
							if(Character.isDigit(t.getOp2().charAt(0)))
								salida += "MOV BX, " + t.getOp2() + "\n";
							else
								salida += "MOV BX, _" + t.getOp2() + "\n";
							
							if(Character.isDigit(t.getOp1().charAt(0)))
								salida += "MOV " + t.getOp1() + ", BX\n";
							else
								salida += "MOV BX, _" + t.getOp1() + "\n";
							registros.replace("BX", t.getId());
						}
					}
				}else {
					
				}
				break;
			case "O":
				
				break;
			case "C":
				
				break;
			default:
				break;
			}
		}
		System.out.println(salida);
	}
	
	
	private String getRegLibreMD() {
		if (registros.get("AX") == 0)
			return "AX";
		if (registros.get("DX") == 0)
			return "DX";
		return null;
	}
	
	private String getRegLibreOtros() {
		if (registros.get("BX") == 0)
			return "BX";
		if (registros.get("CX") == 0)
			return "CX";
		return null;
	}
	
}
