//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compilador;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.io.*;
import java.util.StringTokenizer;
//#line 25 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE_INT=258;
public final static short CTE_FLOAT=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short END_IF=263;
public final static short OUT=264;
public final static short FUNC=265;
public final static short RETURN=266;
public final static short INTEGER=267;
public final static short FLOAT=268;
public final static short CADENA=269;
public final static short COMENTARIO_ML=270;
public final static short WHILE=271;
public final static short LOOP=272;
public final static short PROC=273;
public final static short ASSIGN=274;
public final static short COMP_IGUAL=275;
public final static short COMP_MENOR_IGUAL=276;
public final static short COMP_MAYOR_IGUAL=277;
public final static short DISTINTO=278;
public final static short NA=279;
public final static short TRUE=280;
public final static short FALSE=281;
public final static short SHADOWING=282;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    2,    3,    3,    3,    3,    3,    4,
    4,    1,    1,    1,    1,    7,    7,    5,    5,    8,
   10,   11,   11,   11,   11,    6,    6,    6,    6,    6,
    6,    9,   19,   18,   18,   21,   16,   17,   17,   23,
   22,   20,   25,   25,   25,   25,   25,   25,   12,   12,
   12,   12,   12,   15,   15,   24,   24,   24,   26,   26,
   26,   13,   13,   13,   13,   13,   14,   14,
};
final static short yylen[] = {                            2,
    1,    3,    1,    2,    1,    3,    5,    2,    4,    1,
    1,    2,    1,    2,    1,    2,    1,    1,    1,    5,
    2,    3,   10,    2,    9,    1,    1,    1,    1,    1,
    2,    3,    9,    4,    4,    2,    5,    8,    4,    3,
    4,    3,    1,    1,    1,    1,    1,    1,    2,    5,
    8,    3,    6,    4,    5,    3,    3,    1,    3,    3,
    1,    1,    1,    2,    1,    2,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   10,   11,    0,    0,    0,    0,
    0,   13,   15,   18,   19,    0,   26,   27,   28,   29,
   30,    0,    0,   31,    0,    0,    0,    0,    0,   21,
   12,   14,    0,    0,    0,    0,    0,    0,    0,    0,
   62,   63,   65,    0,   61,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   32,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,   66,   54,    0,    0,    0,
    0,   35,   41,   43,   44,   45,   46,   47,   48,    0,
    0,    0,    0,    2,    0,    0,    0,    0,    0,   34,
   40,    0,   39,    0,    0,   59,   60,    0,   37,    0,
   55,    0,    0,    0,   20,    0,    0,    0,    0,    0,
    0,    7,    0,    0,   17,    0,    0,    0,    0,    0,
    0,    0,   16,    0,    0,    0,   38,   33,    0,    0,
    0,   67,   68,   25,   51,    0,   23,
};
final static short yydgoto[] = {                          9,
   10,   34,   38,   11,   12,   13,  116,   14,   15,   16,
   36,   58,   45,  134,   17,   18,   19,   20,   21,   49,
   22,   23,   40,   50,   80,   47,
};
final static short yysindex[] = {                      -222,
  -43,  -25,  -13,   -4,    0,    0,    4, -217,    0, -222,
 -209,    0,    0,    0,    0,   25,    0,    0,    0,    0,
    0, -179,  -24,    0,  -42,   41,  -42, -169,  -42,    0,
    0,    0, -173,    3,  -40,  -21,   59,   63, -222, -204,
    0,    0,    0, -184,    0,   10,   24,   50,   69,  -23,
   70,   71,  -42, -144,    0, -164, -141,   76, -222, -139,
   60, -119,   -3,   62,    0,    0,    0,  -42,  -42,  -42,
  -42,    0,    0,    0,    0,    0,    0,    0,    0,  -42,
   64, -150,   11,    0,   65,   80, -154, -100,   83,    0,
    0, -222,    0,   24,   24,    0,    0,   36,    0,    5,
    0,  -42, -175,   68,    0, -127,  -82, -180,   87, -125,
  -42,    0, -130, -123,    0,  -64, -147,   92,   95,   81,
 -173,   84,    0,   85, -175, -140,    0,    0, -186, -113,
   86,    0,    0,    0,    0, -186,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  150,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -106,    0,    0,    0,    0,
    0,    0,    8,    0,    0,    0,  112,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,   13,    0,   32,    0,    0,    0,  117,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  118,   38,    0,  121,    0,
    0,    0,    0,  -36,  -31,    0,    0,  122,    0,    0,
    0,    0,  124,    0,    0,  125,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  128,    0,    0,
    0,    0,    0,    0,  129,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   -9,    0,    0,  -18,    1,   -2,    0,    0,    0,    0,
    0,    0,  -38,   40,    0,    0,    0,    0,    0,  143,
    0,    0,    0,  -12,    0,   28,
};
final static int YYTABLESIZE=255;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   56,   58,   44,   58,   56,   91,   56,   32,   56,   57,
   31,   57,   46,   57,   26,   24,   57,   58,   58,   68,
   58,   69,   56,   56,  105,   56,   27,   57,   57,   62,
   57,   96,   97,    1,    2,   28,   78,    3,   79,   30,
   83,    4,  113,   29,    5,    6,   54,   33,    7,   88,
    8,    3,   68,   68,   69,   69,    4,   63,   64,   32,
  122,   55,   31,  109,   35,   70,    3,   98,   67,  101,
   71,    4,  119,   65,   66,    1,    2,   37,   68,    3,
   69,   48,  107,    4,  110,   32,    5,    6,   31,  114,
    7,    5,    6,  132,  133,   94,   95,  114,   39,   51,
   53,   59,   60,   61,   32,  115,  130,   31,   72,   73,
   81,   82,   84,  123,   85,   86,   87,   89,   90,   92,
   93,  100,   99,  103,  104,  102,  106,  108,  111,  112,
  117,  118,  120,  121,  124,  125,    1,    2,  126,  127,
    3,  131,  128,  135,    4,  129,  136,    5,    6,    1,
   36,    7,    5,    8,   24,    1,    2,    8,   49,    3,
   22,    6,   42,    4,   52,    9,    5,    6,   50,   53,
    7,   52,    8,    1,    2,  137,    0,    3,    0,    0,
    0,    4,    0,    0,    5,    6,    0,    0,    7,    0,
    8,    1,    2,    0,    0,    3,    0,    0,    0,    4,
    0,    0,    5,    6,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,   41,   42,   43,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    5,    6,    0,    0,
    0,    0,    0,   58,   58,   58,   58,    0,   56,   56,
   56,   56,    0,   57,   57,   57,   57,    0,   25,    0,
    0,   74,   75,   76,   77,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   45,   45,   41,  125,   43,   10,   45,   41,
   10,   43,   25,   45,   40,   59,   35,   59,   60,   43,
   62,   45,   59,   60,  125,   62,   40,   59,   60,   39,
   62,   70,   71,  256,  257,   40,   60,  260,   62,  257,
   53,  264,  125,   40,  267,  268,   44,  257,  271,   59,
  273,   44,   43,   43,   45,   45,   44,  262,  263,   62,
  125,   59,   62,  102,   40,   42,   59,   80,   59,   59,
   47,   59,  111,  258,  259,  256,  257,  257,   43,  260,
   45,   41,   92,  264,  103,   88,  267,  268,   88,  108,
  271,  267,  268,  280,  281,   68,   69,  116,  123,  269,
  274,  123,   44,   41,  107,  108,  125,  107,   59,   41,
   41,   41,  257,  116,  279,  257,   41,  257,   59,  123,
   59,  272,   59,   44,  279,   61,   44,  123,   61,  257,
   44,  257,  263,  257,  282,   44,  256,  257,   44,   59,
  260,  282,   59,  257,  264,   61,   61,  267,  268,    0,
  257,  271,   41,  273,  123,  256,  257,   41,   41,  260,
  123,   41,   41,  264,   41,   41,  267,  268,   41,   41,
  271,   29,  273,  256,  257,  136,   -1,  260,   -1,   -1,
   -1,  264,   -1,   -1,  267,  268,   -1,   -1,  271,   -1,
  273,  256,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,
   -1,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  267,  268,   -1,   -1,
   -1,   -1,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  274,   -1,
   -1,  275,  276,  277,  278,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=282;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE_INT","CTE_FLOAT","IF","THEN",
