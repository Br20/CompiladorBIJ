%{
package compilador;
import java.lang.Math;
import java.util.Hashtable;
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
			| '(' ')' NA '=' factor ',' SHADOWING '=' boolean {} {addAtributoP(this.ultProc.toString(),"NA", $5.sval);
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

seleccion 	: IF '(' condicion ')' '{' cuerpo '}' ELSE '{' cuerpo '}' END_IF ';' {agregarEstructura("SENTENCIA DE SELECCION en linea " + $1.ival + " hasta linea " + $9.ival);}
			| IF '(' condicion ')' '{' cuerpo '}' END_IF ';' {agregarEstructura("SENTENCIA DE SELECCION en linea " + $1.ival + " hasta linea " + $7.ival);}
;


condicion 	:  expresion comparador expresion
;

comparador	: COMP_IGUAL
			| COMP_MENOR_IGUAL
			| COMP_MAYOR_IGUAL
			| DISTINTO
			| '<'
			| '>'
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


			
asignacion	: ID ASSIGN expresion ';' { validarDefinicion($1.sval);
									agregarEstructura("SENTENCIA DE ASIGNACION en linea " + $1.ival + " hasta linea " + $4.ival);}
			| tipo ID ASSIGN expresion ';'{
									if (redeclarable($2.sval) && setTipo($2.sval, $1.sval))
										addAtributo($2.sval,"Uso", "nombre de variable");
									validarDefinicion($2.sval);
									agregarEstructura("SENTENCIA DE ASIGNACION en linea " + $1.ival + " hasta linea " + $5.ival);}
;




expresion	: expresion '+' termino 
 			| expresion '-' termino 
 			| termino
;
 
termino 	: termino '*' factor
 			| termino '/' factor 
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
		return yyparse();
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
		int ultP = this.ambito.lastIndexOf(".");
		this.ambito.delete(ultP, this.ambito.length());
	}
	
	private void incrementarAmbito(String nProc) {
		this.ambito.append("." + nProc);
	}
	
	private boolean setTipo(String id, String tipo) {
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

	
	private boolean redeclarable(String id) {
		System.out.println("REDECLARABLE: " + id);
		String proc = this.ambito.toString();
		if(!proc.substring(proc.indexOf("."), proc.length()).equals(".p")) {
			proc = proc.substring(proc.lastIndexOf(".")+1, proc.length()) + proc.substring(proc.indexOf("."), proc.lastIndexOf("."));
			return shadowing(proc);
		}
		return true;
	}
	
	public boolean shadowing(String proc) {
		//System.out.println(proc + "HOLA");
		if (tSimbolos.containsKey(proc)) {
			if (tSimbolos.get(proc).containsKey("SHADOWING")) {
				System.out.println("SHADOWING:"+tSimbolos.get(proc));
				if(tSimbolos.get(proc).get("SHADOWING").toString().contains("FALSE")) {
					//System.out.println("ahora si anda bbto ");
					return false;
				}
			}			//System.out.println(proc.substring(proc.indexOf("."), proc.length()));
			//System.out.println(proc.substring(proc.indexOf("."), proc.length()).equals(".p"));
			if(proc.substring(proc.indexOf("."), proc.length()).equals(".p")) {
				//System.out.println("VA A YAMAR CON: " + proc.substring(proc.lastIndexOf(".")+1, proc.length()) + proc.substring(proc.indexOf("."), proc.lastIndexOf(".")));
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

	
	//VALIDA SI LA VARIABLE ESTA DEFINIDA EN EL AMBITO O AMBITOS ALCANZABLES
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
		int ultP = this.ambito.lastIndexOf(".");
		String proc = this.getNombreProc(this.ultProc + this.ambito.toString().substring(0, ultP));
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