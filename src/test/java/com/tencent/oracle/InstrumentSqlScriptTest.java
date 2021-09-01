package com.tencent.oracle;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *  在整体SQL 头尾 插桩 功能验证；
 * **/
public class InstrumentSqlScriptTest {

    private static Instrument instrument;
    private static  String sql = "select   *  from\n foo\n  order by x ; ";

    @BeforeClass
    public static void SetUpForAllTest(){
        instrument = new Instrument(sql);
    }

    @Test
    public void shouldInstrumentHeadAndTail(){
        assertTrue(instrument.getInstrumentSQL().startsWith(Instrument.coverageDeclare));
        assertTrue(instrument.getInstrumentSQL().endsWith(Instrument.coverageEnd));
    }
}
