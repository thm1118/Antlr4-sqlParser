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

    // todo: 这里为了演示插桩用了文字，需要设计 覆盖率数据声明头，结尾，增量计数器（语句、分支、函数（含存储过程、触发器等））
    public static final String coverageDeclare = "/*注入的覆盖率数据存储，计数器等的声明头 */\n";
    public static final String  coverageEnd = "/*注入的覆盖率计数器最后收尾动作，比如汇总？ */\n";
    public static final String coverageStatementIncrementor = "/*注入的语句 增量计数器*/ \n";
    public static final String coverageBranchIncrementor = "/*注入的分支 增量计数器*/ \n";

    private final InstrumentListner instrumentor;

    public Instrument(String originalSQL) {
        CharStream input = CharStreams.fromString(originalSQL.toUpperCase());
        PlSqlLexer lexer = new PlSqlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlSqlParser parser = new PlSqlParser(tokens);

        ParseTree tree = parser.sql_script();

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        this.instrumentor = new InstrumentListner(tokens, coverageDeclare, coverageEnd,
                coverageStatementIncrementor, coverageBranchIncrementor);
        walker.walk(instrumentor, tree); // initiate walk of tree with listener

        // todo: 调试用，需移除
//        System.out.println("-------------------tree.getText():-------------------");
//        System.out.println(tree.getText());
//        System.out.println("-------------------tree.toStringTree():-------------------");
//        System.out.println(tree.toStringTree());
//        System.out.println("-------------------tree.toStringTree(parser):-------------------");
//        System.out.println(tree.toStringTree(parser));
    }

    public String getInstrumentSQL() {
        return instrumentor.rewriter.getText();
    }


}