"ELSE","END_IF","OUT","FUNC","RETURN","INTEGER","FLOAT","CADENA",
"COMENTARIO_ML","WHILE","LOOP","PROC","ASSIGN","COMP_IGUAL","COMP_MENOR_IGUAL",
"COMP_MAYOR_IGUAL","DISTINTO","NA","TRUE","FALSE","SHADOWING",
};
final static String yyrule[] = {
"$accept : programa",
"programa : cuerpo",
"parametros : parametros ',' ID",
"parametros : ID",
"parametros : parametros ','",
"parametrosinvo : ID",
"parametrosinvo : ID ',' ID",
"parametrosinvo : ID ',' ID ',' ID",
"parametrosinvo : ID ','",
"parametrosinvo : ID ',' ID ','",
"tipo : INTEGER",
"tipo : FLOAT",
"cuerpo : cuerpo declarativa",
"cuerpo : declarativa",
"cuerpo : cuerpo ejecutable",
"cuerpo : ejecutable",
"cuerpowhile : cuerpowhile ejecutable",
"cuerpowhile : ejecutable",
"declarativa : procedure",
"declarativa : declaracion",
"procedure : proc procdef '{' cuerpo '}'",
"proc : PROC ID",
"procdef : '(' lp ')'",
"procdef : '(' lp ')' NA '=' factor ',' SHADOWING '=' boolean",
"procdef : '(' ')'",
"procdef : '(' ')' NA '=' factor ',' SHADOWING '=' boolean",
"ejecutable : asignacion",
"ejecutable : salida",
"ejecutable : seleccion",
"ejecutable : invocacion",
"ejecutable : control",
"ejecutable : error ';'",
"declaracion : tipo parametros ';'",
"control : WHILE '(' condicion ')' LOOP '{' cuerpowhile '}' ';'",
"invocacion : identificadorFuncion parametrosinvo ')' ';'",
"invocacion : ID '(' ')' ';'",
"identificadorFuncion : ID '('",
"salida : OUT '(' CADENA ')' ';'",
"seleccion : ifcondi cuerpoif ELSE '{' cuerpo '}' END_IF ';'",
"seleccion : ifcondi cuerpoif END_IF ';'",
"cuerpoif : '{' cuerpo '}'",
"ifcondi : IF '(' condicion ')'",
"condicion : expresion comparador expresion",
"comparador : COMP_IGUAL",
"comparador : COMP_MENOR_IGUAL",
"comparador : COMP_MAYOR_IGUAL",
"comparador : DISTINTO",
"comparador : '<'",
"comparador : '>'",
"lp : tipo ID",
"lp : tipo ID ',' tipo ID",
"lp : tipo ID ',' tipo ID ',' tipo ID",
"lp : tipo ID ','",
"lp : tipo ID ',' tipo ID ','",
"asignacion : ID ASSIGN expresion ';'",
"asignacion : tipo ID ASSIGN expresion ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE_INT",
"factor : '-' CTE_INT",
"factor : CTE_FLOAT",
"factor : '-' CTE_FLOAT",
"boolean : TRUE",
"boolean : FALSE",
};

