package com.tencent.oracle;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);

        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }
}
