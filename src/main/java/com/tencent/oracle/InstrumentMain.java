package com.tencent.oracle;

public class InstrumentMain {
    public static void main(String[] args){

//        String sql = "CREATE PROCEDURE REMOVE_EMP (EMPLOYEE_ID NUMBER) AS\n" +
//                "   TOT_EMPS NUMBER;\n" +
//                "   BEGIN\n" +
//                "      DELETE FROM EMPLOYEES\n" +
//                "      WHERE EMPLOYEES.EMPLOYEE_ID = REMOVE_EMP.EMPLOYEE_ID;\n" +
//                "   TOT_EMPS := TOT_EMPS - 1;\n" +
//                "   END;\n" +
//                "   \n" +
//                "SELECT * FROM FOO;\n" +
//                "/* 注释不会解析 */\n" +
//                "/* DELETE * FROM BAR; */";

        String sql = "DECLARE\n" +
                "   jobid      employees.job_id%TYPE;\n" +
                "   empid      employees.employee_id%TYPE := 115;\n" +
                "   sal_raise  NUMBER(3,2);\n" +
                "BEGIN\n" +
                "SELECT job_id INTO jobid from employees WHERE employee_id = empid;\n" +
                "IF jobid = 'PU_CLERK' THEN sal_raise := .09;\n" +
                "  ELSIF jobid = 'SH_CLERK' THEN sal_raise := .08;\n" +
                "  ELSIF jobid = 'ST_CLERK' THEN sal_raise := .07;\n" +
                "ELSE sal_raise := 0;\n" +
                "END IF;\n" +
                "END;";

        Instrument instrument = new Instrument(sql);

        System.out.println("-------------原始SQL----------------------");
        System.out.println(sql);
        // 输出 注入覆盖率计数器 的SQL
        System.out.println("-------------注入覆盖率计数器 的SQL---------:");
        System.out.println(instrument.getInstrumentSQL());
    }
}
