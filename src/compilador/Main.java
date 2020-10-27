package compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
	
	public static String codigo;
	
	public static void main(String[] args) throws IOException {
		
		
        Parser p = new Parser();
        p.yyparse(args[0]); //el primer argumento especifica la ruta del archivo con el codigo
        p.escribirTablaS();
        p.escribirEstruc();
		p.cerrarFicheros();
	}

}
