package compilador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class GeneradorAssembler {

	private String salida = "";
	private String header = "";
	private Hashtable<String, Integer> registros = null;
	//private String regLibre= "";
	private List<Terceto> tercetos = null;
	private int numAux = 0;
	//private int nLabel = 0;
	Hashtable<String, Hashtable<String, Atributo>> ts; 
	private FileWriter arcAssembler = null;
	private static PrintWriter pwAss = null;
	
	
	
	public GeneradorAssembler() {
		registros =  new Hashtable<String, Integer>();
		salida = "";
		registros.put("AX", -1);
		registros.put("BX", -1);
		registros.put("CX", -1);
		registros.put("DX", -1);
		
	}
	
	private void liberar(String reg) {
		//Este procedimiento recibe el nombre de un registro AX, BX, CX o DX cuando se necesita un registro para hacer una operacion
		//El metodo copia el valor del registro a liberar en una nueva variable auxiliar y agrega esta variable auxiliar con el tipo a la 
		//tabla de simbolos para que luego se defina en el .data
		int aux = registros.get(reg);
		//System.out.println(reg+"   "+aux);
		salida += "MOV @aux" + numAux + ", " + reg + "\n";
		tercetos.get(aux).setReg("@aux"+numAux);
		Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
		Atributo tipo = new Atributo ("Tipo",""+tercetos.get(aux).getTipo());
		registros.replace(reg, -1); //Una vez liberado el registro se marca con valor -1 en la hashtable que se reconoce para reconocerlo como libre
		hs.put("Tipo", tipo);
		ts.put("@aux"+numAux, hs);
		numAux ++;
	}
	
	
	
	private void toAssByOpSR(Terceto t, String op) {
		//Este metodo abstrae el comportamiento para la generacion de codigo de suma entre datos de tipo entero, ya que ambos se comportan igual
		//primero busca cuales seran las referencias en codigo assembler de los operandos y los guarda en variables de tipo string
		String opera1 = "_"; 
		String opera2 = "_"; 
		if (Character.isDigit(t.getOp1().charAt(0)))
			opera1 = "const"+t.getOp1().replace('.','_');
		else
			opera1 += t.getOp1();
		if (Character.isDigit(t.getOp2().charAt(0)))
			opera2 = "const"+t.getOp2().replace('.','_');
		else
			opera2 += t.getOp2();
		int i = 0;
		if (t.getOp1().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1));
			opera1 = tercetos.get(i).getReg();
		}
		if (t.getOp2().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1));
			opera2 = tercetos.get(i).getReg();
		}
		//Busca un regristo libre para guardar el primer operando y luego hacer la operacion indicada con el segundo operando
		String libre = getRegLibreOtros();
		if (libre==null) {
			liberar("BX");
			libre = "BX";
		}
		salida += "MOV "+ libre + ", " + opera1 + "\n";
		salida += op + " " + libre + ", " + opera2 + "\n";
		t.setReg(libre);//Se indica que el resultado de ese terceto estará guardado en el registro que se guardo el resultado del operador
		//Se agrega una instruccion de salto en caso de que el flag de overflow sea igual a 1
		salida += "JO LabelErrorOV\n";
	
	}
	
	private void toAssByOpMD(Terceto t, String op) {
		//primero calcula cual sera la referencia de los operando en el .data
		String opera1 = "_"; 
		String opera2 = "_"; 
		if (Character.isDigit(t.getOp1().charAt(0)))
			opera1 = "const"+t.getOp1().replace('.','_');
		else
			opera1 += t.getOp1();
		if (Character.isDigit(t.getOp2().charAt(0)))
			opera2 = "const"+t.getOp2().replace('.','_');
		else
			opera2 += t.getOp2();
		int i = 0;
		if (t.getOp1().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1));
			opera1 = tercetos.get(i).getReg();
		}
		if (t.getOp2().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1));
			opera2 = tercetos.get(i).getReg();
		}
		if (t.getOperador().equals("/")) { //En caso de que la operacion sea una division, se compara el divisor con 0 y en caso de ser igual se
											//salta a la etiqueta de este error
			if (registros.get("AX")!=-1)
				liberar("AX");
			salida += "MOV AX, " + opera2 + "\n";
			salida += "CMP AX, 0\n";
			salida += "JE LabelErrorDIV\n";
		}
		if (registros.get("AX")!= -1) //Como las divisiones y multiplicaciones se realizan sobre el registro AX, se verifica si está libre y en caso contrrario se lo libera
			liberar("AX");
		salida += "MOV AX, " + opera1 + "\n";
		salida += "CWD\n"; 
		salida += op + " " + opera2 + "\n";
		registros.replace("AX", t.getId()); //Se marca el registro como ocupado
		t.setReg("AX");//Se indica que el resultado de ese terceto estará guardado en el registro que se guardo el resultado del operador
	}
	
	public void toAssFloatByOp(Terceto t) {
		//primero calcula cual sera la referencia de los operando en el .data
		String opera1 = "_"; 
		String opera2 = "_"; 
		if (Character.isDigit(t.getOp1().charAt(0)))
			opera1 = "const"+t.getOp1().replace('.','_');
		else
			opera1 += t.getOp1();
		if (Character.isDigit(t.getOp2().charAt(0)))
			opera2 = "const"+t.getOp2().replace('.','_');
		else
			opera2 += t.getOp2();
		int i = 0;
		if (t.getOp1().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1));
			opera1 = tercetos.get(i).getReg();
		}
		if (t.getOp2().charAt(0) == '[') {
			i = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1));
			opera2 = tercetos.get(i).getReg();
		}
		if (t.getToConv() == 0) { //indica que no hay que hacer ninguna conversion de los operando y la operacion es entre valores flotantes
			switch (t.getOperador()) {
			case "+":
				salida += "FLD " + opera1 + "\n"; //Tanto para la suma como para la resta carga el primer operando en ST
				salida += "FADD " + opera2 + "\n"; //Realiza la operacion de ST con el segundo operando
				salida += "FSTSW @mem2byte\n";  //Carga la palabra de estado en la variable @men2byte
				if (registros.get("AX") != -1)  //Verifica si AX esta libre, para liberarlo en caso contrario
					liberar("AX");
				salida += "MOV AX , @mem2byte\n"; //Copia el valor @mem2byte en AX
				salida += "SAHF\n"; //Copia el valor de AH en los 8 bits del registro de indicadores
				salida += "JO LabelErrorOV\n"; //Salta a la etiqueta por error de overflow en caso 
				break;
			case "-":
				salida += "FLD " + opera1 + "\n";
				salida += "FSUB " + opera2 + "\n";
				salida += "FSTSW @mem2byte\n";
				if (registros.get("AX") != -1)
					liberar("AX");
				salida += "MOV AX , @mem2byte\n";
				salida += "SAHF\n";
				salida += "JO LabelErrorOV\n";
				break;
			case "*":
				salida += "FLD " + opera1 + "\n";
				salida += "FMUL " + opera2 + "\n";
				break;
			case "/":
				salida += "FLD " + opera2 + "\n";
				salida += "FTST\n"; 
				salida += "FSTSW @mem2byte\n";
				if (registros.get("AX") != -1)
					liberar("AX");
				salida += "MOV AX , @mem2byte\n";
				salida += "SAHF\n"; 
				salida += "JZ LabelErrorDIV\n";
				salida += "FDIVR " + opera1 + "\n";
				break;
			case ":=":
				salida += "FLD " + opera2 + "\n";
				salida += "FSTP " + opera1 + "\n";
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
					salida += "FSTSW @mem2byte\n";
					if (registros.get("AX") != -1)
						liberar("AX");
					salida += "MOV AX , @mem2byte\n";
					salida += "SAHF\n";
					salida += "JO LabelErrorOV\n";
					break;
				case "-":
					salida += "FLD " + opera2 + "\n";
					salida += "FISUBR " + opera1 + "\n";
					salida += "FSTSW @mem2byte\n";
					if (registros.get("AX") != -1)
						liberar("AX");
					salida += "MOV AX , @mem2byte\n";
					salida += "SAHF\n";
					salida += "JO LabelErrorOV\n";
					break;
				case "*":
					salida += "FLD " + opera2 + "\n";
					salida += "FIMUL " + opera1 + "\n";
					break;
				case "/":
					salida += "FLD " + opera2 + "\n";
					salida += "FTST\n"; 
					salida += "FSTSW @mem2byte\n";
					if (registros.get("AX") != -1)
						liberar("AX");
					salida += "MOV AX , @mem2byte\n";
					salida += "SAHF\n";
					salida += "JZ LabelErrorDIV\n";
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
					salida += "FSTSW @mem2byte\n";
					if (registros.get("AX") != -1)
						liberar("AX");
					salida += "MOV AX , @mem2byte\n";
					salida += "SAHF\n";
					salida += "JO LabelErrorOV\n";
					break;
				case "-":
					salida += "FLD " + opera1 + "\n";
					salida += "FISUB " + opera2 + "\n";
					salida += "FSTSW @mem2byte\n";
					if (registros.get("AX") != -1)
						liberar("AX");
					salida += "MOV AX , @mem2byte\n";
					salida += "SAHF\n";
					salida += "JO LabelErrorOV\n";
					break;
				case "*":
					salida += "FLD " + opera1 + "\n";
					salida += "FIMUL " + opera2 + "\n";
					break;
				case "/":
					salida += "FILD " + opera2 + "\n";
					salida += "FTST\n"; 
					salida += "FSTSW @mem2byte\n";
					if (registros.get("AX") != -1)
						liberar("AX");
					salida += "MOV AX , @mem2byte\n";
					salida += "SAHF\n";
					salida += "JZ LabelErrorDIV\n";
					salida += "FDIVR " + opera1 + "\n";
					break;
				case ":=":
					salida += "FILD " + opera2 + "\n";
					salida += "FSTP " + opera1 + "\n";
					break;
				default:
					break;
				}
			}
		}
		if (!t.getOperador().equals(":=")) {
			salida += "FSTP @aux" + numAux + "\n";
			t.setReg("@aux"+numAux);
			Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
			Atributo tipo = new Atributo ("Tipo",""+t.getTipo());
			hs.put("Tipo", tipo);
			ts.put("@aux"+numAux, hs);
			numAux++;
		}
	}
	
	
	public void toAssbyComp(Terceto t) {
		//System.out.println();
		String opera1 = "_"; 
		String opera2 = "_"; 
		if (Character.isDigit(t.getOp1().charAt(0))) {
			opera1 = "const"+t.getOp1().replace('.','_');
		}
		else
			opera1 += t.getOp1();
		if (Character.isDigit(t.getOp2().charAt(0))) {
			opera2 = "const"+t.getOp2().replace('.','_');
		}
		else
			opera2 += t.getOp2();
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
					String libre =  this.getRegLibreOtros(); //VERIFICAR SI HAY LIBRE
					salida += "MOV " + libre + ", " + opera2 +"\n";
					salida += "CMP " + opera1  + ", " + libre + "\n";
					break;
				default:
					break;
				}
				break;
			case "FLOAT":
				salida += "FLD " + opera1 + "\n";
				salida += "FCOMP " + opera2 + "\n";
				salida += "FSTSW @mem2byte\n";
				if (registros.get("AX") != -1)
					liberar("AX");
				salida += "MOV AX , @mem2byte\n";
				salida += "SAHF\n";
				break;
			default:
				break;
			}
			
		}else {
			if (t.getToConv() == 1) {
				salida += "FILD " + opera1 + "\n";
				salida += "FCOMP " + opera2 + "\n";
				salida += "FSTSW @mem2byte\n";
				if (registros.get("AX") != -1)
					liberar("AX");
				salida += "MOV AX , @mem2byte\n";
				salida += "SAHF\n";
			}else {
				salida += "FLD " + opera1 + "\n";
				salida += "FICOMP " + opera2 + "\n";
				salida += "FSTSW @mem2byte\n";
				if (registros.get("AX") != -1)
					liberar("AX");
				salida += "MOV AX , @mem2byte\n";
				salida += "SAHF\n";
			}
		}
	}
	
	
	private void setHeader() {
		header += "include \\masm32\\include\\masm32rt.inc\n";
		header += ".386\n";
		header += ".model flat\n";
		header += "option casemap :none\n";
		header += "include \\masm32\\include\\windows.inc\n"; 
		header += "include \\masm32\\include\\kernel32.inc\n";
        header += "include \\masm32\\include\\user32.inc\n";
		header += "includelib \\masm32\\lib\\kernel32.lib\n";
		header += "includelib \\masm32\\lib\\user32.lib\n";
		header += ".data\n";
		header += "Error db \"Error\", 0\n";
		header += "ErrorDiv db \"Error al intentar dividir por cero\", 0\n";
		header += "ErrorOv db \"Error por overflow en una suma\", 0\n";
		header += "Salida dt ?, 0\n";
		header += "Imp db \"Salida por pantalla\", 0\n";
		header += "@mem2byte dw ? ; 16 bits\n";
	}
	
	
	private void setData() {
		ts.forEach((k, v) -> {
			if (v.containsKey("Tipo")) {
				//System.out.println(k + "   " + v.get("Tipo").getValue());
				String tipo = (String)v.get("Tipo").getValue();
				if (tipo.equals("INTEGER")) {
					if (!Character.isDigit(k.charAt(0)) && k.charAt(0)!= '[' && k.charAt(0)!= '@') {
						header += "_"+k + " dw ? ; 16 bits\n";
					}
					if (Character.isDigit(k.charAt(0)))
						header += "const" + k + " dw " + k + " ; 16 bits\n";
					if (k.charAt(0)== '@')
						header += k + " dw ? ; 16 bits\n";
				}
				if (tipo.equals("FLOAT")) {
					if (!Character.isDigit(k.charAt(0)) && k.charAt(0)!= '[' && k.charAt(0)!= '@') {
						header += "_"+k + " dd ? ; 32 bits\n";
					}
					if (Character.isDigit(k.charAt(0)))
						header += "const" + k.replace('.','_') + " dd " + k + " ; 32 bits\n";
					if (k.charAt(0)== '@')
						header += k + " dd ? ; 32 bits\n";
				}
				if (tipo.equals("CADENA")) {
					header += "cad" + k.replaceAll(" ", "") + " db \"" + k + "\", 0\n";
				}
			}
		});
		header += ".code\n";
		header += "start:\n";
		//header += "F[N]INIT\n";
		
	}
	
	
	public void toAssembler(List<Terceto> terce, Hashtable<String, Hashtable<String, Atributo>> ts, String path) throws IOException {
		tercetos = terce;
		this.ts = ts;
		arcAssembler = new FileWriter(path.substring(0, path.indexOf('.')) + ".asm");
		pwAss = new PrintWriter(arcAssembler);
		setHeader();
		List<Integer> labels = new ArrayList<Integer>();
		for (Terceto t: tercetos) {
			if (labels.contains(t.getId())) //Si se llega a
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
				//System.out.println(t.getId()+ t.getTipo());
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
					String opera1 = "_"; 
					String opera2 = "_"; 
					if (Character.isDigit(t.getOp1().charAt(0))) {
						opera1 = "const"+t.getOp1().replace('.','_');
					}
					else
						opera1 += t.getOp1();
					if (Character.isDigit(t.getOp2().charAt(0))) {
						opera2 = "const"+t.getOp2().replace('.','_');
					}
					else
						opera2 += t.getOp2();
					int i = 0;
					if (t.getOp1().charAt(0) == '[') {
						i = Integer.parseInt(t.getOp1().substring(1, t.getOp1().length()-1));
						opera1 = tercetos.get(i).getReg();
					}
					if (t.getOp2().charAt(0) == '[') {
						i = Integer.parseInt(t.getOp2().substring(1, t.getOp2().length()-1));
						opera2 = tercetos.get(i).getReg();
					}
					if (opera2.charAt(0)=='_' || opera2.charAt(0)=='@' || opera2.charAt(0)=='c') {
						String libre = getRegLibreOtros();
						if (libre == null) {
							liberar("BX");
							libre = "BX";
						}
						salida += "MOV " + libre + ", " + opera2 + "\n";
						opera2 = ""+libre;
					}
					//salida += "MOV BX, " + opera2 + "\n";
					salida += "MOV " + opera1 + ", "+opera2+"\n";
				}else {
					toAssFloatByOp(t);
				}
				break;
			case "O": //Si es un terceto de una salida por pantalla se invoca al messagebox con la variable en la que se guardo esa cadena
				salida += "invoke MessageBox, NULL, addr cad" + t.getOp1().replaceAll(" ", "")+", addr Imp, MB_OK\n";
				break;
			case "P": //Si se detecta un terceto de la invocacion a un procedimiento se hace un call al identificador que esta en el primer operando 
				salida += "CALL " + t.getOp1() + "\n";
				break;
			case "BF": //Si es un terceto de bifuercacion por falso, se verifica cual fue el operador de la comparacion y en baso a eso se genera el salto inverso
				String compa = "";
				int i = Integer.parseInt(t.getOp1().substring(1,t.getOp1().length()-1));
				switch (tercetos.get(i).getTipo()) {
				case "INTEGER"://En caso de q haya sido una comparacion por flotante usamos los saltos de aritmetica sin signo
					switch (tercetos.get(i).getOperador()) {

					case "<":
						compa = "JGE";
						break;
					case ">":
						compa = "JLE";
						break;
					case "<=":
						compa = "JG";
						break;
					case ">=":
						compa = "JL";
						break;
					case "==":
						compa = "JNE";
						break;
					default:
						break;
					}
					break;
				case "FLOAT": //En caso de q haya sido una comparacion por flotante usamos los saltos de aritmetica sin signo
					switch (tercetos.get(i).getOperador()) {
					case "<":
						compa = "JAE";
						break;
					case ">":
						compa = "JBE";
						break;
					case "<=":
						compa = "JA";
						break;
					case ">=":
						compa = "JB";
						break;
					case "==":
						compa = "JNE";
						break;
					default:
						break;
					}
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
				//System.out.println(t.toString());
				salida += "RET\n";
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
		setData();
		salida += "JMP LabelFin\n";
		
		salida += "LabelErrorOV:\n";
		salida += "invoke MessageBox, NULL, addr ErrorOv, addr Error, MB_OK\n";
		salida += "invoke ExitProcess, 0\n";
		
		salida += "LabelErrorDIV:\n";
		salida += "invoke MessageBox, NULL, addr ErrorDiv, addr Error, MB_OK\n";
		salida += "invoke ExitProcess, 0\n";
		
		salida += "LabelFin:\n";
		salida += "invoke ExitProcess, 0\n";
		salida += "end start";
		escribirArchivo();
	}
	
	
	
	private void escribirArchivo() throws IOException {
		pwAss.print(header);
		pwAss.print(salida);
		arcAssembler.close();
	}
	
	private String getRegLibreMD() {
		if (registros.get("AX") == -1)
			return "AX";
		if (registros.get("DX") == -1)
			return "DX";
		return null;
	}
	
	private String getRegLibreOtros() {
		if (registros.get("BX") == -1)
			return "BX";
		if (registros.get("CX") == -1)
			return "CX";
		return null;
	}
	
}
