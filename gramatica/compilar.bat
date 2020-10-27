yacc.exe -J gramatica.y
copy ParserVal.java temp.txt
echo.package compilador;>ParserVal.java
type temp.txt >>ParserVal.java
del temp.txt
move .\Parser.java ..\src\compilador\
move .\ParserVal.java ..\src\compilador\