//#line 318 "gramatica.y"
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
//#line 783 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 19 "gramatica.y"
{agregarEstructura("Se termino de compilar el programa correctamente");}
break;
case 2:
//#line 22 "gramatica.y"
{if(redeclarable(val_peek(0).sval)){
									if (setTipo(val_peek(0).sval, lastTipo.toString()))
										addAtributo(val_peek(0).sval,"Uso", "nombre de variable");
								}else{
									String var = this.getNombreVariable(val_peek(0).sval + this.ambito.toString());
									if (var == null){
										if(setTipo(val_peek(0).sval, lastTipo.toString()))
											addAtributo(val_peek(0).sval,"Uso", "nombre de variable");
									}else
										yyerror("No se permite redeclarar la variable por shadowing");
								}}
break;
case 3:
//#line 33 "gramatica.y"
{if(redeclarable(val_peek(0).sval)){
									if (setTipo(val_peek(0).sval, lastTipo.toString()))
										addAtributo(val_peek(0).sval,"Uso", "nombre de variable");
								}else{
									String var = this.getNombreVariable(val_peek(0).sval + this.ambito.toString());
									if (var == null){
										if(setTipo(val_peek(0).sval, lastTipo.toString()))
											addAtributo(val_peek(0).sval,"Uso", "nombre de variable");
									}else
										yyerror("No se permite redeclarar la variable por shadowing");
								}}
