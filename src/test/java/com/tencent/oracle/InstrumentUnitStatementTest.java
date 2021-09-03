package com.tencent.oracle;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * 验证 所有DDL，DML 等 语句 的正确插桩
 * **/
public class InstrumentUnitStatementTest {

    // -------------- DDL 语句 ----------------------
    @Test
    public void shouldInstrumentCreateTableStatement(){
        Instrument instrument;
        String sql = "CREATE TABLE hr.admin_emp (\n" +
                "         empno      NUMBER(5) PRIMARY KEY,\n" +
                "         ename      VARCHAR2(15) NOT NULL,\n" +
                "         ssn        NUMBER(9) ENCRYPT,\n" +
                "         job        VARCHAR2(10),\n" +
                "         mgr        NUMBER(5),\n" +
                "         hiredate   DATE DEFAULT (sysdate),\n" +
                "         photo      BLOB,\n" +
                "         sal        NUMBER(7,2),\n" +
                "         hrly_rate  NUMBER(7,2) GENERATED ALWAYS AS (sal/2080),\n" +
                "         comm       NUMBER(7,2),\n" +
                "         deptno     NUMBER(3) NOT NULL\n" +
                "                     CONSTRAINT admin_dept_fkey REFERENCES hr.departments\n" +
                "                     (department_id))\n" +
                "   TABLESPACE admin_tbs\n" +
                "   STORAGE ( INITIAL 50K); ";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }

    @Test
    public void shouldInstrumentCreateProcedureStatement(){
        Instrument instrument;
        String sql = "CREATE PROCEDURE remove_emp (employee_id NUMBER) AS\n" +
                "   tot_emps NUMBER;\n" +
                "   BEGIN\n" +
                "      DELETE FROM employees\n" +
                "      WHERE employees.employee_id = remove_emp.employee_id;\n" +
                "   tot_emps := tot_emps - 1;\n" +
                "   END; ";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }

    @Test
    public void shouldInstrumentCreateFunctionStatement(){
        Instrument instrument;
        String sql = "CREATE FUNCTION get_bal(acc_no IN NUMBER) \n" +
                "   RETURN NUMBER \n" +
                "   IS acc_bal NUMBER(11,2);\n" +
                "   BEGIN \n" +
                "      SELECT order_total \n" +
                "      INTO acc_bal \n" +
                "      FROM orders \n" +
                "      WHERE customer_id = acc_no; \n" +
                "      RETURN(acc_bal); \n" +
                "    END; ";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }

    // -------------- DML 语句 ----------------------

    @Test
    public void shouldInstrumentSelectStatement(){
        Instrument instrument;
        String sql = "select   *  from\n foo\n  order by x ; ";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }

    @Test
    public void shouldInstrumentInsertStatement(){
        Instrument instrument;
        String sql = "INSERT INTO bonus SELECT ename, job, sal, comm FROM emp\n" +
                "   WHERE comm > sal * 0.25; ";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }

    @Test
    public void shouldInstrumentUpdateStatement(){
        Instrument instrument;
        String sql = "UPDATE e1 SET (first_name, last_name) =\n" +
                "      (SELECT first_name, last_name FROM employees\n" +
                "         WHERE employee_id = e1.employee_id);";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }

    @Test
    public void shouldInstrumentDeleteStatement(){
        Instrument instrument;
        String sql = "DELETE FROM bonus WHERE sales_amt < quota;";
        instrument = new Instrument(sql);
        String result = instrument.getInstrumentSQL();
        List<String> rows = Util.splitToLines(result);
        assertEquals(11, rows.indexOf("  statements_cov_tab(1).id_row := 1;"));
        assertEquals(12, rows.indexOf("statements_cov_tab(1).id_col := 0; "));
    }
}
