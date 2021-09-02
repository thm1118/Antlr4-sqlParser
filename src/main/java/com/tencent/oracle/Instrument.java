package com.tencent.oracle;

import com.tencent.oracle.config.CoverageDataConfig;
import com.tencent.oracle.listener.InstrumentListner;
import com.tencent.oracle.out.PlSqlLexer;
import com.tencent.oracle.out.PlSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Instrument {

    private final InstrumentListner instrumentor;

    public Instrument(String originalSQL) {
        CharStream input = CharStreams.fromString(originalSQL.toUpperCase());
        PlSqlLexer lexer = new PlSqlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlSqlParser parser = new PlSqlParser(tokens);

        ParseTree tree = parser.sql_script();

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        this.instrumentor = new InstrumentListner(tokens, new CoverageDataConfig());
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