break;
case 4:
//#line 43 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 5:
//#line 46 "gramatica.y"
{ validarDefinicion(val_peek(0).sval);
					validarTipo(val_peek(0).sval,1);
			}
break;
case 6:
//#line 49 "gramatica.y"
{ validarDefinicion(val_peek(2).sval);
						validarDefinicion(val_peek(0).sval);
						validarTipo(val_peek(2).sval,1);
						validarTipo(val_peek(0).sval,2);
						}
break;
case 7:
//#line 54 "gramatica.y"
{
						validarDefinicion(val_peek(4).sval);
						validarDefinicion(val_peek(2).sval);
						validarDefinicion(val_peek(0).sval);
						validarTipo(val_peek(4).sval,1);
						validarTipo(val_peek(2).sval,2);
						validarTipo(val_peek(0).sval,3);
						}
break;
case 8:
//#line 62 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");
					 validarDefinicion(val_peek(1).sval);
					 validarTipo(val_peek(1).sval,1);
					 }
break;
case 9:
//#line 66 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");
							 validarDefinicion(val_peek(3).sval);
							validarDefinicion(val_peek(1).sval);
							validarTipo(val_peek(3).sval,1);
							validarTipo(val_peek(1).sval,2);
							}
break;
case 10:
//#line 76 "gramatica.y"
{this.lastTipo = new StringBuffer(val_peek(0).sval);}
break;
case 11:
//#line 77 "gramatica.y"
{this.lastTipo = new StringBuffer(val_peek(0).sval);}
break;
case 20:
//#line 98 "gramatica.y"
{agregarEstructura("PROCEDURE en linea " + val_peek(4).ival + " hasta linea " + val_peek(0).ival);
															reducirAmbito();}
break;
case 21:
//#line 103 "gramatica.y"
{incrementarAmbito(val_peek(0).sval);
				this.ultProc = new StringBuffer(val_peek(0).sval);
				addAtributoPDec(this.ultProc.toString(),"Uso", "nombre de procedimiento");}
break;
case 23:
//#line 109 "gramatica.y"
{addAtributoP( this.ultProc.toString(),"NA", val_peek(4).sval);
																addAtributoP( this.ultProc.toString(),"SHADOWING", val_peek(0).sval);}
break;
case 25:
//#line 112 "gramatica.y"
{addAtributoP(this.ultProc.toString(),"NA", val_peek(4).sval);
																addAtributoP(this.ultProc.toString(),"SHADOWING", val_peek(0).sval);}
break;
case 31:
//#line 122 "gramatica.y"
{yyerror(val_peek(0).ival,"Error sintactico en sentencia ejecutable");}
break;
case 32:
//#line 125 "gramatica.y"
{agregarEstructura("SENTENCIA DECLARATIVA en linea " + val_peek(2).ival + " hasta linea " + val_peek(0).ival);}
break;
case 33:
//#line 129 "gramatica.y"
{agregarEstructura("SENTENCIA DE CONTROL en linea " + val_peek(8).ival);}
break;
case 34:
//#line 132 "gramatica.y"
{agregarEstructura("SENTENCIA DE INVOCACION en linea " + val_peek(3).ival);
											
											}
break;
case 35:
//#line 135 "gramatica.y"
{agregarEstructura("SENTENCIA DE INVOCACION en linea " + val_peek(3).ival);}
break;
case 36:
//#line 139 "gramatica.y"
{ultProc = new StringBuffer(val_peek(1).sval); }
break;
case 37:
//#line 142 "gramatica.y"
{agregarEstructura("SENTENCIA DE SALIDA en linea: " + val_peek(4).ival);}
break;
case 38:
//#line 145 "gramatica.y"
{Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
																tInc.setOp1("["+(Terceto.id)+"]");}
