%{
package compilador;
import java.lang.Math;
import java.util.Hashtable;
import javafx.util.Pair;
import java.io.*;
import java.util.StringTokenizer;
%}

/* YACC Declarations */
%token ID CTE_INT CTE_FLOAT IF THEN ELSE END_IF OUT FUNC RETURN INTEGER FLOAT CADENA COMENTARIO_ML WHILE LOOP PROC ASSIGN COMP_IGUAL COMP_MENOR_IGUAL COMP_MAYOR_IGUAL DISTINTO NA TRUE FALSE SHADOWING
%start programa


%%


programa 	: cuerpo {agregarEstructura("Se termino de compilar el programa correctamente");}
;

parametros 	: parametros ',' ID {setTipo($3.sval, lastTipo.toString());}
			| ID {setTipo($1.sval, lastTipo.toString());}
			| parametros ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");}
;	

parametrosinvo 	: ID
			| ID ',' ID
			| ID ',' ID ',' ID
			| ID ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");}
			| ID ',' ID ',' {yyerror($4.ival,"Parametro faltante luego de la ',' ");}
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


procedure	: proc '(' lp ')' '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $8.ival);
															reducirAmbito();}
			| proc '(' lp ')' NA '=' factor ',' SHADOWING '=' boolean '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $15.ival);
																									reducirAmbito();}
			| proc '(' ')' '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival+ " hasta linea " + $7.ival);
														reducirAmbito();}
			| proc '(' ')' NA '=' factor ',' SHADOWING '=' boolean '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $14.ival);
																								reducirAmbito();}		
;	


proc : PROC ID {incrementarAmbito($2.sval);}
;


ejecutable	: asignacion
			| salida
			| seleccion
			| invocacion
			| control
			| error ';'  {yyerror($2.ival,"Error sintactico en sentencia ejecutable");}
;			

declaracion	: tipo parametros ';' {agregarEstructura("SENTENCIA DECLARATIVA en linea " + $1.ival + " hasta linea " + $3.ival);
									System.out.println("Aca andan los paramentro pa: " + $2.sval +"  " + $1.sval);}
;


control 	: WHILE '(' condicion ')' LOOP '{' cuerpowhile '}' ';' {agregarEstructura("SENTENCIA DE CONTROL en linea " + $1.ival);}
;

invocacion 	: ID '(' parametrosinvo ')' ';' {agregarEstructura("SENTENCIA DE INVOCACION en linea " + $1.ival);}
			| ID '(' ')' ';' {agregarEstructura("SENTENCIA DE INVOCACION en linea " + $1.ival);}
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

lp 		: tipo ID {setTipo($2.sval, $1.sval);}
			| tipo ID ',' tipo ID { setTipo($2.sval, $1.sval);
									setTipo($5.sval, $4.sval);
									}
			| tipo ID ',' tipo ID ',' tipo ID { setTipo($2.sval, $1.sval);
									setTipo($5.sval, $4.sval);
									setTipo($8.sval, $7.sval);
									}
			| tipo ID ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");
							setTipo($2.sval, $1.sval);}
			| tipo ID ',' tipo ID ',' {yyerror($4.ival,"Parametro faltante luego de la ',' ");
									setTipo($2.sval, $1.sval);
									setTipo($5.sval, $4.sval);}	
;


			
asignacion	: ID ASSIGN expresion ';' {agregarEstructura("SENTENCIA DE ASIGNACION en linea " + $1.ival + " hasta linea " + $4.ival);}
			| tipo ID ASSIGN expresion ';'{agregarEstructura("SENTENCIA DE ASIGNACION en linea " + $1.ival + " hasta linea " + $5.ival);}
;

expresion	: expresion '+' termino 
 			| expresion '-' termino 
 			| termino
;
 
termino 	: termino '*' factor
 			| termino '/' factor 
 			| factor
;

factor		: ID
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
		//Se encarga de cargar los errores detectados en el archivo correspondiente
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
	
	private void setTipo(String id, String tipo) {
		Atributo att = new Atributo("Tipo", tipo);
		Hashtable<String, Atributo> hs = this.tSimbolos.get(id + this.ambito.toString());
		if(!hs.containsKey("Tipo")) {
			hs.put(att.getNombre(), att);
		}else {
			yyerror("Se esta intentando redefinir una variable");
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