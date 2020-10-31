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
   10,   11,   11,   11,   15,   11,    6,    6,    6,    6,
    6,    6,    9,   20,   19,   19,   17,   18,   18,   21,
   23,   23,   23,   23,   23,   23,   12,   12,   12,   12,
   12,   16,   16,   22,   22,   22,   24,   24,   24,   13,
   13,   13,   13,   13,   14,   14,
};
final static short yylen[] = {                            2,
    1,    3,    1,    2,    1,    3,    5,    2,    4,    1,
    1,    2,    1,    2,    1,    2,    1,    1,    1,    5,
    2,    3,   10,    2,    0,   10,    1,    1,    1,    1,
    1,    2,    3,    9,    5,    4,    5,   13,    9,    3,
    1,    1,    1,    1,    1,    1,    2,    5,    8,    3,
    6,    4,    5,    3,    3,    1,    3,    3,    1,    1,
    1,    2,    1,    2,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   10,   11,    0,    0,    0,    0,
    0,   13,   15,   18,   19,    0,   27,   28,   29,   30,
   31,   32,    0,    0,    0,    0,    0,   21,   12,   14,
    0,    0,    0,    0,   60,   61,   63,    0,   59,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   33,    0,    0,    0,    0,   62,   64,   52,    0,    0,
    0,    0,    0,   36,    0,    0,   41,   42,   43,   44,
   45,   46,    0,    0,    0,    0,    2,    0,    0,    0,
    0,    0,    0,   57,   58,    0,   35,    0,    0,   37,
    0,   53,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,    7,    0,    0,   17,    0,    0,    0,    0,
    0,    0,    0,    0,   16,    0,    0,    0,    0,   39,
   34,    0,    0,    0,    0,   65,   66,   25,   49,    0,
    0,   26,   23,    0,   38,
};
final static short yydgoto[] = {                          9,
   10,   32,   44,   11,   12,   13,  107,   14,   15,   16,
   34,   54,   39,  128,  132,   17,   18,   19,   20,   21,
   45,   46,   73,   41,
};
final static short yysindex[] = {                      -216,
  -42,  -25,   -5,   -2,    0,    0,    6, -215,    0, -216,
 -199,    0,    0,    0,    0,   29,    0,    0,    0,    0,
    0,    0,  -37,  -38,  -37, -207,  -37,    0,    0,    0,
 -189,   15,  -40,  -26,    0,    0,    0, -191,    0,   -9,
   18,   54,   43,   62,   64,  -23,   65,   66,  -37, -148,
    0, -169, -146,   71, -216,    0,    0,    0,  -37,  -37,
  -37,  -37, -144,    0,   56,   -7,    0,    0,    0,    0,
    0,    0,  -37,   58, -154,   11,    0,   59,   75, -158,
 -119,   18,   18,    0,    0,   78,    0, -216,    2,    0,
    7,    0,  -37, -196,   63,    0, -134, -100, -181,   81,
 -130,  -37,    0, -174, -128,    0,  -64, -151,   88,   89,
   12,   77, -189,   80,    0,   73, -196, -142, -216,    0,
    0, -188, -115,   82,  -82,    0,    0,    0,    0, -188,
 -117,    0,    0,   85,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  147,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   19,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,  109,    0,    0,    0,    0,    0,    0,    0,   22,
    0,   28,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  112,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  114,   36,
    0,  -36,  -31,    0,    0,  117,    0,    0,  120,    0,
    0,    0,    0,  121,    0,    0,  122,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  124,    0,
    0,    0,    0,    0,    0,    0,  125,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -39,    0,    0,   -3,    3,    1,    0,    0,    0,    0,
    0,    0,  -29,   39,    0,    0,    0,    0,    0,    0,
  143,    4,    0,   35,
};
final static int YYTABLESIZE=255;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         56,
   52,   56,   43,   56,   54,   96,   54,   38,   54,   55,
   30,   55,   29,   55,   24,   81,   22,   56,   56,   59,
   56,   60,   54,   54,  104,   54,   40,   55,   55,   53,
   55,   84,   85,   59,   25,   60,   71,   26,   72,    1,
    2,   28,  131,    3,   59,   27,   60,    4,   98,   58,
    5,    6,   76,   59,    7,   60,    8,   31,   50,   61,
  114,   47,    3,  100,   62,    4,   56,   57,   33,   92,
    5,    6,  110,   51,    1,    2,   89,    3,    3,  125,
    4,   30,    4,   29,   49,    5,    6,  111,  112,    7,
  101,  126,  127,   82,   83,  105,   55,   63,   30,  106,
   29,   64,   65,  105,   66,   74,   75,  115,   77,   78,
   79,   80,   86,  123,   87,   88,   90,   91,   94,   93,
   95,   97,  103,  102,  108,   30,  109,   29,  113,   99,
  116,  117,  118,  122,  119,  120,    1,    2,  121,  124,
    3,  129,  130,  135,    4,  134,    1,    5,    6,    5,
   24,    7,    8,    8,   47,    1,    2,    6,   22,    3,
   40,   50,    9,    4,   48,   51,    5,    6,  133,   48,
    7,    0,    8,    1,    2,    0,    0,    3,    0,    0,
    0,    4,    0,    0,    5,    6,    0,    0,    7,    0,
    8,    1,    2,    0,    0,    3,    0,    0,    0,    4,
    0,    0,    5,    6,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   42,   35,
   36,   37,    0,    0,    0,    0,    5,    6,    0,    0,
    0,    0,    0,   56,   56,   56,   56,    0,   54,   54,
   54,   54,    0,   55,   55,   55,   55,    0,   23,    0,
    0,   67,   68,   69,   70,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   41,  125,   43,   45,   45,   41,
   10,   43,   10,   45,   40,   55,   59,   59,   60,   43,
   62,   45,   59,   60,  125,   62,   23,   59,   60,   33,
   62,   61,   62,   43,   40,   45,   60,   40,   62,  256,
  257,  257,  125,  260,   43,   40,   45,  264,   88,   59,
  267,  268,   49,   43,  271,   45,  273,  257,   44,   42,
  125,  269,   44,   93,   47,   44,  258,  259,   40,   59,
  267,  268,  102,   59,  256,  257,   73,   59,  260,  119,
   59,   81,  264,   81,  274,  267,  268,  262,  263,  271,
   94,  280,  281,   59,   60,   99,  123,   44,   98,   99,
   98,   59,   41,  107,   41,   41,   41,  107,  257,  279,
  257,   41,  257,  117,   59,  123,   59,  272,   44,   61,
  279,   44,  257,   61,   44,  125,  257,  125,  257,  123,
  282,   44,   44,   61,  123,   59,  256,  257,   59,  282,
  260,  257,   61,   59,  264,  263,    0,  267,  268,   41,
  123,  271,   41,  273,   41,  256,  257,   41,  123,  260,
   41,   41,   41,  264,   41,   41,  267,  268,  130,   27,
  271,   -1,  273,  256,  257,   -1,   -1,  260,   -1,   -1,
   -1,  264,   -1,   -1,  267,  268,   -1,   -1,  271,   -1,
  273,  256,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,
   -1,   -1,  267,  268,   -1,   -1,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,
  258,  259,   -1,   -1,   -1,   -1,  267,  268,   -1,   -1,
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
"$$1 :",
"procdef : '(' ')' NA '=' factor ',' SHADOWING '=' boolean $$1",
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

