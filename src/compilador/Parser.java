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
    6,    9,   19,   21,   20,   18,   18,   23,   16,   17,
   17,   25,   24,   22,   27,   27,   27,   27,   27,   27,
   12,   12,   12,   12,   12,   15,   15,   26,   26,   26,
   28,   28,   28,   13,   13,   13,   13,   13,   14,   14,
};
final static short yylen[] = {                            2,
    1,    3,    1,    2,    1,    3,    5,    2,    4,    1,
    1,    2,    1,    2,    1,    2,    1,    1,    1,    5,
    2,    3,   10,    2,    9,    1,    1,    1,    1,    1,
    2,    3,    6,    1,    4,    4,    3,    2,    5,    8,
    4,    3,    4,    3,    1,    1,    1,    1,    1,    1,
    2,    5,    8,    3,    6,    4,    5,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    1,    2,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   10,   11,   34,    0,    0,    0,
    0,   13,   15,   18,   19,    0,   26,   27,   28,   29,
   30,    0,    0,    0,    0,   31,    0,   38,    0,    0,
   21,   12,   14,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,   65,   67,    0,   63,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,    0,
    0,    0,    0,    0,   37,    0,    0,    0,    0,   66,
   68,   56,    0,    0,    0,    0,   43,   45,   46,   47,
   48,   49,   50,    0,    0,    0,    2,    0,    0,    0,
    0,    0,   17,    0,   35,    0,   36,   42,    0,   41,
    0,    0,   61,   62,    0,   39,   57,    0,    0,    0,
   20,    0,    0,   16,    0,    0,    0,    0,    0,   33,
    7,    0,    0,    0,    0,    0,    0,    0,    0,   40,
    0,    0,    0,   69,   70,   25,   53,    0,   23,
};
final static short yydgoto[] = {                          9,
   10,   35,   42,   11,   12,   13,   94,   14,   15,   16,
   37,   60,   49,  136,   17,   18,   19,   20,   21,   22,
   23,   52,   24,   25,   44,   53,   84,   51,
};
final static short yysindex[] = {                      -216,
  -32,  -25,   -4,    6,    0,    0,    0, -207,    0, -216,
 -190,    0,    0,    0,    0,   31,    0,    0,    0,    0,
    0, -197,   41,  -38,  -33,    0,  -37,    0,  -37, -176,
    0,    0,    0, -178,  -14,  -40,  -22,  -19,  -37,   53,
   44,   59, -216, -203,    0,    0,    0, -180,    0,   13,
   35,   64,  -23,   66,  -37, -146,    0, -167, -143,   74,
 -216, -158,   75, -139,    0,   61, -119,   -2,   63,    0,
    0,    0,  -37,  -37,  -37,  -37,    0,    0,    0,    0,
    0,    0,    0,  -37,   65,   21,    0,   62,   81, -153,
 -100, -130,    0,  -64,    0,   84,    0,    0, -216,    0,
   35,   35,    0,    0,    4,    0,    0,  -37, -184,   68,
    0, -178,   71,    0, -126,  -82,   88, -124,  -37,    0,
    0, -129, -147,   92,   95,   83,   79, -184, -138,    0,
 -195, -114,   85,    0,    0,    0,    0, -195,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  147,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -6,    0,    0,    0,    0,    0,  109,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,   10,    0,   28,    0,    0,
    0,    0,    0,  112,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  114,   36,
    0,    0,    0,    0,    0,  117,    0,    0,    0,    0,
  -36,  -31,    0,    0,  120,    0,    0,    0,  121,    0,
    0,    0,    0,    0,  122,    0,    0,    0,    0,    0,
    0,    0,    0,  124,    0,    0,    0,  125,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -26,    0,    0,  -20,    3,    1,    0,    0,    0,    0,
    0,    0,  -43,   32,    0,    0,    0,    0,    0,    0,
    0,  130,    0,    0,    0,    7,    0,   14,
};
final static int YYTABLESIZE=255;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   58,   60,   41,   60,   58,   98,   58,   48,   58,   59,
   33,   59,   32,   59,   28,   59,   67,   60,   60,   73,
   60,   74,   58,   58,  111,   58,   26,   59,   59,   56,
   59,  103,  104,   50,   91,   29,   82,    3,   83,    1,
    2,   92,  122,    3,   57,   30,   73,    4,   74,   31,
    5,    6,    3,    4,    7,   73,    8,   74,   68,   69,
  113,   86,   93,   73,  117,   74,   34,   33,    4,   32,
   36,   72,  116,   92,   38,  125,   75,   70,   71,  107,
   39,   76,    5,    6,  134,  135,  101,  102,  118,   43,
  105,   33,   54,   32,  114,   55,   64,    1,    2,   66,
   61,    3,   65,   62,   77,    4,   85,  132,    5,    6,
   87,   88,    7,   89,   90,   95,   33,   96,   32,   97,
   99,  100,  108,  106,  109,  110,  112,  115,  119,  120,
  121,  123,  124,  126,  127,  128,    1,    2,  129,  131,
    3,  130,  137,  133,    4,  138,    1,    5,    6,    5,
   24,    7,    8,    8,   51,    1,    2,    6,   22,    3,
   44,   54,    9,    4,   52,   55,    5,    6,   63,  139,
    7,    0,    8,    1,    2,    0,    0,    3,    0,    0,
    0,    4,    0,    0,    5,    6,    0,    0,    7,    0,
    8,    1,    2,    0,    0,    3,    0,    0,    0,    4,
    0,    0,    5,    6,    0,    0,    7,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   40,   45,
   46,   47,    0,    0,    0,    0,    5,    6,    0,    0,
    0,    0,    0,   60,   60,   60,   60,    0,   58,   58,
   58,   58,    0,   59,   59,   59,   59,    0,   27,    0,
    0,   78,   79,   80,   81,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   41,  125,   43,   45,   45,   41,
   10,   43,   10,   45,   40,   36,   43,   59,   60,   43,
   62,   45,   59,   60,  125,   62,   59,   59,   60,   44,
   62,   75,   76,   27,   61,   40,   60,   44,   62,  256,
  257,   62,  125,  260,   59,   40,   43,  264,   45,  257,
  267,  268,   59,   44,  271,   43,  273,   45,  262,  263,
  125,   55,   62,   43,  108,   45,  257,   67,   59,   67,
   40,   59,   99,   94,  272,  119,   42,  258,  259,   59,
   40,   47,  267,  268,  280,  281,   73,   74,  109,  123,
   84,   91,  269,   91,   94,  274,   44,  256,  257,   41,
  123,  260,   59,  123,   41,  264,   41,  128,  267,  268,
  257,  279,  271,  257,   41,   41,  116,  257,  116,   59,
  123,   59,   61,   59,   44,  279,  257,   44,   61,   59,
  257,   44,  257,  263,  282,   44,  256,  257,   44,   61,
  260,   59,  257,  282,  264,   61,    0,  267,  268,   41,
  123,  271,   41,  273,   41,  256,  257,   41,  123,  260,
   41,   41,   41,  264,   41,   41,  267,  268,   39,  138,
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
"procdef : '(' ')' NA '=' factor ',' SHADOWING '=' boolean",
"ejecutable : asignacion",
"ejecutable : salida",
"ejecutable : seleccion",
"ejecutable : invocacion",
"ejecutable : control",
"ejecutable : error ';'",
"declaracion : tipo parametros ';'",
"control : condiWhile LOOP '{' cuerpowhile '}' ';'",
"while : WHILE",
"condiWhile : while '(' condicion ')'",
"invocacion : identificadorFuncion parametrosinvo ')' ';'",
"invocacion : identificadorFuncion ')' ';'",
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

