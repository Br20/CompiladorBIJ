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
import java.util.Hashtable;
import javafx.util.Pair;
import java.io.*;
import java.util.StringTokenizer;
//#line 24 "Parser.java"




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
    8,    8,    8,    6,    6,    6,    6,    6,    6,    9,
   17,   16,   16,   14,   15,   15,   18,   20,   20,   20,
   20,   20,   20,   10,   10,   10,   10,   10,   13,   13,
   19,   19,   19,   21,   21,   21,   11,   11,   11,   11,
   11,   12,   12,
};
final static short yylen[] = {                            2,
    1,    3,    1,    2,    1,    3,    5,    2,    4,    1,
    1,    2,    1,    2,    1,    2,    1,    1,    1,    8,
   15,    7,   14,    1,    1,    1,    1,    1,    2,    3,
    9,    5,    4,    5,   13,    9,    3,    1,    1,    1,
    1,    1,    1,    2,    5,    8,    3,    6,    4,    5,
    3,    3,    1,    3,    3,    1,    1,    1,    2,    1,
    2,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   10,   11,    0,    0,    0,    0,
    0,   13,   15,   18,   19,   24,   25,   26,   27,   28,
   29,    0,    0,    0,    0,    0,    0,   12,   14,    0,
    0,   57,   58,   60,    0,   56,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   30,   59,
   61,   49,    0,    0,    0,    0,    0,   33,    0,    0,
   38,   39,   40,   41,   42,   43,    0,    0,    0,    0,
    0,    0,    0,    2,    0,    0,   54,   55,    0,   32,
    0,    0,   34,    0,    0,    0,    0,    0,   50,    0,
    0,    0,    0,    0,    0,    0,    0,    7,    0,    0,
   17,    0,    0,   22,    0,    0,    0,    0,    0,    0,
    0,   16,    0,    0,    0,   20,    0,   36,   31,    0,
    0,    0,    0,    0,    0,    0,    0,   62,   63,    0,
   46,    0,    0,    0,    0,   35,    0,    0,   23,    0,
   21,
};
final static short yydgoto[] = {                          9,
   10,   31,   41,   11,   12,   13,  102,   14,   15,   72,
   36,  130,   16,   17,   18,   19,   20,   42,   43,   67,
   38,
};
final static short yysindex[] = {                      -181,
  -29,  -25,   17,   20,    0,    0,   25, -211,    0, -181,
 -186,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -37,  -38,  -37, -180,  -37,   51,    0,    0, -173,
  -11,    0,    0,    0, -206,    0,   -1,    7,   49,   35,
   61,   63,  -23,   64,   68,  -40,  -37, -146,    0,    0,
    0,    0,  -37,  -37,  -37,  -37, -145,    0,   55,   -7,
    0,    0,    0,    0,    0,    0,  -37,   56, -155, -117,
 -139,   78,    2,    0,    7,    7,    0,    0,   76,    0,
 -181,   21,    0,   -2,   62, -181,   80, -112,    0, -130,
 -108, -161,  -37,  -87, -200,   67, -181,    0, -189, -127,
    0,   57,   87,    0, -125,  -37,  -69,   10,   75, -173,
   77,    0, -147,   93,   95,    0, -181,    0,    0,   79,
 -200, -141,    1, -203, -115,   84, -116,    0,    0,   27,
    0, -203,   92, -181,   30,    0,   19, -181,    0,   39,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  155,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -8,
    0,    0,    0,    0,    0,    0,    0,  -41,  116,    0,
    0,    0,    0,    0,    0,    0,    0,   -4,    0,    0,
    0,    0,    0,    0,    0,    0,  117,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -36,  -31,    0,    0,  120,    0,
    0,  125,    0,    0,    0,    0,  131,    0,    0,  133,
    0,    0,    0,    0,  134,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  135,    0,    0,    0,    0,    0,    0,
  137,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  -54,    0,    0,  -33,   31,    6,    0,    0,    0,    0,
  -21,   47,    0,    0,    0,    0,    0,  157,    3,    0,
   28,
};
final static int YYTABLESIZE=328;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   70,   53,   40,   53,   51,   86,   51,   35,   51,   52,
   97,   52,   71,   52,   23,   29,   99,   53,   53,   53,
   53,   54,   51,   51,   37,   51,   91,   52,   52,   21,
   52,   94,   48,   77,   78,    3,   65,  104,   66,    4,
   28,   53,  107,   54,   53,   27,   54,   49,   55,   73,
    3,   50,   51,   56,    4,  116,   24,   52,  100,   25,
   89,  105,  123,   53,   26,   54,    5,    6,  100,   82,
   30,  103,  108,  109,    1,    2,  128,  129,    3,  137,
   75,   76,    4,  140,  115,    5,    6,  125,   44,    7,
   46,    8,   57,   58,    1,    2,   29,  101,    3,   29,
   47,   59,    4,   60,   68,    5,    6,  112,   69,    7,
   74,   79,   29,   80,   83,   81,   84,   87,   88,   90,
   92,   28,   93,   95,   28,  127,   98,  106,   29,  110,
  113,  114,  117,  118,  120,  119,  121,   28,  122,  124,
  126,  131,   29,  139,  132,   29,  133,    1,    2,  134,
  136,    3,  138,   28,    1,    4,    5,    8,    5,    6,
    6,   85,    7,  141,    8,   37,   96,   28,    1,    2,
   28,   44,    3,    9,   47,   45,    4,   48,  135,    5,
    6,  111,   45,    7,    0,    8,    1,    2,    0,    0,
    3,    0,    0,    0,    4,    0,    0,    5,    6,    0,
    0,    7,    0,    8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   39,   32,
   33,   34,    0,    0,    0,    0,    5,    6,    0,    0,
    0,    0,    0,   53,   53,   53,   53,    0,   51,   51,
   51,   51,    0,   52,   52,   52,   52,    0,   22,    0,
    0,   61,   62,   63,   64,    0,    1,    2,    0,    0,
    3,    0,    0,    0,    4,    0,    0,    5,    6,    0,
    0,    7,    0,    8,    1,    2,    0,    0,    3,    0,
    0,    0,    4,    0,    0,    5,    6,    0,    0,    7,
    0,    8,    0,    0,    1,    2,    0,    0,    3,    0,
    0,    0,    4,    0,    0,    5,    6,    0,    0,    7,
    0,    8,    1,    2,    0,    0,    3,    0,    0,    0,
    4,    0,    0,    5,    6,    0,    0,    7,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   41,  123,   43,   45,   45,   41,
  123,   43,   46,   45,   40,   10,  125,   59,   60,   43,
   62,   45,   59,   60,   22,   62,   81,   59,   60,   59,
   62,   86,   44,   55,   56,   44,   60,  125,   62,   44,
   10,   43,   97,   45,   43,  257,   45,   59,   42,   47,
   59,  258,  259,   47,   59,  125,   40,   59,   92,   40,
   59,   95,  117,   43,   40,   45,  267,  268,  102,   67,
  257,   93,  262,  263,  256,  257,  280,  281,  260,  134,
   53,   54,  264,  138,  106,  267,  268,  121,  269,  271,
   40,  273,   44,   59,  256,  257,   91,   92,  260,   94,
  274,   41,  264,   41,   41,  267,  268,  102,   41,  271,
  257,  257,  107,   59,   59,  123,  272,  257,   41,   44,
  123,   91,   61,   44,   94,  125,  257,   61,  123,  257,
   44,  257,  123,   59,  282,   59,   44,  107,   44,   61,
  282,  257,  137,  125,   61,  140,  263,  256,  257,  123,
   59,  260,  123,  123,    0,  264,   41,   41,  267,  268,
   41,  279,  271,  125,  273,   41,  279,  137,  256,  257,
  140,   41,  260,   41,   41,   41,  264,   41,  132,  267,
  268,  125,   26,  271,   -1,  273,  256,  257,   -1,   -1,
  260,   -1,   -1,   -1,  264,   -1,   -1,  267,  268,   -1,
   -1,  271,   -1,  273,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,
  258,  259,   -1,   -1,   -1,   -1,  267,  268,   -1,   -1,
   -1,   -1,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  274,   -1,
   -1,  275,  276,  277,  278,   -1,  256,  257,   -1,   -1,
  260,   -1,   -1,   -1,  264,   -1,   -1,  267,  268,   -1,
   -1,  271,   -1,  273,  256,  257,   -1,   -1,  260,   -1,
   -1,   -1,  264,   -1,   -1,  267,  268,   -1,   -1,  271,
   -1,  273,   -1,   -1,  256,  257,   -1,   -1,  260,   -1,
   -1,   -1,  264,   -1,   -1,  267,  268,   -1,   -1,  271,
   -1,  273,  256,  257,   -1,   -1,  260,   -1,   -1,   -1,
  264,   -1,   -1,  267,  268,   -1,   -1,  271,
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
"procedure : PROC ID '(' lp ')' '{' cuerpo '}'",
"procedure : PROC ID '(' lp ')' NA '=' factor ',' SHADOWING '=' boolean '{' cuerpo '}'",
"procedure : PROC ID '(' ')' '{' cuerpo '}'",
"procedure : PROC ID '(' ')' NA '=' factor ',' SHADOWING '=' boolean '{' cuerpo '}'",
"ejecutable : asignacion",
"ejecutable : salida",
"ejecutable : seleccion",
"ejecutable : invocacion",
"ejecutable : control",
"ejecutable : error ';'",
"declaracion : tipo parametros ';'",
"control : WHILE '(' condicion ')' LOOP '{' cuerpowhile '}' ';'",
"invocacion : ID '(' parametrosinvo ')' ';'",
"invocacion : ID '(' ')' ';'",
"salida : OUT '(' CADENA ')' ';'",
"seleccion : IF '(' condicion ')' '{' cuerpo '}' ELSE '{' cuerpo '}' END_IF ';'",
"seleccion : IF '(' condicion ')' '{' cuerpo '}' END_IF ';'",
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

