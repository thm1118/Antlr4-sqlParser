DECLARE
    -- 定义单个语句（或分支）的覆盖率 record 数据结构
    TYPE t_cov_rec_type IS RECORD (id_row INTEGER,id_col INTEGER, exc_count INTEGER default 0);
    -- 定义 存储多个 语句覆盖率的 table 数据结构
    TYPE t_statements_cov_tab_type IS TABLE OF t_cov_rec_type INDEX BY PLS_INTEGER;
    -- 定义 存储分支语句覆盖率的 table 数据结构
    TYPE t_branches_cov_tab_type IS TABLE OF t_cov_rec_type INDEX BY PLS_INTEGER;
    -- 初始化 语句覆盖率的 table
    statements_cov_tab t_statements_cov_tab_type;
    branches_cov_tab t_branches_cov_tab_type;
    -- 初始化tabel中的每个语句和分支
    statements_cov_tab(1).id_row := 1;
    statements_cov_tab(1).id_col := 0;
    statements_cov_tab(2).id_row := 6;
    statements_cov_tab(2).id_col := 4;
    statements_cov_tab(3).id_row := 7;
    statements_cov_tab(3).id_col := 4;
    statements_cov_tab(4).id_row := 8;
    statements_cov_tab(4).id_col := 8;
    statements_cov_tab(5).id_row := 10;
    statements_cov_tab(5).id_col := 8;
    statements_cov_tab(6).id_row := 11;
    statements_cov_tab(6).id_col := 34;
    statements_cov_tab(7).id_row := 13;
    statements_cov_tab(7).id_col := 8;
    branches_cov_tab(1).id_row := 7;
    branches_cov_tab(1).id_col := 4;
    branches_cov_tab(2).id_row := 9;
    branches_cov_tab(2).id_col := 4;
    branches_cov_tab(3).id_row := 11;
    branches_cov_tab(3).id_col := 4;
    branches_cov_tab(4).id_row := 12;
    branches_cov_tab(4).id_col := 4;

BEGIN

    statements_cov_tab(1).exc_count := statements_cov_tab(1).exc_count + 1;
    DECLARE
        JOBID     EMPLOYEES.JOB_ID%TYPE;
        EMPID     EMPLOYEES.EMPLOYEE_ID%TYPE := 115;
        SAL_RAISE NUMBER(3, 2);
    BEGIN

        statements_cov_tab(2).exc_count := statements_cov_tab(2).exc_count + 1;
        SELECT JOB_ID INTO JOBID FROM EMPLOYEES WHERE EMPLOYEE_ID = EMPID;

        statements_cov_tab(3).exc_count := statements_cov_tab(3).exc_count + 1;
        IF JOBID = 'PU_CLERK' THEN
            branches_cov_tab(1).exc_count := branches_cov_tab(1).exc_count + 1;


            statements_cov_tab(4).exc_count := statements_cov_tab(4).exc_count + 1;
            SAL_RAISE := .09;
        ELSIF JOBID = 'SH_CLERK' THEN
            branches_cov_tab(2).exc_count := branches_cov_tab(2).exc_count + 1;


            statements_cov_tab(5).exc_count := statements_cov_tab(5).exc_count + 1;
            SAL_RAISE := .08;
        ELSIF JOBID = 'ST_CLERK' THEN
            branches_cov_tab(3).exc_count := branches_cov_tab(3).exc_count + 1;

            statements_cov_tab(6).exc_count := statements_cov_tab(6).exc_count + 1;
            SAL_RAISE := .07;
        ELSE
            branches_cov_tab(4).exc_count := branches_cov_tab(4).exc_count + 1;


            statements_cov_tab(7).exc_count := statements_cov_tab(7).exc_count + 1;
            SAL_RAISE := 0;
        END IF;
    END;
    -- 输出结果
    dbms_output.put_line('语句覆盖率情况：');
    FOR i IN statements_cov_tab.first .. statements_cov_tab.last
        LOOP
            dbms_output.put_line('第'||statements_cov_tab(i).id_row ||'行，第'||statements_cov_tab(i).id_col||' 列 起始的语句的覆盖次数 '||statements_cov_tab(i).exc_count);
        END LOOP;
    -- 总语句数
    dbms_output.put_line('总语句数量为：'||statements_cov_tab.count);

    dbms_output.put_line('分支覆盖率情况：');
    FOR i IN branches_cov_tab.first .. branches_cov_tab.last
        LOOP
            dbms_output.put_line('第'||branches_cov_tab(i).id_row ||'行，第'||branches_cov_tab(i).id_col||' 列 起始的分支覆盖次数 '||branches_cov_tab(i).exc_count);
        END LOOP;
    -- 总分支数
    dbms_output.put_line('总分支分支数量为：'||branches_cov_tab.count);
END;
