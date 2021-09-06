package com.tencent.oracle.listener;

import com.tencent.oracle.config.CoverageDataConfig;
import com.tencent.oracle.out.PlSqlParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import com.tencent.oracle.out.PlSqlParser;


public class InstrumentListner extends PlSqlParserBaseListener {
    public TokenStreamRewriter rewriter;
    TokenStream tokens;

    CoverageDataConfig coverageDataConfig;

    public InstrumentListner(TokenStream tokens, CoverageDataConfig coverageDataConfig){
        this.tokens = tokens;
        this.coverageDataConfig = coverageDataConfig;
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    private void instrumentCoverageDeclare(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.getStart(), coverageDataConfig.getCoverageDeclare());
    }

    private void instrumentCoverageEnd(ParserRuleContext ctx) {
        rewriter.insertAfter(ctx.getStop(), coverageDataConfig.getCoverageEnd());
    }

    private void instrumentCoverageStatementIncrement(ParserRuleContext ctx, int seq) {
        rewriter.insertBefore(ctx.getStart(), coverageDataConfig.getCoverageStatementIncrementor(seq));
    }

    /**
     *
     * @param seq ：代码行号
     * @param token：当前 token；
     * */
    private void instrumentCoverageBranchIncrement(Token token, int seq) {
       rewriter.insertAfter(token, coverageDataConfig.getCoverageBranchIncrementor(seq));
    }

    @Override
    public void exitSql_script(PlSqlParser.Sql_scriptContext ctx){
        super.exitSql_script(ctx);
        instrumentCoverageDeclare(ctx);
        instrumentCoverageEnd(ctx);
    }

    @Override
    public void enterUnit_statement(PlSqlParser.Unit_statementContext ctx) {
        super.enterUnit_statement(ctx);
        //todo: row,col ???
        int seq = coverageDataConfig.addStatement(ctx.start.getLine(), ctx.start.getCharPositionInLine());
        instrumentCoverageStatementIncrement(ctx, seq);
    }

    @Override
    public void enterStatement(PlSqlParser.StatementContext ctx) {
        super.enterStatement(ctx);
        int seq = coverageDataConfig.addStatement(ctx.start.getLine(), ctx.start.getCharPositionInLine());
        instrumentCoverageStatementIncrement(ctx, seq);
    }

    @Override
    public void enterIf_statement(PlSqlParser.If_statementContext ctx) {
        super.enterIf_statement(ctx);
        int seq = coverageDataConfig.addBranch(ctx.start.getLine(), ctx.start.getCharPositionInLine());
        instrumentCoverageBranchIncrement(ctx.THEN().getSymbol(), seq);
    }

    @Override
    public void enterElsif_part(PlSqlParser.Elsif_partContext ctx) {
        super.enterElsif_part(ctx);
        int seq = coverageDataConfig.addBranch(ctx.start.getLine(), ctx.start.getCharPositionInLine());
        instrumentCoverageBranchIncrement(ctx.THEN().getSymbol(), seq);
    }

    @Override
    public void enterElse_part(PlSqlParser.Else_partContext ctx) {
        super.enterElse_part(ctx);
        int seq = coverageDataConfig.addBranch(ctx.start.getLine(), ctx.start.getCharPositionInLine());
        instrumentCoverageBranchIncrement(ctx.getStart(), seq);
    }
}