//#line 227 "gramatica.y"
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
			System.out.println("Se esta intentando cambiar un atributo");
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
//#line 542 "Parser.java"
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
case 2:
//#line 21 "gramatica.y"
{setTipo(val_peek(0).sval, lastTipo.toString());
								addAtributo(val_peek(0).sval,"Uso", "nombre de variable");}
break;
case 3:
//#line 23 "gramatica.y"
{setTipo(val_peek(0).sval, lastTipo.toString());
				addAtributo(val_peek(0).sval,"Uso", "nombre de variable");}
break;
case 4:
//#line 25 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 8:
//#line 31 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 9:
//#line 32 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 10:
//#line 37 "gramatica.y"
{this.lastTipo = new StringBuffer(val_peek(0).sval);}
break;
case 11:
//#line 38 "gramatica.y"
{this.lastTipo = new StringBuffer(val_peek(0).sval);}
break;
case 20:
//#line 59 "gramatica.y"
{agregarEstructura("PROCEDURE en linea " + val_peek(4).ival + " hasta linea " + val_peek(0).ival);
															reducirAmbito();}
break;
case 21:
//#line 64 "gramatica.y"
{incrementarAmbito(val_peek(0).sval);
				this.ultProc = new StringBuffer(val_peek(0).sval);
				addAtributoPDec(this.ultProc.toString(),"Uso", "nombre de procedimiento");}
