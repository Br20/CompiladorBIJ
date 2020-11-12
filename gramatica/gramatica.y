%{
package compilador;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.io.*;
import java.util.StringTokenizer;
%}

/* YACC Declarations */
%token ID CTE_INT CTE_FLOAT IF THEN ELSE END_IF OUT FUNC RETURN INTEGER FLOAT CADENA COMENTARIO_ML WHILE LOOP PROC ASSIGN COMP_IGUAL COMP_MENOR_IGUAL COMP_MAYOR_IGUAL DISTINTO NA TRUE FALSE SHADOWING
%start programa


%%


programa 	: cuerpo {agregarEstructura("Se termino de compilar el programa correctamente");}
;

parametros 	: parametros ',' ID {if(redeclarable($3.sval)){
									if (setTipo($3.sval, lastTipo.toString()))
										addAtributo($3.sval,"Uso", "nombre de variable");
								}else{
									String var = this.getNombreVariable($3.sval + this.ambito.toString());
									if (var == null){
										if(setTipo($3.sval, lastTipo.toString()))
											addAtributo($3.sval,"Uso", "nombre de variable");
									}else
										yyerror("No se permite redeclarar la variable por shadowing");
								}}
			| ID {if(redeclarable($1.sval)){
									if (setTipo($1.sval, lastTipo.toString()))
										addAtributo($1.sval,"Uso", "nombre de variable");
								}else{
									String var = this.getNombreVariable($1.sval + this.ambito.toString());
									if (var == null){
										if(setTipo($1.sval, lastTipo.toString()))
											addAtributo($1.sval,"Uso", "nombre de variable");
									}else
										yyerror("No se permite redeclarar la variable por shadowing");
								}}			| parametros ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");}
;	

parametrosinvo 	: ID { validarDefinicion($1.sval);
					validarTipo($1.sval,1);
			}
			| ID ',' ID { validarDefinicion($1.sval);
						validarDefinicion($3.sval);
						validarTipo($1.sval,1);
						validarTipo($3.sval,2);
						}
			| ID ',' ID ',' ID{
						validarDefinicion($1.sval);
						validarDefinicion($3.sval);
						validarDefinicion($5.sval);
						validarTipo($1.sval,1);
						validarTipo($3.sval,2);
						validarTipo($5.sval,3);
						}
			| ID ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");
					 validarDefinicion($1.sval);
					 validarTipo($1.sval,1);
					 }
			| ID ',' ID ',' {yyerror($4.ival,"Parametro faltante luego de la ',' ");
							 validarDefinicion($1.sval);
							validarDefinicion($3.sval);
							validarTipo($1.sval,1);
							validarTipo($3.sval,2);
							}
;



tipo		: INTEGER {this.lastTipo = new StringBuffer($1.sval);}
			| FLOAT {this.lastTipo = new StringBuffer($1.sval);}
;
						

	

cuerpo		: cuerpo declarativa
			| declarativa
			| cuerpo ejecutable
			| ejecutable
;

cuerpowhile  	: cuerpowhile ejecutable
			| ejecutable
;

declarativa	: procedure
			| declaracion
;


procedure	: proc procdef '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $5.ival);
															reducirAmbito();}
;	


proc : PROC ID {incrementarAmbito($2.sval);
				this.ultProc = new StringBuffer($2.sval);
				addAtributoPDec(this.ultProc.toString(),"Uso", "nombre de procedimiento");}
;

procdef : 	  '(' lp ')'
			| '(' lp ')' NA '=' factor ',' SHADOWING '=' boolean {addAtributoP( this.ultProc.toString(),"NA", $6.sval);
																addAtributoP( this.ultProc.toString(),"SHADOWING", $10.sval);}
			| '(' ')'
			| '(' ')' NA '=' factor ',' SHADOWING '=' boolean {addAtributoP(this.ultProc.toString(),"NA", $5.sval);
																addAtributoP(this.ultProc.toString(),"SHADOWING", $9.sval);}
