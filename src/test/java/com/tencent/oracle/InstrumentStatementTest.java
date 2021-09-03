package com.tencent.oracle;

import org.junit.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 验证 函数、存储过程，触发器 等内部语句 的 正确插桩
 * **/
public class InstrumentStatementTest {




    @Test
    public void shouldInstrumentAssignStatement() throws IOException {
        Instrument instrument;
        String sql = Util.readResource("assign_statement.sql");
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);

        int assign_statementIndex = rows.indexOf("SAL_RAISE := .08;\r");
        assertTrue(assign_statementIndex >0);
        assertEquals("statements_cov_tab(2).exc_count := statements_cov_tab(2).exc_count + 1;",
                rows.get(assign_statementIndex - 1).trim());
    }


    @Test
    public void shouldInstrumentDeclareVariableStatement() throws IOException {
        Instrument instrument;
        String sql = Util.readResource("declare.sql");
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        int declare_statementIndex = rows.indexOf("DECLARE\r");
        assertTrue(declare_statementIndex > 0);
        assertEquals("statements_cov_tab(1).exc_count := statements_cov_tab(1).exc_count + 1;",
                rows.get(declare_statementIndex - 1).trim());
    }

    @Test
    public void shouldInstrumentIFStatement() throws IOException {
        Instrument instrument;
        String sql = Util.readResource("if.sql");
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        int if_statementIndex = rows.indexOf("IF JOB_ID = 1 THEN ");
        assertTrue(if_statementIndex > 0);
        assertEquals("statements_cov_tab(2).exc_count := statements_cov_tab(2).exc_count + 1;",
                rows.get(if_statementIndex - 1).trim());
        assertEquals("branches_cov_tab(1).exc_count := branches_cov_tab(1).exc_count + 1;",
                rows.get(if_statementIndex + 1).trim());

    }

    @Test
    public void shouldInstrumentElseStatement() throws IOException {
        Instrument instrument;
        String sql = Util.readResource("if_else.sql");
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);

        int Else_statementIndex = rows.indexOf("    ELSE ");
        assertTrue(Else_statementIndex > 0);
        assertEquals("branches_cov_tab(2).exc_count := branches_cov_tab(2).exc_count + 1;",
                rows.get(Else_statementIndex + 1).trim());
        assertEquals("statements_cov_tab(4).exc_count := statements_cov_tab(4).exc_count + 1;",
                rows.get(Else_statementIndex + 4).trim());
    }

    @Test
    public void shouldInstrumentElseIfStatement() throws IOException {
        Instrument instrument;
        String sql = Util.readResource("if_elseif.sql");
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);

        int ElseIf_statementIndex = rows.indexOf("    ELSIF JOBID = 'SH_CLERK' THEN ");
        assertTrue(ElseIf_statementIndex > 0);
        assertEquals("branches_cov_tab(2).exc_count := branches_cov_tab(2).exc_count + 1;",
                rows.get(ElseIf_statementIndex + 1).trim());
        assertEquals("statements_cov_tab(5).exc_count := statements_cov_tab(5).exc_count + 1;",
                rows.get(ElseIf_statementIndex + 4).trim());
        assertEquals("branches_cov_tab(3).exc_count := branches_cov_tab(3).exc_count + 1;",
                rows.get(ElseIf_statementIndex + 7).trim());
        assertEquals("branches_cov_tab(4).exc_count := branches_cov_tab(4).exc_count + 1;",
                rows.get(ElseIf_statementIndex + 12).trim());
    }

}