break;
case 39:
//#line 147 "gramatica.y"
{Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
											tInc.setOp1("["+(Terceto.id)+"]");}
break;
case 40:
//#line 151 "gramatica.y"
{ Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
							tInc.setOp2("["+(Terceto.id+1)+"]");
							Terceto t = new Terceto("[?]",null,"BI",null);
							tercetos.add(t);
							colaTercetos.add(t);

}
break;
case 41:
//#line 160 "gramatica.y"
{Terceto t = new Terceto(val_peek(1).sval,"[?]","BF", null);
								tercetos.add(t);
								colaTercetos.add(t);}
break;
case 42:
//#line 164 "gramatica.y"
{yyval.sval = "["+agregarTerceto(val_peek(2).sval,val_peek(0).sval,val_peek(1).sval).toString()+"]";}
break;
case 43:
//#line 167 "gramatica.y"
{yyval.sval = "==";}
break;
case 44:
//#line 168 "gramatica.y"
{yyval.sval = "<=";}
break;
case 45:
//#line 169 "gramatica.y"
{yyval.sval = ">=";}
break;
case 46:
//#line 170 "gramatica.y"
{yyval.sval = "!=";}
break;
case 47:
//#line 171 "gramatica.y"
{yyval.sval = "<";}
break;
case 48:
//#line 172 "gramatica.y"
{yyval.sval = ">";}
break;
case 49:
//#line 175 "gramatica.y"
{if (setTipo(val_peek(0).sval, val_peek(1).sval))
						addAtributoP(this.ultProc.toString(), "param1", val_peek(1).sval);
					addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");}
break;
case 50:
//#line 178 "gramatica.y"
{ if(setTipo(val_peek(3).sval, val_peek(4).sval))
										addAtributo(val_peek(3).sval,"Uso", "nombre de parametro");
									if(setTipo(val_peek(0).sval, val_peek(1).sval))
										addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", val_peek(4).sval);
									addAtributoP(this.ultProc.toString(), "param2", val_peek(1).sval);
									}
break;
case 51:
//#line 185 "gramatica.y"
{ 
									if(setTipo(val_peek(6).sval, val_peek(7).sval))
										addAtributo(val_peek(6).sval,"Uso", "nombre de parametro");
									if(setTipo(val_peek(3).sval, val_peek(4).sval))
										addAtributo(val_peek(3).sval,"Uso", "nombre de parametro");
									if (setTipo(val_peek(0).sval, val_peek(1).sval))
										addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", val_peek(7).sval);
									addAtributoP(this.ultProc.toString(), "param2", val_peek(4).sval);
									addAtributoP(this.ultProc.toString(), "param3", val_peek(1).sval);
									}
break;
case 52:
//#line 196 "gramatica.y"
{yyerror(val_peek(1).ival,"Parametro faltante luego de la ',' ");
							if(setTipo(val_peek(1).sval, val_peek(2).sval))
								addAtributo(val_peek(1).sval,"Uso", "nombre de parametro");
							addAtributoP(this.ultProc.toString(), "param1", val_peek(2).sval);
							}
break;
case 53:
//#line 201 "gramatica.y"
{yyerror(val_peek(2).ival,"Parametro faltante luego de la ',' ");
									if (setTipo(val_peek(4).sval, val_peek(5).sval))
										addAtributo(val_peek(4).sval,"Uso", "nombre de parametro");
									if (setTipo(val_peek(1).sval, val_peek(2).sval))
										addAtributo(val_peek(1).sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", val_peek(5).sval);
									addAtributoP(this.ultProc.toString(), "param2", val_peek(2).sval);
									}
break;
case 54:
//#line 213 "gramatica.y"
{ if (validarDefinicion(val_peek(3).sval))
											agregarTerceto(val_peek(3).sval,val_peek(1).sval,":=");
									agregarEstructura("SENTENCIA DE ASIGNACION en linea " + val_peek(3).ival + " hasta linea " + val_peek(0).ival);}
break;
case 55:
//#line 216 "gramatica.y"
{
								if(redeclarable(val_peek(3).sval)){
									if (setTipo(val_peek(3).sval, lastTipo.toString())){
										addAtributo(val_peek(3).sval,"Uso", "nombre de variable");
										agregarTerceto(val_peek(3).sval,val_peek(1).sval,":=");
									}
								}else{
									String var = this.getNombreVariable(val_peek(3).sval + this.ambito.toString());
									if (var == null){
										if(setTipo(val_peek(3).sval, lastTipo.toString())){
											addAtributo(val_peek(3).sval,"Uso", "nombre de variable");
											agregarTerceto(val_peek(3).sval,val_peek(1).sval,":=");
										}
									}else
										yyerror("No se permite redeclarar la variable por shadowing");
								}}
break;
case 56:
//#line 237 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"+").toString()+"]";}
break;
case 57:
//#line 238 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"-").toString()+"]";}
break;
case 59:
//#line 242 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"*").toString()+"]";}
break;
case 60:
//#line 243 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"/").toString()+"]";}
break;
case 62:
//#line 247 "gramatica.y"
{validarDefinicion(val_peek(0).sval);}
break;
case 63:
//#line 248 "gramatica.y"
{ 
				 	Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
					int cant = 0;
					if (val_peek(0).sval.equals("32768")){
						yyerrorLex("Constante positiva fuera de rango");
						hs = tSimbolos.get(val_peek(0).sval);
						cant = (int)hs.get("Referencias").getValue();
						hs.get("Referencias").setValue(cant--);
						if ((int)hs.get("Referencias").getValue() == 0)
							tSimbolos.remove(val_peek(0).sval);
						val_peek(0).sval="32767";
						if (!tSimbolos.containsKey(val_peek(0).sval)){
							hs = new Hashtable<String, Atributo>();
							Atributo cantR = new Atributo("Referencias", 1);
							hs.put(cantR.getNombre(), cantR);
							tSimbolos.put(val_peek(0).sval, hs);
						}
						else{ 
							hs = tSimbolos.get(val_peek(0).sval);
							cant = (int)hs.get("Referencias").getValue();
							hs.get("Referencias").setValue(cant++);
						}
 				}}
break;
case 64:
//#line 272 "gramatica.y"
{	
							Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
							int cant = 0;
							if (!tSimbolos.containsKey("-" +val_peek(0).sval)){
								Atributo cantR = new Atributo("Referencias", 1);
								hs.put(cantR.getNombre(), cantR);
								tSimbolos.put("-" +val_peek(0).sval, hs);
							}
							else {
								hs = tSimbolos.get("-" + val_peek(0).sval);
								cant = (int)hs.get("Referencias").getValue();
								hs.get("Referencias").setValue(cant++);
							}
							hs = tSimbolos.get(val_peek(0).sval);
							cant = (int)hs.get("Referencias").getValue();
							hs.get("Referencias").setValue(cant--);
							yyval.sval = "-" +val_peek(0).sval;
							if ((int)tSimbolos.get(val_peek(0).sval).get("Referencias").getValue() == 0)
									tSimbolos.remove(val_peek(0).sval);}
break;
case 65:
//#line 291 "gramatica.y"
{}
break;
case 66:
//#line 292 "gramatica.y"
{	
							Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
							int cant = 0;
							if (!tSimbolos.containsKey("-" +val_peek(0).sval)){
								Atributo cantR = new Atributo("Referencias", 1);
								hs.put(cantR.getNombre(), cantR);
								tSimbolos.put("-" +val_peek(0).sval, hs);
							}
							else {
								hs = tSimbolos.get("-" +val_peek(0).sval);
								cant = (int)hs.get("Referencias").getValue();
								hs.get("Referencias").setValue(cant++);
							}
							hs = tSimbolos.get(val_peek(0).sval);
							cant = (int)hs.get("Referencias").getValue();
							hs.get("Referencias").setValue(cant--);
							yyval.sval = "-" + val_peek(0).sval;
							if ((int)tSimbolos.get(val_peek(0).sval).get("Referencias").getValue() == 0)
									tSimbolos.remove(val_peek(0).sval);}
break;
//#line 1290 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
