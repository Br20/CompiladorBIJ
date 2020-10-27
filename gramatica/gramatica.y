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

parametros 	: parametros ',' ID
			| ID
			| parametros ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");}
;	

parametrosinvo 	: ID
			| ID ',' ID
			| ID ',' ID ',' ID
			| ID ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");}
			| ID ',' ID ',' {yyerror($4.ival,"Parametro faltante luego de la ',' ");}
;



tipo		: INTEGER
			| FLOAT
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


procedure	: PROC ID '(' lp ')' '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $8.ival);}
			| PROC ID '(' lp ')' NA '=' factor ',' SHADOWING '=' boolean '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $15.ival);}
			| PROC ID '(' ')' '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival+ " hasta linea " + $7.ival);}
			| PROC ID '(' ')' NA '=' factor ',' SHADOWING '=' boolean '{' cuerpo '}' {agregarEstructura("PROCEDURE en linea " + $1.ival + " hasta linea " + $14.ival);}		
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

lp 		: tipo ID
			| tipo ID ',' tipo ID
			| tipo ID ',' tipo ID ',' tipo ID
			| tipo ID ',' {yyerror($2.ival,"Parametro faltante luego de la ',' ");}
			| tipo ID ',' tipo ID ',' {yyerror($4.ival,"Parametro faltante luego de la ',' ");}	
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
 			| CTE_INT { if ($1.sval.equals("32768")){
					yyerrorLex("Constante positiva fuera de rango");
					tSimbolos.replace($1.sval, (tSimbolos.get($1.sval)-1));
					if (tSimbolos.get($1.sval) == 0)
						tSimbolos.remove($1.sval);
					$1.sval="32767";
					if (!tSimbolos.containsKey($1.sval))
						tSimbolos.put($1.sval, 1);
					else 
						tSimbolos.replace($1.sval, (tSimbolos.get($1.sval)+1));
 				}}

			| '-' CTE_INT {	if (!tSimbolos.containsKey("-" +$2.sval))
								tSimbolos.put("-" +$2.sval, 1);
							else 
								tSimbolos.replace("-" + $2.sval, (tSimbolos.get("-" +$2.sval)+1));
							tSimbolos.replace($2.sval, (tSimbolos.get($2.sval)-1));
							if (tSimbolos.get($2.sval) == 0)
									tSimbolos.remove($2.sval);}
			| CTE_FLOAT {}
			| '-' CTE_FLOAT {	if (!tSimbolos.containsKey("-" +$2.sval))
								tSimbolos.put("-" +$2.sval, 1);
							else 
								tSimbolos.replace("-" + $2.sval, (tSimbolos.get("-" +$2.sval)+1));
							tSimbolos.replace($2.sval, (tSimbolos.get($2.sval)-1));
							if (tSimbolos.get($2.sval) == 0)
									tSimbolos.remove($2.sval);}
;

boolean 	: TRUE
			| FALSE
;	

%%
	private Lexico lexer;
	private int erroresS = 0;
	public static int nLinea = 1;
	public Hashtable<String, Integer> tSimbolos = new Hashtable<String, Integer>();
	private FileWriter txtErrores = null;
	private static PrintWriter pw = null;
	private FileWriter txtTokens = null;
	private static PrintWriter pwTo = null;
	private FileWriter txtTabla = null;
	private static PrintWriter pwTa = null;
	private FileWriter txtEstruc = null;
	private static PrintWriter pwEs = null;
	private StringBuffer buffer = new StringBuffer();

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
			pwTa.println("Simbolo: " + k + ", cantidad de referencias: " + v);
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
	
	private int yylex() {
		Par token = this.lexer.yylex();
		pwTo.println("Token entregado: " + token.getValue()); // agrega el token entregado al archivo de tokens
		yylval = new ParserVal(token.getKey());
		yylval.ival = nLinea; //se utiliza la variable ival de la clase ParserVal para guardar el numero de linea en el que se detecto el token
		if (token.getKey() != null) { // Si tiene lexema
			if (!tSimbolos.containsKey(token.getKey())) { // Si no existe en tabla de simbolos se crea para ese lexema
				tSimbolos.put(token.getKey(), 1);
			} else {
				tSimbolos.replace(token.getKey(), (tSimbolos.get(token.getKey()) + 1));
			}
		}
		return (int) token.getValue();
	}

	private void yyerror(String s) {
		if (!s.equals("syntax error")) { // Ignora el error default de yacc.
			erroresS++;
			System.out.println("Error de sintaxis cerca de la línea " + nLinea + ": " + s);
			pw.println("Error de sintaxis cerca de la línea " + nLinea + ": " + s);
		}
	}

	private void yyerror(int linea, String s) {
		erroresS++;
		System.out.println("Linea: " + linea + " - Error: " + s);
		pw.println("Error de sintaxis cerca de la línea " + linea + ": " + s);
	}

	private void yyerrorLex(String s) {
		// Agrega los errores lexicos que detecta la gramatica	
		Lexico.erroresL++;
		pw.println("Error cerca de la línea " + nLinea + ": " + s);
	}