;


ejecutable	: asignacion
			| salida
			| seleccion
			| invocacion
			| control
			| error ';'  {yyerror($2.ival,"Error sintactico en sentencia ejecutable");}
;			

declaracion	: tipo parametros ';' {agregarEstructura("SENTENCIA DECLARATIVA en linea " + $1.ival + " hasta linea " + $3.ival);}
;


control 	: WHILE '(' condicion ')' LOOP '{' cuerpowhile '}' ';' {agregarEstructura("SENTENCIA DE CONTROL en linea " + $1.ival);}
;

invocacion 	: identificadorFuncion parametrosinvo ')' ';' {agregarEstructura("SENTENCIA DE INVOCACION en linea " + $1.ival);
											
											}
			| ID '(' ')' ';' {agregarEstructura("SENTENCIA DE INVOCACION en linea " + $1.ival);}
;


identificadorFuncion : ID '(' {ultProc = new StringBuffer($1.sval); }
;

salida		: OUT '(' CADENA ')' ';' {agregarEstructura("SENTENCIA DE SALIDA en linea: " + $1.ival);}
;

seleccion 	: ifcondi cuerpoif ELSE '{' cuerpo '}' END_IF ';' {Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
																tInc.setOp1("["+(Terceto.id)+"]");}
			| ifcondi cuerpoif END_IF ';' {Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
											tInc.setOp1("["+(Terceto.id)+"]");}
;

cuerpoif : '{' cuerpo '}' { Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
							tInc.setOp2("["+(Terceto.id+1)+"]");
							Terceto t = new Terceto("[?]",null,"BI",null);
							tercetos.add(t);
							colaTercetos.add(t);

}
;

ifcondi : IF '(' condicion ')' {Terceto t = new Terceto($3.sval,"[?]","BF", null);
								tercetos.add(t);
								colaTercetos.add(t);}

condicion 	:  expresion comparador expresion {$$.sval = "["+agregarTerceto($1.sval,$3.sval,$2.sval).toString()+"]";}
;

comparador	: COMP_IGUAL {$$.sval = "==";}
			| COMP_MENOR_IGUAL {$$.sval = "<=";}
			| COMP_MAYOR_IGUAL {$$.sval = ">=";}
			| DISTINTO {$$.sval = "!=";}
			| '<' {$$.sval = "<";}
			| '>'{$$.sval = ">";}
;

