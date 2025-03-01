import ASTNode.*;
import AntlrGenFiles.NybCLexer;
import AntlrGenFiles.NybCParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class ASTVisitorTest {


    @Test
    void visitBinaryOpsNode() {
        //Setup
        NybCStack nybCStack = new NybCStack();
        ASTVisitor astVisitor = new ASTVisitor(nybCStack);

        // Add all variables
        IntNode intNode = new IntNode();
        intNode.setValue(2);
        FloatNode floatNode = new FloatNode();
        floatNode.setValue(2.5f);
        BoolNode boolNode = new BoolNode();
        boolNode.setValue(true);
        StringNode stringNode = new StringNode();
        stringNode.setValue("Hello");


        // Test for Int + Int
        BinaryOpNode binaryOpNode = new BinaryOpNode(intNode, "+", intNode);
        var resultIntInt = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultIntInt);
        Assertions.assertEquals(4, resultIntInt);

        // Test for Int + float
        binaryOpNode = new BinaryOpNode(intNode, "+", floatNode);
        var resultIntFloat = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultIntFloat);
        Assertions.assertEquals(4.5f, resultIntFloat);

        // Test for Int + Bool
        Assertions.assertThrows(RuntimeException.class, () -> {
            BinaryOpNode TMPbinaryOpNode = new BinaryOpNode(intNode, "+", boolNode);
            var resultIntBool = astVisitor.Visit(TMPbinaryOpNode);
            Assertions.assertNull(resultIntBool);
        });

        // Test for Int + String
        binaryOpNode = new BinaryOpNode(intNode, "+", stringNode);
        var resultIntString = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultIntString);
        Assertions.assertEquals("2Hello", resultIntString);

        // Test for Float + Float
        binaryOpNode = new BinaryOpNode(floatNode, "+", floatNode);
        var resultFloatFloat = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultFloatFloat);
        Assertions.assertEquals(5.0f, resultFloatFloat);

        // Test for Float + bool
        Assertions.assertThrows(RuntimeException.class, () -> {
            BinaryOpNode TMPbinaryOpNode = new BinaryOpNode(floatNode, "+", boolNode);
            var resultFloatBool = astVisitor.Visit(TMPbinaryOpNode);
            Assertions.assertNull(resultFloatBool);
        });

        // Test for Float + String
        binaryOpNode = new BinaryOpNode(floatNode, "+", stringNode);
        var resultFloatString = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultFloatString);
        Assertions.assertEquals("2.5Hello", resultFloatString);

        // Test for Bool + bool
        Assertions.assertThrows(RuntimeException.class, () -> {
            BinaryOpNode TMPbinaryOpNode = new BinaryOpNode(boolNode, "+", boolNode);
            var resultBoolBool = astVisitor.Visit(TMPbinaryOpNode);
            Assertions.assertNull(resultBoolBool);
        });

        // Test for Bool + String
        binaryOpNode = new BinaryOpNode(boolNode, "+", stringNode);
        var resultBoolString = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultBoolString);
        Assertions.assertEquals("trueHello", resultBoolString);

        // Test for String + String
        binaryOpNode = new BinaryOpNode(stringNode, "+", stringNode);
        var resultStringString = astVisitor.Visit(binaryOpNode);
        Assertions.assertNotNull(resultStringString);
        Assertions.assertEquals("HelloHello", resultStringString);

    }
}


