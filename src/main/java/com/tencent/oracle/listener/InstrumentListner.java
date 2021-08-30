package com.tencent.oracle.listener;

import com.tencent.oracle.out.PlSqlParserBaseListener;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import com.tencent.oracle.out.PlSqlParser;


public class InstrumentListner extends PlSqlParserBaseListener {
    public TokenStreamRewriter rewriter;
    TokenStream tokens;

    public InstrumentListner(TokenStream tokens){
        this.tokens = tokens;
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    /*
    * */
    private void instrumentCoverageIncrement(PlSqlParser.Select_statementContext ctx) {
        rewriter.insertBefore(ctx.getStart(), "注入的增量计数器 \n");
    }


    @Override
    public void enterSelect_statement(PlSqlParser.Select_statementContext ctx) {
        super.enterSelect_statement(ctx);
        instrumentCoverageIncrement(ctx);
    }

    @Override
    public void exitSelect_statement(PlSqlParser.Select_statementContext ctx) {
        super.exitSelect_statement(ctx);
    }


}