//#line 195 "gramatica.y"
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
	
	private int yylex() {
		Par token = this.lexer.yylex();
		pwTo.println("Token entregado: " + token.getValue()); // agrega el token entregado al archivo de tokens
		yylval = new ParserVal(token.getKey());
		yylval.ival = nLinea; //se utiliza la variable ival de la clase ParserVal para guardar el numero de linea en el que se detecto el token
		if (token.getKey() != null) { // Si tiene lexema
			Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
			Atributo cantR = new Atributo("Referencias", 1);
			hs.put(cantR.getNombre(), cantR);
			if (!tSimbolos.containsKey(token.getKey())) { // Si no existe en tabla de simbolos se crea para ese lexema
				tSimbolos.put(token.getKey(), hs);
			} else {
				hs = tSimbolos.get(token.getKey());
				int cant = (int)hs.get("Referencias").getValue();
				hs.get("Referencias").setValue(cant++);
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
//#line 471 "Parser.java"
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
//#line 18 "gramatica.y"
{agregarEstructura("Se termino de compilar el programa correctamente");}
break;
case 4:
//#line 23 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 8:
//#line 29 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 9:
//#line 30 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 20:
//#line 57 "gramatica.y"
{agregarEstructura("PROCEDURE en linea " + val_peek(7).ival + " hasta linea " + val_peek(0).ival);}
break;
case 21:
//#line 58 "gramatica.y"
{agregarEstructura("PROCEDURE en linea " + val_peek(14).ival + " hasta linea " + val_peek(0).ival);}
break;
case 22:
//#line 59 "gramatica.y"
{agregarEstructura("PROCEDURE en linea " + val_peek(6).ival+ " hasta linea " + val_peek(0).ival);}
break;
case 23:
//#line 60 "gramatica.y"
{agregarEstructura("PROCEDURE en linea " + val_peek(13).ival + " hasta linea " + val_peek(0).ival);}
break;
case 29:
//#line 68 "gramatica.y"
{yyerror(val_peek(0).ival,"Error sintactico en sentencia ejecutable");}
break;
case 30:
//#line 71 "gramatica.y"
{agregarEstructura("SENTENCIA DECLARATIVA en linea " + val_peek(2).ival + " hasta linea " + val_peek(0).ival);
									System.out.println("Aca andan los paramentro pa: " + val_peek(1).sval +"  " + val_peek(2).sval);}
break;
case 31:
//#line 76 "gramatica.y"
{agregarEstructura("SENTENCIA DE CONTROL en linea " + val_peek(8).ival);}
break;
case 32:
//#line 79 "gramatica.y"
{agregarEstructura("SENTENCIA DE INVOCACION en linea " + val_peek(4).ival);}
break;
case 33:
//#line 80 "gramatica.y"
{agregarEstructura("SENTENCIA DE INVOCACION en linea " + val_peek(3).ival);}
break;
case 34:
//#line 84 "gramatica.y"
{agregarEstructura("SENTENCIA DE SALIDA en linea: " + val_peek(4).ival);}
break;
case 35:
//#line 87 "gramatica.y"
{agregarEstructura("SENTENCIA DE SELECCION en linea " + val_peek(12).ival + " hasta linea " + val_peek(4).ival);}
break;
case 36:
//#line 88 "gramatica.y"
{agregarEstructura("SENTENCIA DE SELECCION en linea " + val_peek(8).ival + " hasta linea " + val_peek(2).ival);}
break;
case 47:
//#line 106 "gramatica.y"
{yyerror(val_peek(1).ival,"Parametro faltante luego de la ',' ");}
break;
case 48:
//#line 107 "gramatica.y"
{yyerror(val_peek(2).ival,"Parametro faltante luego de la ',' ");}
break;
case 49:
//#line 112 "gramatica.y"
{agregarEstructura("SENTENCIA DE ASIGNACION en linea " + val_peek(3).ival + " hasta linea " + val_peek(0).ival);}
break;
case 50:
//#line 113 "gramatica.y"
{agregarEstructura("SENTENCIA DE ASIGNACION en linea " + val_peek(4).ival + " hasta linea " + val_peek(0).ival);}
break;
case 58:
//#line 127 "gramatica.y"
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
case 59:
//#line 151 "gramatica.y"
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
							if ((int)tSimbolos.get(val_peek(0).sval).get("Referencias").getValue() == 0)
									tSimbolos.remove(val_peek(0).sval);}
break;
case 60:
//#line 169 "gramatica.y"
{}
break;
case 61:
//#line 170 "gramatica.y"
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
							if ((int)tSimbolos.get(val_peek(0).sval).get("Referencias").getValue() == 0)
									tSimbolos.remove(val_peek(0).sval);}
break;
//#line 773 "Parser.java"
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
