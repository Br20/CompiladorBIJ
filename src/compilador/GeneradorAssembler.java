package compilador;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class GeneradorAssembler {

	private String salida;
	private Hashtable<String, Integer> registros = null;
	//private String regLibre= "";
	private List<Terceto> tercetos = null;
	private int numAux = 0;
	private int nLabel = 0;
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
	
	public void toAssFloatByOp(Terceto t) {
		String opera1 = "_" + t.getOp1();
		String opera2 = "_" + t.getOp2();
		int i = 0;
		if (t.getOp1().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1));
			opera1 = tercetos.get(i).getReg();
		}
		if (t.getOp2().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1));
			opera2 = tercetos.get(i).getReg();
		}
		if (t.getToConv() == 0) {
			switch (t.getOperador()) {
			case "+":
				salida += "FLD " + opera2 + "\n";
				salida += "FADD " + opera1 + "\n";
				break;
			case "-":
				salida += "FLD " + opera2 + "\n";
				salida += "FSUB " + opera1 + "\n";
				break;
			case "*":
				salida += "FLD " + opera2 + "\n";
				salida += "FMUL " + opera1 + "\n";
				break;
			case "/":
				salida += "FLD " + opera2 + "\n";
				salida += "FDIV " + opera1 + "\n";
				break;
			case ":=":
				salida += "FLD " + opera2 + "\n";
				salida += "FST " + opera1 + "\n";
				break;
			default:
				break;
			}
		} else {
			if (t.getToConv() == 1) {
				switch (t.getOperador()) {
				case "+":
					salida += "FLD " + opera2 + "\n";
					salida += "FIADD " + opera1 + "\n";
					break;
				case "-":
					salida += "FLD " + opera2 + "\n";
					salida += "FISUBR " + opera1 + "\n";
					break;
				case "*":
					salida += "FLD " + opera2 + "\n";
					salida += "FIMUL " + opera1 + "\n";
					break;
				case "/":
					salida += "FLD " + opera2 + "\n";
					salida += "FIDIVR " + opera1 + "\n";
					break;
				default:
					break;
				}
			} else {
				switch (t.getOperador()) {
				case "+":
					salida += "FLD " + opera1 + "\n";
					salida += "FIADD " + opera2 + "\n";
					break;
				case "-":
					salida += "FLD " + opera1 + "\n";
					salida += "FISUB " + opera2 + "\n";
					break;
				case "*":
					salida += "FLD " + opera1 + "\n";
					salida += "FIMUL " + opera2 + "\n";
					break;
				case "/":
					salida += "FLD " + opera1 + "\n";
					salida += "FIDIV " + opera2 + "\n";
					break;
				case ":=":
					salida += "FILD " + opera2 + "\n";
					salida += "FST " + opera1 + "\n";
					break;
				default:
					break;
				}
			}
		}
	}
	
	
	public void toAssbyComp(Terceto t) {
		System.out.println();
		String opera1 = "_" + t.getOp1();
		String opera2 = "_" + t.getOp2();
		int i = 0;
		if (t.getOp1().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1));
			opera1 = tercetos.get(i).getReg();
		}
		if (t.getOp2().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1));
			opera2 = tercetos.get(i).getReg();
		}
		if (t.getToConv() == 0) {
			switch (t.getTipo()) {
			case "INTEGER":
				switch (t.getOperador()) {
				case ">": case "<": case ">=": case "<=": case "==":
					String libre =  this.getRegLibreOtros();
					salida += "MOV " + libre + ", " + opera2 +"\n";
					salida += "CMP " + opera1  + ", " + libre + "\n";
					break;
				default:
					break;
				}
				break;
			case "FLOAT":
				switch (t.getOperador()) {
				case ">":
					
					break;
				case "<":
					
					break;
				case ">=":
					
					break;
				case "<=":
					
					break;
				case "==":
					
					break;

				default:
					break;
				}
				break;
			default:
				break;
			}
			
		}else {
			if (t.getToConv() == 1) {
				
			}else {
				
			}
		}
	}
	
	public void toAssembler(List<Terceto> terce) {
		tercetos = terce;
		List<Integer> labels = new ArrayList<Integer>();
		for (Terceto t: tercetos) {
			if (labels.contains(t.getId()))
				salida += "Label" + t.getId() + ":\n";
			switch (t.getOperador()) {
			case "+":
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpSR(t,"ADD");
				}else {
					toAssFloatByOp(t);
				}
				break;
			case "-":
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpSR(t,"SUB");
				}else {
					toAssFloatByOp(t);
				}
				break;
			case "*":
				System.out.println(t.getId()+ t.getTipo());
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpMD(t,"MUL");
				}else {
					toAssFloatByOp(t);
				}
				break;
			case "/":
				if (t.getTipo().equals("INTEGER")) {
					toAssByOpMD(t,"DIV");
				}else {
					toAssFloatByOp(t);
				}
				break;
			case ":=":
				if (t.getTipo().equals("INTEGER")) {
					if (t.getOp2().charAt(0) == '[') {
						int i = Integer.parseInt(t.getOp2().substring(1,t.getOp2().length()-1));
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
							libre = "BX";
							if(Character.isDigit(t.getOp2().charAt(0)))
								salida += "MOV " + libre + ", " + t.getOp2() + "\n";
							else
								salida += "MOV " + libre + ", _" + t.getOp2() + "\n";
							
							if(Character.isDigit(t.getOp1().charAt(0)))
								salida += "MOV " + t.getOp1() + ", " + libre + "\n";
							else
								salida += "MOV _" + t.getOp1() + ", " + libre + "\n";
							registros.replace(libre, t.getId());
						}
					}
				}else {
					toAssFloatByOp(t);
				}
				break;
			case "O":
				break;
			case "P":
				salida += "JMP " + t.getOp1() + "\n";
				salida += "Label" + (t.getId()+1) + ":\n";
				break;
			case "BF":
				String compa = "";
				int i = Integer.parseInt(t.getOp1().substring(1,t.getOp1().length()-1));
				switch (tercetos.get(i).getOperador()) {
				case "<":
					compa = "JL";
					break;
				case ">":
					compa = "JG";
					break;
				case "<=":
					compa = "JLE";
					break;
				case ">=":
					compa = "JGE";
					break;
				case "==":
					compa = "JE";
					break;
				default:
					break;
				}
				salida+= compa +" Label" + t.getOp2().substring(1,t.getOp2().length()-1) + "\n";
				labels.add(Integer.parseInt(t.getOp2().substring(1,t.getOp2().length()-1)));
				break;
			case "BI":
				salida += "JMP Label" + t.getOp1().substring(1,t.getOp1().length()-1) + "\n";
				salida += "Label"+labels.remove(labels.size()-1)+":\n";
				labels.add(Integer.parseInt(t.getOp1().substring(1,t.getOp1().length()-1)));
				break;
			case "RET":
				salida += "JMP Label" + t.getOp1() + "\n";
				break;
			case "ET":
				if (t.getOp2()!= null)
					salida += "JMP FDEC" + t.getOp2() + "\n" +  t.getOp1() + ":\n";
				else
					salida +=  t.getOp1() + ":\n";
				break;
			case "FDEC":
				salida += "FDEC"+ t.getOp1() + ":\n";
				break;
			case ">":case "<":case ">=":case "<=":case "==":
				toAssbyComp(t);
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
