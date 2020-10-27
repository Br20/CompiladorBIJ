package compilador;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;



public class Lexico {
	

	private int[] pos = {0};
	private char actual;
	public StringBuffer buffer = new StringBuffer();
	public static int erroresL = 0;
	private AccionSemantica accion;
	private int token;
	private boolean[] lex = new boolean [1];
	private BarraDivisora accBarra= new BarraDivisora();
    private ContarNL accNL = new ContarNL();
	private AccionInicializaBuffer accInicB = new AccionInicializaBuffer();
	private CaracterSimple accCaract = new CaracterSimple();
	private VerificaRangoInt accInt= new VerificaRangoInt();
	private VerificaRangoFloat accFloat = new VerificaRangoFloat();
	private VerificaLargoID accID = new VerificaLargoID();
	private AccionCadena accCad = new AccionCadena();
	private AgregaCaracter accAgg = new AgregaCaracter();
	private VerificaKeyW accKeyW = new VerificaKeyW();
	private VerificarComparador accComp = new VerificarComparador();
	private AccionErrorComentario accErrorCom = new AccionErrorComentario();
	private AccionAsignacion accAsig = new AccionAsignacion();
	private AccionErrorInt accErrorInt = new AccionErrorInt();
	private AccionErrorIntSufijo accErrorIntSuf = new AccionErrorIntSufijo();
	private AccionErrorCaracter accErrorCaracter = new AccionErrorCaracter();
	private AccionErrorFloat accErrorFloat= new AccionErrorFloat();
	private AccionErrorCadenaMultilinea accErrorCadML = new AccionErrorCadenaMultilinea();
	private String codigo;
	private String path;
	
	
	
	private AccionSemantica [][] matAcciones= { {accInicB,accInicB,accInicB,accErrorCaracter,accErrorCaracter,accCaract,accCaract,null,accCaract,accCaract,accInicB,accInicB,accInicB,accInicB,accInicB,accInicB,null,accNL,accInicB,accInicB,accCaract,accCaract, accInicB},//DUDA EN EL 5 (ESTABA VACIO)
												{accKeyW,accAgg,accKeyW,accAgg,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW,accKeyW, accKeyW},
												{accAgg,accID,accAgg,accAgg,accID,accID,accID,accID,accID,accID,accID,accID,accID,accID,accID,accID,accID,accID,accAgg,accAgg,accID,accID,accID},
												{accErrorInt,accErrorInt,accAgg,null,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accAgg,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt,accErrorInt, accErrorInt},//No vino punto, guion o digito 
												{accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accInt,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf,accErrorIntSuf},// No vino la i
												{accErrorCaracter,accErrorCaracter,accAgg,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter,accErrorCaracter},//No vino numerito
												{accFloat,accFloat,accAgg,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accAgg,accFloat,accFloat,accFloat},
												{accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accAgg,accAgg,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat},//No vino + o - despues de f
												{accErrorFloat,accErrorFloat,accAgg,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat,accErrorFloat}, //No vino numero despues del signo de float
												{accFloat,accFloat,accAgg,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat,accFloat},
												{accBarra,accBarra,accBarra,accBarra,null,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra,accBarra},
												{null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,accNL,null,null,null,accErrorCom, null},//No cierra comentario
												{null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,accNL,null,null,null,accErrorCom, null},//No cierra comentario
												{accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp},
												{accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp},
												{accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp},
												{accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp,accComp},
												{accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accAgg,accCad,accAgg,accErrorCadML,accAgg,accAgg,accAgg,accErrorCadML,accAgg},//No cierra cadena o tiene mas de una linea
												{accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig,accAsig}};//
	
	
	
	private int [][] matEstados = { {2 ,1 ,3 ,0 ,0 ,99,99,10,99,99,5 ,13,15,14,16,17,0 ,0 ,2 ,2 ,99,99,18},
									{99,1 ,99,1 ,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{2 ,99,2 ,2 ,99,99,99,99,99,99,99,99,99,99,99,99,99,99,2 ,2 ,99,99,99},
									{99,99,3 ,4 ,99,99,99,99,99,99,6 ,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{0 ,0 ,6 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,99 ,0 },
									{99,99,6 ,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,7 ,99,99,99},
									{99,99,99,99,99,8 ,8 ,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,9 ,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,9 ,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,99,99,11,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{11,11,11,11,12,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,99,11},
									{11,11,11,11,12,11,11,0 ,11,11,11,11,11,11,11,11,11,11,11,11,11,99,11},
									{99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99},
									{17,17,17,17,17,17,17,17,17,17,17,17,17,17,17,99,17,99,17,17,17,99,17},
									{99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99,99}};
	//48 - 57 numero
	//97 - 122 minuscula
	//65 - 90 mayuscula
	
	
	public Lexico(String path) {
		// TODO Auto-generated constructor stub
		// Se inicializa la clase lexico con una ruta de un archivo y si existe se extrae el codigo
		this.path = path;
		try {
			this.codigo= new String(Files.readAllBytes(Paths.get(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public Par yylex() {
		//metodo encargado de la devolucion de tokens
		int estado = 0;
		token = 0;
		lex[0] = false;
		if (pos[0] < codigo.length()) {
			while (estado != 99 && pos[0]< codigo.length()) 
			{
				actual = codigo.charAt(pos[0]);
				accion = matAcciones[estado][getColumna(actual)];
				if (accion != null) {
					token = accion.accionar(buffer, actual, pos, lex);
				}
				estado= matEstados[estado][getColumna(actual)];
				if (pos[0]< codigo.length()) {
					pos[0]++;
				}
				else {
					break;
				}
			}
			if (pos[0] == codigo.length() && estado!=99) { //se acomoda si se llega a fin de codigo 
				accion = matAcciones[estado][21];
				estado = matEstados[estado][21];
				if (accion != null)
					token = accion.accionar(buffer, actual, pos, lex);
			}
			if (lex[0]) {
				return new Par(buffer.toString(), token);
			}
			return new Par(null, token);
		}
		return new Par(null, 0);
	}
	
	
	
	
	public int getCantErrores() {
		return this.erroresL; 
	}
	
	
	private int getColumna(char c) {
		//indica a que columna corresponde el caracter actual
		switch (c){
			case 'i':
				return 18;
			case 'f':
				return 19;
			case '>':
				return 13;
			case '<':
				return 11;
			case '=':
				return 12;
			case '/':
				return 7;
			case '*':
				return 8;
			case '+':
				return 5;
			case '-':
				return 6;
			case '_':
				return 3;
			case '%':
				return 4;
			case '\'':
				return 15;
			case '\n':
				return 17;
			case '!':
				return 14;
			case '.':
				return 10;
			case '\t':
				return 16;
			case ' ':
				return 16;
			case ':':
				return 22;
			case '\r':
				return 17; 
			default:
				break;
		}
		int ascii = (int) c;
		if ((ascii > 96) && (ascii < 123))
			return 0;
		if ((ascii > 64) && (ascii < 91))
			return 1;
		if ((ascii > 47) && (ascii < 58))
			return 2;
		if ((ascii == 40) || (ascii == 41) || (ascii==123) || (ascii == 125))
			return 9;
		if ((ascii == 96) && (ascii == 123))
			return 0;
		return 20; 	
	}
	
	
}