lp 		: tipo ID {if (setTipo($2.sval, $1.sval))
						addAtributoP(this.ultProc.toString(), "param1", $1.sval);
					addAtributo($2.sval,"Uso", "nombre de parametro");}
			| tipo ID ',' tipo ID { if(setTipo($2.sval, $1.sval))
										addAtributo($2.sval,"Uso", "nombre de parametro");
									if(setTipo($5.sval, $4.sval))
										addAtributo($5.sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", $1.sval);
									addAtributoP(this.ultProc.toString(), "param2", $4.sval);
									}
			| tipo ID ',' tipo ID ',' tipo ID { 
									if(setTipo($2.sval, $1.sval))
										addAtributo($2.sval,"Uso", "nombre de parametro");
									if(setTipo($5.sval, $4.sval))
										addAtributo($5.sval,"Uso", "nombre de parametro");
									if (setTipo($8.sval, $7.sval))
										addAtributo($8.sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", $1.sval);
									addAtributoP(this.ultProc.toString(), "param2", $4.sval);
									addAtributoP(this.ultProc.toString(), "param3", $7.sval);
									}
			| tipo ID ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");
							if(setTipo($2.sval, $1.sval))
								addAtributo($2.sval,"Uso", "nombre de parametro");
							addAtributoP(this.ultProc.toString(), "param1", $1.sval);
							}
			| tipo ID ',' tipo ID ',' {yyerror($4.ival,"Parametro faltante luego de la ',' ");
									if (setTipo($2.sval, $1.sval))
										addAtributo($2.sval,"Uso", "nombre de parametro");
									if (setTipo($5.sval, $4.sval))
										addAtributo($5.sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", $1.sval);
									addAtributoP(this.ultProc.toString(), "param2", $4.sval);
									}	
;


			
asignacion	: ID ASSIGN expresion ';' { if (validarDefinicion($1.sval))
											agregarTerceto($1.sval,$3.sval,":=");
									agregarEstructura("SENTENCIA DE ASIGNACION en linea " + $1.ival + " hasta linea " + $4.ival);}
			| tipo ID ASSIGN expresion ';'{
								if(redeclarable($2.sval)){
									if (setTipo($2.sval, lastTipo.toString())){
										addAtributo($2.sval,"Uso", "nombre de variable");
										agregarTerceto($2.sval,$4.sval,":=");
									}
								}else{
									String var = this.getNombreVariable($2.sval + this.ambito.toString());
									if (var == null){
										if(setTipo($2.sval, lastTipo.toString())){
											addAtributo($2.sval,"Uso", "nombre de variable");
											agregarTerceto($2.sval,$4.sval,":=");
										}
									}else
										yyerror("No se permite redeclarar la variable por shadowing");
								}}
;




expresion	: expresion '+' termino {$$.sval = "[" + agregarTerceto($1.sval, $3.sval,"+").toString()+"]";}
 			| expresion '-' termino {$$.sval = "[" + agregarTerceto($1.sval, $3.sval,"-").toString()+"]";}
 			| termino
;
 
termino 	: termino '*' factor {$$.sval = "[" + agregarTerceto($1.sval, $3.sval,"*").toString()+"]";}
 			| termino '/' factor{$$.sval = "[" + agregarTerceto($1.sval, $3.sval,"/").toString()+"]";}
 			| factor
;

factor		: ID {validarDefinicion($1.sval);}
 			| CTE_INT { 
				 	Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
					int cant = 0;
					if ($1.sval.equals("32768")){
						yyerrorLex("Constante positiva fuera de rango");
						hs = tSimbolos.get($1.sval);
						cant = (int)hs.get("Referencias").getValue();
						hs.get("Referencias").setValue(cant--);
						if ((int)hs.get("Referencias").getValue() == 0)
							tSimbolos.remove($1.sval);
						$1.sval="32767";
						if (!tSimbolos.containsKey($1.sval)){
							hs = new Hashtable<String, Atributo>();
							Atributo cantR = new Atributo("Referencias", 1);
							hs.put(cantR.getNombre(), cantR);
							tSimbolos.put($1.sval, hs);
						}
						else{ 
							hs = tSimbolos.get($1.sval);
							cant = (int)hs.get("Referencias").getValue();
							hs.get("Referencias").setValue(cant++);
						}
 				}}

			| '-' CTE_INT {	
							Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
							int cant = 0;
							if (!tSimbolos.containsKey("-" +$2.sval)){
								Atributo cantR = new Atributo("Referencias", 1);
								hs.put(cantR.getNombre(), cantR);
								tSimbolos.put("-" +$2.sval, hs);
							}
							else {
								hs = tSimbolos.get("-" + $2.sval);
								cant = (int)hs.get("Referencias").getValue();
								hs.get("Referencias").setValue(cant++);
							}
							hs = tSimbolos.get($2.sval);
							cant = (int)hs.get("Referencias").getValue();
							hs.get("Referencias").setValue(cant--);
							$$.sval = "-" +$2.sval;
							if ((int)tSimbolos.get($2.sval).get("Referencias").getValue() == 0)
									tSimbolos.remove($2.sval);}
			| CTE_FLOAT {}
			| '-' CTE_FLOAT {	
							Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
							int cant = 0;
							if (!tSimbolos.containsKey("-" +$2.sval)){
								Atributo cantR = new Atributo("Referencias", 1);
								hs.put(cantR.getNombre(), cantR);
								tSimbolos.put("-" +$2.sval, hs);
							}
							else {
								hs = tSimbolos.get("-" +$2.sval);
								cant = (int)hs.get("Referencias").getValue();
								hs.get("Referencias").setValue(cant++);
							}
							hs = tSimbolos.get($2.sval);
							cant = (int)hs.get("Referencias").getValue();
							hs.get("Referencias").setValue(cant--);
							$$.sval = "-" + $2.sval;
							if ((int)tSimbolos.get($2.sval).get("Referencias").getValue() == 0)
									tSimbolos.remove($2.sval);}
;

boolean 	: TRUE
			| FALSE
;	

%%
	private Lexico lexer;
	private int erroresS = 0;
	public static int nLinea = 1;
	public Hashtable<String, Hashtable<String, Atributo>> tSimbolos = new Hashtable<String, Hashtable<String, Atributo>>();
	private FileWriter txtErrores = null;
	private static PrintWriter pw = null;
	private FileWriter txtTokens = null;
	private static PrintWriter pwTo = null;
	private FileWriter txtTabla = null;
	private static PrintWriter pwTa = null;
	private FileWriter txtEstruc = null;
	private static PrintWriter pwEs = null;
	private StringBuffer buffer = new StringBuffer();
	private StringBuffer ambito = new StringBuffer(".p");
	private StringBuffer lastTipo = new StringBuffer();
	private StringBuffer ultProc = new StringBuffer();
	private List<Terceto> tercetos = new ArrayList<Terceto>();
	private List<Terceto> colaTercetos = new ArrayList<Terceto>();
	private String [][] matSuma = {{"INTEGER","FLOAT"},{"FLOAT","FLOAT"}};
	private String [][] matResta = {{"INTEGER","FLOAT"},{"FLOAT","FLOAT"}};
	private String [][] matMult = {{"INTEGER","FLOAT"},{"FLOAT","FLOAT"}};
	private String [][] matDiv = {{"INTEGER","FLOAT"},{"FLOAT","FLOAT"}};
	private String [][] matAsig = {{"INTEGER","X"},{"FLOAT","FLOAT"}};
	
	
	
	public int yyparse(String path) throws IOException {
		//Es la funcion publica con la que el main se comunica y le pasa la direccion del archivo con el codigo
		//Utiliza la ruta para crear todos los archivos que seran generados
		lexer = new Lexico(path);
		txtErrores = new FileWriter(path.substring(0, path.indexOf('.')) + "Errores.txt");
		pw = new PrintWriter(txtErrores);
		txtTokens = new FileWriter(path.substring(0, path.indexOf('.')) + "Tokens.txt");
		pwTo = new PrintWriter(txtTokens);
		txtTabla = new FileWriter(path.substring(0, path.indexOf('.')) + "TablaS.txt");
		pwTa = new PrintWriter(txtTabla);
		txtEstruc = new FileWriter(path.substring(0, path.indexOf('.')) + "Estructura.txt");
		pwEs = new PrintWriter(txtEstruc);
		escribirCodigoConLineas(path);
		return yyparse();
	}
	
	
	private void escribirCodigoConLineas(String path) throws IOException {
	    PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(path.substring(0, path.indexOf('.')) + "ConNroLinea.txt")));
	    BufferedReader lector = null;
	    int nroLinea = 1;
	    try {
	        lector = new BufferedReader(new FileReader(path));
	        String linea = lector.readLine();
	        while(linea != null)
	        {
	            escritor.write(nroLinea + "- "+ linea + "\n");
	            nroLinea++;
	            linea = lector.readLine();
	        }
	     }
	     catch (FileNotFoundException e) {
	         System.out.println("Error: Fichero no encontrado");
	         System.out.println(e.getMessage());
	     }
	    escritor.close();
	    lector.close();
	}
	
	public void imprimirTercetos() {
		for (Terceto t : tercetos) {
			System.out.println(t.toString());
		}
	}


	public static void escribirError(String s) {
		pw.println(s);
	}

	public void cerrarFicheros() throws IOException {
		if (txtErrores != null)
			txtErrores.close();
		if (txtTokens != null)
			txtTokens.close();
		if (txtTabla != null)
			txtTabla.close();
		if (txtEstruc != null)
			txtEstruc.close();
	}

	public void escribirTablaS() {
		//escribe todos los datos que se tenga en la tabla de simbolos con la cantidad de referencias
		tSimbolos.forEach((k, v) -> {
			pwTa.println("Simbolo: " + k + " " + v);
		});
	}

	private void agregarEstructura(String s) {
		//A medida que se detecta una estructura, se inserta en un stringbuffer que almacena estas
		buffer.insert(0, s + "\n");
	}

	public void escribirEstruc() {
		//se escribe todo lo del string buffer que registra las estructuras, en el archivo
		pwEs.println(buffer.toString());
	}
	
	
	private void reducirAmbito() {
		//Metodo invocado cuando el sintactico termina de leer un procedimiento, reduce el ambito sacando el nombre de ese procedimiento
		int ultP = this.ambito.lastIndexOf(".");
		this.ambito.delete(ultP, this.ambito.length());
	}
	
	private void incrementarAmbito(String nProc) {
		//Metodo invocado cuando el sintactico lee la definicion de un procedimiento, agrega el nombre al ambito actual.
		this.ambito.append("." + nProc);
	}
	
	private boolean setTipo(String id, String tipo) {
		//Metodo invocado en la definicion de variables, si no tiene el atributo "Uso" quiere decir que este id no esta asociado a ninguna 
		//variable ni procedimiento, si la puede definir retorna true en caso de que si se pueda definir(GRAMATICA)
		Atributo att = new Atributo("Tipo", tipo);
		Hashtable<String, Atributo> hs = this.tSimbolos.get(id + this.ambito.toString());
		if(!hs.containsKey("Uso")) {
			hs.put(att.getNombre(), att);
			return true;
		}else {
			if (hs.containsKey("Tipo"))
				yyerror("Se esta intentando redefinir una variable");
			else
				yyerror("El identificador esta asociado a un procedimiento");
			return false;
		}
	}

	
		private Integer agregarTerceto(String op1, String op2, String operador) {
		String tipo = null;
		System.out.println("op1 " + op1+ " op2 " + op2 + " operador " + operador);
		if (!tSimbolos.containsKey(op1) ) {
			//System.out.println(op1 + this.ambito);
			op1 = getNombreVariable(op1+this.ambito);
		}
		if (!tSimbolos.containsKey(op2)) {
			//System.out.println(op2 + this.ambito);
			op2 = getNombreVariable(op2+this.ambito);
		}
		System.out.println("DESPUES de modificar op1 " + op1+ " op2 " + op2 + " operador " + operador);
		if (op1!=null && op2!=null) {
			String t1 = tSimbolos.get(op1).get("Tipo").getValue().toString();
			String t2 = tSimbolos.get(op1).get("Tipo").getValue().toString();
			switch (operador) {
			case "+":
				if (t1.equals("INTEGER") && t2.equals("INTEGER"))
					tipo = matSuma[0][0];
				if (t1.equals("INTEGER") && t2.equals("FLOAT"))
					tipo = matSuma[0][1];
				if (t1.equals("FLOAT") && t2.equals("INTEGER"))
					tipo = matSuma[1][0];
				if (t1.equals("FLOAT") && t2.equals("FLOAT"))
					tipo = matSuma[1][1];
			case "-":
				if (t1.equals("INTEGER") && t2.equals("INTEGER"))
					tipo = matResta[0][0];
				if (t1.equals("INTEGER") && t2.equals("FLOAT"))
					tipo = matResta[0][1];
				if (t1.equals("FLOAT") && t2.equals("INTEGER"))
					tipo = matResta[1][0];
				if (t1.equals("FLOAT") && t2.equals("FLOAT"))
					tipo = matResta[1][1];
			case "/":
				if (t1.equals("INTEGER") && t2.equals("INTEGER"))
					tipo = matMult[0][0];
				if (t1.equals("INTEGER") && t2.equals("FLOAT"))
					tipo = matMult[0][1];
				if (t1.equals("FLOAT") && t2.equals("INTEGER"))
					tipo = matMult[1][0];
				if (t1.equals("FLOAT") && t2.equals("FLOAT"))
					tipo = matMult[1][1];
			case "*":
				if (t1.equals("INTEGER") && t2.equals("INTEGER"))
					tipo = matDiv[0][0];
				if (t1.equals("INTEGER") && t2.equals("FLOAT"))
					tipo = matDiv[0][1];
				if (t1.equals("FLOAT") && t2.equals("INTEGER"))
					tipo = matDiv[1][0];
				if (t1.equals("FLOAT") && t2.equals("FLOAT"))
					tipo = matDiv[1][1];
			case ":=":
				if (t1.equals("INTEGER") && t2.equals("INTEGER"))
					tipo = matAsig[0][0];
				if (t1.equals("INTEGER") && t2.equals("FLOAT"))
					tipo = matAsig[0][1];
				if (t1.equals("FLOAT") && t2.equals("INTEGER"))
					tipo = matAsig[1][0];
				if (t1.equals("FLOAT") && t2.equals("FLOAT"))
					tipo = matAsig[1][1];
			default:
				break;
			}
		}
		Terceto t = new Terceto(op1,op2,operador,tipo);
		if (tipo!=null) {
			Atributo a = new Atributo("Tipo", tipo);
			Atributo a2 = new Atributo("Uso", "terceto");
			Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
			hs.put(a.getNombre(), a);
			hs.put(a2.getNombre(),a2);
			tSimbolos.put("["+t.getId()+"]", hs);
			System.out.println("AGREGAAAA TERCETO op1 " + op1+ " op2 " + op2 + " operador " + operador);
 		}
		tercetos.add(t);
		
		return t.getId();
	}
	
	
	private boolean redeclarable(String id) {
		//Este metodo saca el ultimo procedimiento para llamar al metodo recursivo "shadowing"
		String proc = this.ambito.toString();
		if(!proc.substring(proc.indexOf("."), proc.length()).equals(".p")) {
			proc = proc.substring(proc.lastIndexOf(".")+1, proc.length()) + proc.substring(proc.indexOf("."), proc.lastIndexOf("."));
			return shadowing(proc);
		}
		return true;
	}
	
	public boolean shadowing(String proc) {
		//Verifica si en los ambitos anidados o el mismo hay una definicion de SHADOWING = FALSE indicando que no se pueden redeclarar 
		//variables en distintos ambitos
		if (tSimbolos.containsKey(proc)) { //No se si es necesario (Deberia de estar siempre definido)
			if (tSimbolos.get(proc).containsKey("SHADOWING")) { //Se fija si la definicion del procedimiento tiene el parametro "SHADOWING"
				if(tSimbolos.get(proc).get("SHADOWING").toString().contains("FALSE")) {
					return false;
				}
			}			
			if(proc.substring(proc.indexOf("."), proc.length()).equals(".p")) { //Ya recorrio todos los ambitos anidados por lo q retorna true
				return true;
			}else
				return shadowing(proc.substring(proc.lastIndexOf(".")+1, proc.length()) + proc.substring(proc.indexOf("."), proc.lastIndexOf(".")));			
		}
		System.out.println("NO DEBERIA LLEGAR ACA");
		return true;
	}
	
	
	public void addAtributoPDec(String id, String nAtt, String valor) {
		int ultP = this.ambito.lastIndexOf(".");
		Hashtable<String, Atributo> hs = this.tSimbolos.get(id + this.ambito.toString().substring(0, ultP));
		if(hs.containsKey("Uso")){
			yyerror("El nombre de procedimiento ya esta utilizado por una variable u otro procedimiento");
		}else {
			addAtributoP(id, nAtt, valor);
		}
	}
	
	
	public void addAtributoP(String id, String nAtt, String valor) {
		System.out.println(nAtt + " VALOR: " + valor);
		Atributo att = new Atributo(nAtt, valor);
		int ultP = this.ambito.lastIndexOf(".");
		Hashtable<String, Atributo> hs = this.tSimbolos.get(id + this.ambito.toString().substring(0, ultP));
		if(!hs.containsKey(nAtt) && !hs.containsKey("Tipo")) {
			hs.put(att.getNombre(), att);
		}
		else {
			yyerror("Se esta intentando cambiar un atributo o ese identificador ya esta utilizado como variable");
		}
	}
	
	public void addAtributo(String id, String nAtt, String valor) {
		Atributo att = new Atributo(nAtt, valor);
		Hashtable<String, Atributo> hs = this.tSimbolos.get(id + this.ambito.toString());
		if(!hs.containsKey(nAtt)) {
			hs.put(att.getNombre(), att);
		}
		else {
			System.out.println("Se esta intentando cambiar un atributo de " + id); //capaz q no tirar este error y solo verificar q no se quiere redefinir
		}
	}

	
	//VALIDA SI LA VARIABLE ESTA DEFINIDA EN EL AMBITO O AMBITOS ALCANZABLES PARA PODER USARLA
	public boolean validarDefinicion(String id) {
		String var = this.getNombreVariable(id + this.ambito.toString());
		if (var!= null) {
			Hashtable<String, Atributo> hs = this.tSimbolos.get(var);
			if(!hs.containsKey("Tipo") && hs.containsKey("Uso")) { //NO DEBERIA DE ENTRAR NUNCA PQ YA SE BUSCA QUE SEA DE VARIABLE(BORRAR SI ES ASI) 
				yyerror("Se esta intentando utilizar como variable un identificador asociado a un procedimiento");
				return true;
			}else if(!hs.containsKey("Tipo")) {
				yyerror("La variable que se esta intentando utilizar no esta declarada");
				return false;
			}else
				return true;
		}else 
			yyerror("La variable no fue previamente definida");
		return false;
	}


	public boolean validarTipo(String id, int indice){
		//VALIDA QUE EN LAS INVOCACIONES A PROCEDIMIENTOS ESTEN BIEN LA CANTIDAD DE PARAMETROS Y LOS TIPOS (LO HACE UNO 
		//A UNO A MEDIDA QUE LOS RECONOCE EN LA GRAMATICA)
		int ultP = this.ambito.lastIndexOf(".");
		String proc = this.getNombreProc(this.ultProc + this.ambito.toString().substring(0, ultP)); //BUSCA EL PROCEDIMIENTO MAS PROXIMO DEFINIDO EN AMBITOS
		if (proc != null) {
			if (this.tSimbolos.get(proc).containsKey("param" + indice)) {
				String tipoParamFormal = (String) this.tSimbolos.get(proc).get("param" + indice).getValue();
				String tipoParamReal = "";
				if (validarDefinicion(id)) {
					tipoParamReal = (String) this.tSimbolos.get(id + this.ambito).get("Tipo").getValue();
				}
				if (tipoParamFormal.equals(tipoParamReal)) {
					return true;
				}
				yyerror("Tipo del identificador pasado por parametro real no se corresponde con el parametro formal. Se espera " + tipoParamFormal + " y se recibio " + tipoParamReal);
				return false;
			} else { 
				yyerror("Error en al invocacion, numero de paramtros erroneo");
				return false;
			}
		}
		yyerror("Procedimiento no declarado");
		return false;
		
	}

	
	public String getNombreVariable(String s1) {
		//BUSCA RECURSIVAMENTE SEGUN EL ID SI LA VARIABLE ESTA DEFINIDA EN ALGUNO DE LOS AMBITOS ALCANZABLES, SI NO FUE DEFINIDA DEVUELVE NULL
		if (this.tSimbolos.containsKey(s1)) {
			if (this.tSimbolos.get(s1).containsKey("Tipo"))
				return s1;
			else {
				if (s1.substring(s1.indexOf("."), s1.length()).equals(".p"))
					return null;
				return this.getNombreVariable(s1.substring(0, s1.lastIndexOf(".")));
			}
		}
		else {
			if (s1.substring(s1.indexOf("."), s1.length()).equals(".p"))
				return null;
			return this.getNombreVariable(s1.substring(0, s1.lastIndexOf(".")));
		}
	}

	
	public String getNombreProc(String s1) {
		//BUSCA RECURSIVAMENTE SEGUN EL ID SI EL PROCEDIMIENTO ESTA DEFINIDA EN ALGUNO DE LOS AMBITOS ALCANZABLES, SI NO FUE DEFINIDA DEVUELVE NULL
		if (this.tSimbolos.containsKey(s1)) {
			if (!this.tSimbolos.get(s1).containsKey("Tipo") && this.tSimbolos.get(s1).containsKey("Uso"))
				return s1;
			else
				return this.getNombreProc(s1.substring(0, s1.lastIndexOf(".")));
		}
		else {
			if (s1.substring(s1.indexOf("."), s1.length()).equals(".p"))
				return null;
			return this.getNombreProc(s1.substring(0, s1.lastIndexOf(".")));
		}
	}
	

	private int yylex() {
		Par token = this.lexer.yylex();
		pwTo.println("Token entregado: " + token.getValue()); // agrega el token entregado al archivo de tokens
		yylval = new ParserVal(token.getKey());
		yylval.ival = nLinea; //se utiliza la variable ival de la clase ParserVal para guardar el numero de linea en el que se detecto el token
		if (token.getKey() != null) { // Si tiene lexema
			Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
			Atributo cantR = new Atributo("Referencias", 1);
			hs.put(cantR.getNombre(), cantR);
			if (token.getValue() == ID) {
				String lexema = token.getKey().concat(this.ambito.toString()); 
				if (!tSimbolos.containsKey(lexema)) { // Si no existe en tabla de simbolos se crea para ese lexema
					tSimbolos.put(lexema, hs);
				} else {
					hs = tSimbolos.get(lexema);
					int cant = (int)hs.get("Referencias").getValue();
					hs.get("Referencias").setValue(cant++);
				}
			}else {
				if (!tSimbolos.containsKey(token.getKey())) { // Si no existe en tabla de simbolos se crea para ese lexema
					tSimbolos.put(token.getKey(), hs);
					if (token.getValue() == CTE_FLOAT) {
						Atributo tipo = new Atributo("Tipo", "FLOAT");
						Atributo uso = new Atributo("Uso", "constante");
						hs.put("Tipo", tipo);
						hs.put("Uso", uso);
					}
					if (token.getValue() == CTE_INT) {
						Atributo tipo = new Atributo("Tipo", "INTEGER");
						Atributo uso = new Atributo("Uso", "constante");
						hs.put("Tipo", tipo);
						hs.put("Uso", uso);
					}
				} else {
					hs = tSimbolos.get(token.getKey());
					int cant = (int)hs.get("Referencias").getValue();
					hs.get("Referencias").setValue(cant++);
				}
			}
		}
		return (int) token.getValue();
	}

	private void yyerror(String s) {
		if (!s.equals("syntax error")) { // Ignora el error default de yacc.
			erroresS++;
			System.out.println("Error de sintaxis cerca de la linea " + nLinea + ": " + s);
			pw.println("Error de sintaxis cerca de la linea " + nLinea + ": " + s);
		}
	}

	private void yyerror(int linea, String s) {
		erroresS++;
		System.out.println("Linea: " + linea + " - Error: " + s);
		pw.println("Error de sintaxis cerca de la linea " + linea + ": " + s);
	}

	private void yyerrorLex(String s) {
		// Agrega los errores lexicos que detecta la gramatica	
		Lexico.erroresL++;
		pw.println("Error cerca de la linea " + nLinea + ": " + s);
	}