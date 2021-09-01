package com.tencent.oracle;

import com.google.common.io.Resources;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import com.google.common.io.Resources;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 验证 函数、存储过程，触发器 等内部语句 的 正确插桩
 * **/
public class InstrumentStatementTest {

    // String sql = readResource("ifelse.sql");
    private static String readResource(String name)
            throws IOException
    {
        return Resources.toString(Resources.getResource("oracle/"+name), UTF_8);
    }

    private static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    @Test
    public void shouldInstrumentAssignStatement(){
        Instrument instrument;
        String sql = "sal_raise := .08;";
        instrument = new Instrument(sql);
        assertTrue(instrument.getInstrumentSQL().startsWith(Instrument.coverageDeclare +
                Instrument.coverageStatementIncrementor));
    }

    @Test
    public void shouldInstrumentDeclareVariableStatement(){
        Instrument instrument;
        String sql = "DECLARE\n" +
                "   jobid      employees.job_id%TYPE;";
        instrument = new Instrument(sql);
        assertTrue(instrument.getInstrumentSQL().startsWith(Instrument.coverageDeclare +
                Instrument.coverageStatementIncrementor));
    }

    @Test
    public void shouldInstrumentIFStatement() throws IOException {
        Instrument instrument;
        String sql = "BEGIN \n" +
                "IF jobid = 'PU_CLERK' THEN sal_raise := .09;\n " +
                " END IF;"+
                "END; \n";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
//        assertTrue(result.contains(Instrument.coverageBranchIncrementor));
        assertEquals(count(result, Instrument.coverageBranchIncrementor), 1);

    }

    @Test
    public void shouldInstrumentElseStatement(){
        Instrument instrument;
        String sql = "BEGIN \n" +
                "IF jobid = 'PU_CLERK' THEN sal_raise := .09; \n " +
                "ELSE sal_raise := 0; \n " +
                "END IF;"+
                "END; \n";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        //todo: 应 包含 2 个 coverageBranchIncrementor；
        assertEquals(count(result, Instrument.coverageBranchIncrementor), 2);
    }

    @Test
    public void shouldInstrumentElseIfStatement(){
        Instrument instrument;
        String sql ="BEGIN \n" +
                "IF jobid = 'PU_CLERK' THEN sal_raise := .09; \n" +
                " ELSIF jobid = 'SH_CLERK' THEN sal_raise := .08; \n " +
                "END IF;"+
                "END; \n";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        assertEquals(count(result, Instrument.coverageBranchIncrementor), 3);
    }

}
