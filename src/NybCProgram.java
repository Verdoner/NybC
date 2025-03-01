import ASTNode.ProgramNode;
import AntlrGenFiles.NybCLexer;
import AntlrGenFiles.NybCParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NybCProgram {
    private final List<String> keywords = new ArrayList<>();
    private final NybCStack nybCStack;

    /**
     * Constructor for NybCProgram
     * Adds keywords to the list of reserved words and creates the stack for the program
     */
    public NybCProgram() {
        keywords.addAll(Arrays.asList("begin", "end", "loop", "in", "out", "if", "else", "switch", "var", "function", "case", "return", "break", "continue", "true", "false"));
        nybCStack = new NybCStack();
    }

    /**
     * StartProgram method that starts the program
     * If no arguments are given, it will run the program with the default file
     * If an argument is given, it will run the program with the given file
     */
    public void StartProgram() {
        Path filePath = Path.of("src/code.nybc");
        setup(filePath);
    }
    public void StartProgram(String fileToUse) {
        Path filePath = Path.of(fileToUse);
        setup(filePath);
    }

    /**
     * Setup method that sets up the program
     * @param filePath the path to the file
     * It reads the file and runs the lexer, parser, initial visitor and interpreter
     */
    private void setup(Path filePath) {
        CodePointCharStream inputStream;
        try {
            inputStream = CharStreams.fromString(Files.readString(filePath));
        } catch (IOException ioException) {
            System.out.println("Could not load file");
            return;
        }
        var lexer = new NybCLexer(inputStream);
        var tokenStream = new CommonTokenStream(lexer);

        NybCParser parser;
        ParseTree parseTree;
        try {
            parser = new NybCParser(tokenStream);
            parseTree = parser.program();

        } catch (NullPointerException e){
            Error.SYNTAX_ERROR(e);
            return;
        }
        var ASTVisitor = new ToASTVisitor();
        var AST = ASTVisitor.visit(parseTree);
        var InitialVisitor = new InitialVisitor(nybCStack, keywords);
        InitialVisitor.Visit((ProgramNode) AST);
        var Interpreter = new Interpreter(nybCStack, keywords);
        Interpreter.Visit((ProgramNode) AST);
    }
}
