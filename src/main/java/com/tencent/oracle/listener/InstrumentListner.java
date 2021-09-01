package com.tencent.oracle.listener;

import com.tencent.oracle.out.PlSqlParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import com.tencent.oracle.out.PlSqlParser;


public class InstrumentListner extends PlSqlParserBaseListener {
    public TokenStreamRewriter rewriter;
    TokenStream tokens;

    private final String coverageDeclare;
    private final String coverageEnd;
    private final String coverageStatementIncrementor;
    private final String coverageBranchIncrementor;

    public InstrumentListner(TokenStream tokens, String coverageDeclare, String coverageEnd,
                             String coverageStatementIncrementor, String coverageBranchIncrementor){
        this.tokens = tokens;
        this.coverageDeclare = coverageDeclare;
        this.coverageEnd = coverageEnd;
        this.coverageStatementIncrementor = coverageStatementIncrementor;
        this.coverageBranchIncrementor = coverageBranchIncrementor;
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    private void instrumentCoverageDeclare(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.getStart(), coverageDeclare);
    }

    private void instrumentCoverageEnd(ParserRuleContext ctx) {
        rewriter.insertAfter(ctx.getStop(), coverageEnd);
    }

    private void instrumentCoverageStatementIncrement(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.getStart(), coverageStatementIncrementor);
    }

    private void instrumentCoverageBranchIncrement(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.getStart(), coverageBranchIncrementor);
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
        instrumentCoverageStatementIncrement(ctx);
    }

    @Override
    public void enterStatement(PlSqlParser.StatementContext ctx) {
        super.enterStatement(ctx);
        instrumentCoverageStatementIncrement(ctx);
    }

    @Override
    public void enterIf_statement(PlSqlParser.If_statementContext ctx) {
        super.enterIf_statement(ctx);
        instrumentCoverageBranchIncrement(ctx);
    }

    @Override
    public void enterElsif_part(PlSqlParser.Elsif_partContext ctx) {
        super.enterElsif_part(ctx);
        instrumentCoverageBranchIncrement(ctx);
    }

    @Override
    public void enterElse_part(PlSqlParser.Else_partContext ctx) {
        super.enterElse_part(ctx);
        instrumentCoverageBranchIncrement(ctx);
    }
}
