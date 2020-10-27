Para la prueba del ejecutable, se debe ejecutar por consola el archivo ejecutable(ejecutableCompiladorGrupo16.jar) situandose en la carpeta "ejecutable" con alguno 
de los archivos de extension txt con casos de prueba como parametro, cada uno de estos esta comentado con su comportamiento esperado dentro del archivo:

	-casosDePruebaCADENAS
	-casosDePruebaCOMENTARIOS
	-casosDePruebaCTESFLOAT
	-casosDePruebaCTESINT
	-casosDePruebaESTRUCTURAS
	-casosDePruebaID_KEYW


Ejemplo de ejecucion:
	
	java -jar ejecutableCompiladorGrupo16.jar casosDePruebaCADENAS.txt

Se espera que como salida, se generen 4 archivos, uno que indica los errores, otro con los tokens, otro con las estructuras
y otro con la tabla de simbolos.