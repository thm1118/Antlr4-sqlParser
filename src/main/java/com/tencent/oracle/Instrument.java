package com.tencent.oracle;

import com.tencent.oracle.listener.InstrumentListner;
import com.tencent.oracle.out.PlSqlLexer;
import com.tencent.oracle.out.PlSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Instrument {
    public static void main(String[] args){
//        String initialString = "SELECT * FROM FOO;";
        String initialString = "CREATE PROCEDURE REMOVE_EMP (EMPLOYEE_ID NUMBER) AS\n" +
                "   TOT_EMPS NUMBER;\n" +
                "   BEGIN\n" +
                "      DELETE FROM EMPLOYEES\n" +
                "      WHERE EMPLOYEES.EMPLOYEE_ID = REMOVE_EMP.EMPLOYEE_ID;\n" +
                "   TOT_EMPS := TOT_EMPS - 1;\n" +
                "   END;\n" +
                "   \n" +
                "SELECT * FROM FOO;\n" +
                "/* 注释不会解析 */\n" +
                "/* DELETE * FROM BAR; */";

        CharStream input = CharStreams.fromString(initialString.toUpperCase());

        PlSqlLexer lexer = new PlSqlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlSqlParser parser = new PlSqlParser(tokens);

        ParseTree tree = parser.sql_script();

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        InstrumentListner instrumentor = new InstrumentListner(tokens);
        walker.walk(instrumentor, tree); // initiate walk of tree with listener

        // 输出 注入覆盖率计数器 的SQL
        System.out.println(instrumentor.rewriter.getText());
        System.out.println();
        System.out.println(tree.toStringTree(parser));
        System.out.println();
        System.out.println(tree.toStringTree());
        System.out.println();
        System.out.println(tree.getText());
    }
}
