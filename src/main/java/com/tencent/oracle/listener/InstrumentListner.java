package com.tencent.oracle.listener;

import com.tencent.oracle.out.PlSqlParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import com.tencent.oracle.out.PlSqlParser;


public class InstrumentListner extends PlSqlParserBaseListener {
    public TokenStreamRewriter rewriter;
    TokenStream tokens;
    // todo: 这里为了演示插桩用了文字，需要设计 覆盖率数据声明头，结尾，增量计数器（语句、分支、函数（含存储过程、触发器等））
    private String coverageDeclare = "/*注入的覆盖率数据存储，计数器等的声明头 */\n";
    private String coverageEnd = "/*注入的覆盖率计数器最后收尾动作，比如汇总？ */\n";
    private String coverageIncrementor = "/*注入的增量计数器*/ \n";

    public InstrumentListner(TokenStream tokens){
        this.tokens = tokens;
        this.rewriter = new TokenStreamRewriter(tokens);
    }

    private void instrumentCoverageDeclare(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.getStart(), coverageDeclare);
    }

    private void instrumentCoverageEnd(ParserRuleContext ctx) {
        rewriter.insertAfter(ctx.getStop(), coverageEnd);
    }

    private void instrumentCoverageIncrement(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.getStart(), coverageIncrementor);
    }

    @Override
    public void exitSql_script(PlSqlParser.Sql_scriptContext ctx){
        super.enterSql_script(ctx);
        instrumentCoverageDeclare(ctx);
        instrumentCoverageEnd(ctx);
    }

    @Override
    public void enterUnit_statement(PlSqlParser.Unit_statementContext ctx) {
        super.enterUnit_statement(ctx);
        instrumentCoverageIncrement(ctx);
    }

    @Override
    public void enterStatement(PlSqlParser.StatementContext ctx) {
        super.enterStatement(ctx);
        instrumentCoverageIncrement(ctx);
    }
}