break;
case 23:
//#line 70 "gramatica.y"
{addAtributoP( this.ultProc.toString(),"NA", val_peek(4).sval);
																addAtributoP( this.ultProc.toString(),"SHADOWING", val_peek(0).sval);}
break;
case 25:
//#line 73 "gramatica.y"
{}
break;
case 26:
//#line 73 "gramatica.y"
{addAtributoP(this.ultProc.toString(),"NA", val_peek(5).sval);
																addAtributoP(this.ultProc.toString(),"SHADOWING", val_peek(1).sval);}
break;
case 32:
//#line 83 "gramatica.y"
{yyerror(val_peek(0).ival,"Error sintactico en sentencia ejecutable");}
break;
case 33:
//#line 86 "gramatica.y"
{agregarEstructura("SENTENCIA DECLARATIVA en linea " + val_peek(2).ival + " hasta linea " + val_peek(0).ival);
									System.out.println("Aca andan los paramentro pa: " + val_peek(1).sval +"  " + val_peek(2).sval);}
break;
case 34:
//#line 91 "gramatica.y"
{agregarEstructura("SENTENCIA DE CONTROL en linea " + val_peek(8).ival);}
break;
case 35:
//#line 94 "gramatica.y"
{agregarEstructura("SENTENCIA DE INVOCACION en linea " + val_peek(4).ival);}
break;
case 36:
//#line 95 "gramatica.y"
{agregarEstructura("SENTENCIA DE INVOCACION en linea " + val_peek(3).ival);}
break;
case 37:
//#line 99 "gramatica.y"
{agregarEstructura("SENTENCIA DE SALIDA en linea: " + val_peek(4).ival);}
break;
case 38:
//#line 102 "gramatica.y"
{agregarEstructura("SENTENCIA DE SELECCION en linea " + val_peek(12).ival + " hasta linea " + val_peek(4).ival);}
break;
case 39:
//#line 103 "gramatica.y"
{agregarEstructura("SENTENCIA DE SELECCION en linea " + val_peek(8).ival + " hasta linea " + val_peek(2).ival);}
break;
case 47:
//#line 118 "gramatica.y"
{setTipo(val_peek(0).sval, val_peek(1).sval);
					addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");}
break;
case 48:
//#line 120 "gramatica.y"
{ setTipo(val_peek(3).sval, val_peek(4).sval);
									setTipo(val_peek(0).sval, val_peek(1).sval);
									addAtributo(val_peek(3).sval,"Uso", "nombre de parametro");
									addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");
									}
break;
case 49:
//#line 125 "gramatica.y"
{ setTipo(val_peek(6).sval, val_peek(7).sval);
									setTipo(val_peek(3).sval, val_peek(4).sval);
									setTipo(val_peek(0).sval, val_peek(1).sval);
									addAtributo(val_peek(6).sval,"Uso", "nombre de parametro");
									addAtributo(val_peek(3).sval,"Uso", "nombre de parametro");
									addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");
									}
break;
case 50:
//#line 132 "gramatica.y"
{yyerror(val_peek(1).ival,"Parametro faltante luego de la ',' ");
							setTipo(val_peek(1).sval, val_peek(2).sval);
							addAtributo(val_peek(1).sval,"Uso", "nombre de parametro");}
break;
case 51:
//#line 135 "gramatica.y"
{yyerror(val_peek(2).ival,"Parametro faltante luego de la ',' ");
									setTipo(val_peek(4).sval, val_peek(5).sval);
									setTipo(val_peek(1).sval, val_peek(2).sval);
									addAtributo(val_peek(4).sval,"Uso", "nombre de parametro");
									addAtributo(val_peek(1).sval,"Uso", "nombre de parametro");}
break;
case 52:
//#line 144 "gramatica.y"
{agregarEstructura("SENTENCIA DE ASIGNACION en linea " + val_peek(3).ival + " hasta linea " + val_peek(0).ival);}
break;
case 53:
//#line 145 "gramatica.y"
{agregarEstructura("SENTENCIA DE ASIGNACION en linea " + val_peek(4).ival + " hasta linea " + val_peek(0).ival);}
break;
case 61:
//#line 159 "gramatica.y"
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
case 62:
//#line 183 "gramatica.y"
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
case 63:
//#line 201 "gramatica.y"
{}
break;
case 64:
//#line 202 "gramatica.y"
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
//#line 900 "Parser.java"
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