//#line 348 "gramatica.y"
	private Lexico lexer;
	private int erroresS = 0;
	public static int nLinea = 1;
	public Hashtable<String, Hashtable<String, Atributo>> tSimbolos = new Hashtable<String, Hashtable<String, Atributo>>();
	private FileWriter txtErrores = null;
	private static PrintWriter pw = null;
	private FileWriter txtTabla = null;
	private static PrintWriter pwTa = null;
	private FileWriter txtTercetos = null;
	private static PrintWriter pwTe = null;
	private StringBuffer ambito = new StringBuffer(".p");
	private StringBuffer lastTipo = new StringBuffer();
	private StringBuffer ultProc = new StringBuffer();
	private List<Terceto> tercetos = new ArrayList<Terceto>();
	private List<Terceto> colaTercetos = new ArrayList<Terceto>();
	private int nroTerceto = 0;
	private int ultTercetoImp = 0;
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
		txtTabla = new FileWriter(path.substring(0, path.indexOf('.')) + "TablaS.txt");
		pwTa = new PrintWriter(txtTabla);
		txtTercetos = new FileWriter(path.substring(0, path.indexOf('.')) + "Tercetos.txt");
		pwTe = new PrintWriter(txtTercetos);
		escribirCodigoConLineas(path);
		return yyparse();
	}
	
	public boolean errores() {
		return (this.erroresS > 0);
	}
	
	public List<Terceto> getTercetos(){
		return this.tercetos;
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
		if (tercetos.size()> ultTercetoImp) {
			pwTe.println("\nTERCETOS DE " +this.ambito.substring(this.ambito.lastIndexOf(".")+1,this.ambito.length()).toUpperCase() + this.ambito.substring(0,this.ambito.lastIndexOf(".")).toUpperCase() + "\n");
		}
		for (int i= ultTercetoImp; i<tercetos.size();i++) {
			pwTe.println(tercetos.get(i).toString());
		}
		ultTercetoImp = tercetos.size();
	}


	public static void escribirError(String s) {
		pw.println(s);
	}

	public void cerrarFicheros() throws IOException {
		if (txtErrores != null)
			txtErrores.close();
		if (txtTabla != null)
			txtTabla.close();
		if (txtTercetos != null)
			txtTercetos.close();
	}

	public void escribirTablaS() {
		//escribe todos los datos que se tenga en la tabla de simbolos con la cantidad de referencias
		tSimbolos.forEach((k, v) -> {
			pwTa.println("Simbolo: " + k + " " + v);
		});
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
		if (!tSimbolos.containsKey(op1) ) {
			op1 = getNombreVariable(op1+this.ambito);
		}
		if (!tSimbolos.containsKey(op2)) {
			op2 = getNombreVariable(op2+this.ambito);
		}
		if (op1!=null && op2!=null) {
			String t1 = tSimbolos.get(op1).get("Tipo").getValue().toString();
			String t2 = tSimbolos.get(op2).get("Tipo").getValue().toString();
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
		Terceto t = new Terceto(op1,op2,operador,null);
		if (tipo!=null) {
			if (tipo.equals("X")) {
				t.setTipo("X");
				yyerror("Incompatibilidad de tipo entre los operandos, no se puede asignar un valor flotante a una variable entera");
			}
			switch (tipo) {
			case "INTEGER":
				t.setTipo("INTEGER");
				break;
			case "FLOAT":
				t.setTipo("FLOAT");
			default:
				break;
			}
			Atributo a = new Atributo("Tipo", tipo);
			Atributo a2 = new Atributo("Uso", "terceto");
			Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
			hs.put(a.getNombre(), a);
			hs.put(a2.getNombre(),a2);
			tSimbolos.put("["+t.getId()+"]", hs);
 		}
		tercetos.add(t);
		
		return t.getId();
	}
	
	
	private boolean redeclarable(String id) {
		//Este metodo retorna el valor del "shadowing" si el procedimiento lo tiene definido, o true si no
		String proc = this.ambito.toString();
		if(!proc.substring(proc.indexOf("."), proc.length()).equals(".p")) {
			proc = proc.substring(proc.lastIndexOf(".")+1, proc.length()) + proc.substring(proc.indexOf("."), proc.lastIndexOf("."));
			if (tSimbolos.get(proc).containsKey("SHADOWING"))
				if(tSimbolos.get(proc).get("SHADOWING").toString().contains("FALSE")) {
					return false;
				}
		}
		return true;
	}
	
	/*public boolean shadowing(String proc) {
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
	}*/
	
	
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
		//System.out.println(nAtt + " VALOR: " + valor);
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


	public boolean validarTipo(String id, int indice, int cant){
		//VALIDA QUE EN LAS INVOCACIONES A PROCEDIMIENTOS ESTEN BIEN LA CANTIDAD DE PARAMETROS Y LOS TIPOS (LO HACE UNO 
		//A UNO A MEDIDA QUE LOS RECONOCE EN LA GRAMATICA)
		//int ultP = this.ambito.lastIndexOf(".");
		String proc = this.getNombreProc(this.ultProc + this.ambito.toString()); //BUSCA EL PROCEDIMIENTO MAS PROXIMO DEFINIDO EN AMBITOS
		//System.out.println(this.ultProc + this.ambito.toString());
		if (proc != null) {
			//System.out.println("PROCEDIMIENTO " + proc);
			if (!this.tSimbolos.get(proc).containsKey("param" + (cant+1))) {
				if (id == null) 
					return true;
				if(this.tSimbolos.get(proc).containsKey("param" + (indice))) {
					String tipoParamFormal = (String) this.tSimbolos.get(proc).get("param" + indice).getValue();
					String tipoParamReal = "";
					if (validarDefinicion(id)) {
						tipoParamReal = (String) this.tSimbolos.get(id + this.ambito).get("Tipo").getValue();
					}
					if (tipoParamFormal.equals(tipoParamReal)) {
						Terceto t = new Terceto((String)this.tSimbolos.get(proc).get("param" + indice+"ID").getValue(),id,":=",tipoParamFormal);
						tercetos.add(t);
						return true;
					}
					yyerror("Tipo del identificador pasado por parametro real no se corresponde con el parametro formal. Se espera "
							+ tipoParamFormal + " y se recibio " + tipoParamReal);
					return false;
				} else {
					yyerror("Error en la invocacion, numero de parametros superior al esperado");
					return false;
				}
			} else { 
				yyerror("Error en la invocacion, numero de parametros inferior al esperado");
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
			System.out.println("Error de sintaxis en la linea " + nLinea + ": " + s);
			pw.println("Error de sintaxis en la linea " + nLinea + ": " + s);
		}
	}

	private void yyerror(int linea, String s) {
		erroresS++;
		//System.out.println("Linea: " + linea + " - Error: " + s);
		pw.println("Error de sintaxis en la linea " + linea + ": " + s);
	}

	private void yyerrorLex(String s) {
		// Agrega los errores lexicos que detecta la gramatica	
		Lexico.erroresL++;
		pw.println("Error en la linea " + nLinea + ": " + s);
	}
//#line 803 "Parser.java"
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
//#line 44 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");}
break;
case 5:
//#line 47 "gramatica.y"
{ validarDefinicion(val_peek(0).sval);
					validarTipo(val_peek(0).sval,1,1);
			}
break;
case 6:
//#line 50 "gramatica.y"
{ validarDefinicion(val_peek(2).sval);
						validarDefinicion(val_peek(0).sval);
						validarTipo(val_peek(2).sval,1,2);
						validarTipo(val_peek(0).sval,2,2);
						}
break;
case 7:
//#line 55 "gramatica.y"
{
						validarDefinicion(val_peek(4).sval);
						validarDefinicion(val_peek(2).sval);
						validarDefinicion(val_peek(0).sval);
						validarTipo(val_peek(4).sval,1,3);
						validarTipo(val_peek(2).sval,2,3);
						validarTipo(val_peek(0).sval,3,3);
						}
break;
case 8:
//#line 63 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");
					 validarDefinicion(val_peek(1).sval);
					 validarTipo(val_peek(1).sval,1,1);
					 }
break;
case 9:
//#line 67 "gramatica.y"
{yyerror(val_peek(0).ival,"Parametro faltante luego de la ',' ");
							 validarDefinicion(val_peek(3).sval);
							validarDefinicion(val_peek(1).sval);
							validarTipo(val_peek(3).sval,1,2);
							validarTipo(val_peek(1).sval,2,2);
							}
break;
case 10:
//#line 77 "gramatica.y"
{this.lastTipo = new StringBuffer(val_peek(0).sval);}
break;
case 11:
//#line 78 "gramatica.y"
{this.lastTipo = new StringBuffer(val_peek(0).sval);}
break;
case 20:
//#line 99 "gramatica.y"
{imprimirTercetos();
										reducirAmbito();}
break;
case 21:
//#line 104 "gramatica.y"
{imprimirTercetos();
				incrementarAmbito(val_peek(0).sval);
				this.ultProc = new StringBuffer(val_peek(0).sval);
				addAtributoPDec(this.ultProc.toString(),"Uso", "nombre de procedimiento");}
break;
case 23:
//#line 111 "gramatica.y"
{addAtributoP( this.ultProc.toString(),"NA", val_peek(4).sval);
																addAtributoP( this.ultProc.toString(),"SHADOWING", val_peek(0).sval);}
break;
case 25:
//#line 114 "gramatica.y"
{addAtributoP(this.ultProc.toString(),"NA", val_peek(4).sval);
																addAtributoP(this.ultProc.toString(),"SHADOWING", val_peek(0).sval);}
break;
case 31:
//#line 124 "gramatica.y"
{yyerror(val_peek(0).ival,"Error sintactico en sentencia ejecutable");}
break;
case 33:
//#line 131 "gramatica.y"
{Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
                                tInc.setOp2("[" + (Terceto.id +1) + "]");
                                Terceto t = new Terceto("[" + nroTerceto + "]", null, "BI", null);
                                tercetos.add(t);
                            }
break;
case 34:
//#line 139 "gramatica.y"
{nroTerceto = Terceto.id;}
break;
case 35:
//#line 142 "gramatica.y"
{Terceto t = new Terceto(val_peek(1).sval,"[?]","BF", null);
                        tercetos.add(t);
                        colaTercetos.add(t);}
break;
case 36:
//#line 147 "gramatica.y"
{Terceto t = new Terceto(val_peek(3).sval, null, "P",null);
															tercetos.add(t);}
break;
case 37:
//#line 149 "gramatica.y"
{validarTipo(null,0,0);
											Terceto t = new Terceto(val_peek(2).sval, null, "P",null);
											tercetos.add(t);}
break;
case 38:
//#line 155 "gramatica.y"
{ultProc = new StringBuffer(val_peek(1).sval); 
								yyval.sval = val_peek(1).sval;}
break;
case 39:
//#line 159 "gramatica.y"
{Terceto t = new Terceto(val_peek(2).sval, null, "O",null);
										tercetos.add(t);}
break;
case 40:
//#line 163 "gramatica.y"
{Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
																tInc.setOp1("["+(Terceto.id)+"]");}
break;
case 41:
//#line 165 "gramatica.y"
{Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
											tInc.setOp1("["+(Terceto.id)+"]");}
break;
case 42:
//#line 169 "gramatica.y"
{ Terceto tInc = colaTercetos.get(colaTercetos.size()-1);
							tInc.setOp2("["+(Terceto.id+1)+"]");
							Terceto t = new Terceto("[?]",null,"BI",null);
							tercetos.add(t);
							colaTercetos.add(t);

}
break;
case 43:
//#line 178 "gramatica.y"
{Terceto t = new Terceto(val_peek(1).sval,"[?]","BF", null);
								tercetos.add(t);
								colaTercetos.add(t);}
break;
case 44:
//#line 182 "gramatica.y"
{yyval.sval = "["+agregarTerceto(val_peek(2).sval,val_peek(0).sval,val_peek(1).sval).toString()+"]";}
break;
case 45:
//#line 185 "gramatica.y"
{yyval.sval = "==";}
break;
case 46:
//#line 186 "gramatica.y"
{yyval.sval = "<=";}
break;
case 47:
//#line 187 "gramatica.y"
{yyval.sval = ">=";}
break;
case 48:
//#line 188 "gramatica.y"
{yyval.sval = "!=";}
break;
case 49:
//#line 189 "gramatica.y"
{yyval.sval = "<";}
break;
case 50:
//#line 190 "gramatica.y"
{yyval.sval = ">";}
break;
case 51:
//#line 193 "gramatica.y"
{if (setTipo(val_peek(0).sval, val_peek(1).sval))
						addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");
					addAtributoP(this.ultProc.toString(), "param1", val_peek(1).sval);
					addAtributoP(this.ultProc.toString(), "param1ID", val_peek(0).sval);}
break;
case 52:
//#line 197 "gramatica.y"
{ if(setTipo(val_peek(3).sval, val_peek(4).sval))
										addAtributo(val_peek(3).sval,"Uso", "nombre de parametro");
									if(setTipo(val_peek(0).sval, val_peek(1).sval))
										addAtributo(val_peek(0).sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", val_peek(4).sval);
									addAtributoP(this.ultProc.toString(), "param2", val_peek(1).sval);
									addAtributoP(this.ultProc.toString(), "param1ID", val_peek(3).sval);
									addAtributoP(this.ultProc.toString(), "param2ID", val_peek(0).sval);
									}
break;
case 53:
//#line 206 "gramatica.y"
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
									addAtributoP(this.ultProc.toString(), "param1ID", val_peek(6).sval);
									addAtributoP(this.ultProc.toString(), "param2ID", val_peek(3).sval);
									addAtributoP(this.ultProc.toString(), "param3ID", val_peek(0).sval);
									}
break;
case 54:
//#line 220 "gramatica.y"
{yyerror(val_peek(1).ival,"Parametro faltante luego de la ',' ");
							if(setTipo(val_peek(1).sval, val_peek(2).sval))
								addAtributo(val_peek(1).sval,"Uso", "nombre de parametro");
							addAtributoP(this.ultProc.toString(), "param1", val_peek(2).sval);
							addAtributoP(this.ultProc.toString(), "param1ID", val_peek(1).sval);
							}
break;
case 55:
//#line 226 "gramatica.y"
{yyerror(val_peek(2).ival,"Parametro faltante luego de la ',' ");
									if (setTipo(val_peek(4).sval, val_peek(5).sval))
										addAtributo(val_peek(4).sval,"Uso", "nombre de parametro");
									if (setTipo(val_peek(1).sval, val_peek(2).sval))
										addAtributo(val_peek(1).sval,"Uso", "nombre de parametro");
									addAtributoP(this.ultProc.toString(), "param1", val_peek(5).sval);
									addAtributoP(this.ultProc.toString(), "param2", val_peek(2).sval);
									addAtributoP(this.ultProc.toString(), "param1ID", val_peek(4).sval);
									addAtributoP(this.ultProc.toString(), "param2ID", val_peek(1).sval);
									}
break;
case 56:
//#line 240 "gramatica.y"
{ if (validarDefinicion(val_peek(3).sval))
											agregarTerceto(val_peek(3).sval,val_peek(1).sval,":=");}
break;
case 57:
//#line 242 "gramatica.y"
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
case 58:
//#line 263 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"+").toString()+"]";}
break;
case 59:
//#line 264 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"-").toString()+"]";}
break;
case 61:
//#line 268 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"*").toString()+"]";}
break;
case 62:
//#line 269 "gramatica.y"
{yyval.sval = "[" + agregarTerceto(val_peek(2).sval, val_peek(0).sval,"/").toString()+"]";}
break;
case 64:
//#line 273 "gramatica.y"
{validarDefinicion(val_peek(0).sval);}
break;
case 65:
//#line 274 "gramatica.y"
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
case 66:
//#line 298 "gramatica.y"
{	
							Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
							int cant = 0;
							if (!tSimbolos.containsKey("-" +val_peek(0).sval)){
								Atributo tipo = new Atributo("Tipo", "INTEGER");
								Atributo cantR = new Atributo("Referencias", 1);
								hs.put(cantR.getNombre(), cantR);
								hs.put(tipo.getNombre(), tipo);
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
case 67:
//#line 319 "gramatica.y"
{}
break;
case 68:
//#line 320 "gramatica.y"
{	
							Hashtable<String, Atributo> hs = new Hashtable<String, Atributo>();
							int cant = 0;
							if (!tSimbolos.containsKey("-" +val_peek(0).sval)){
								Atributo tipo = new Atributo("Tipo", "FLOAT");
								Atributo cantR = new Atributo("Referencias", 1);
								hs.put(cantR.getNombre(), cantR);
								hs.put(tipo.getNombre(), tipo);
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
//#line 1332 "Parser.java"
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
