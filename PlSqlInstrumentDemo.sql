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
  -- test variable
  v_test_string VARCHAR2(255);
  v_test_id INTEGER;
BEGIN
   -- 填数据
   statements_cov_tab(1).id_row := 2;
   statements_cov_tab(1).id_col := 3;
   statements_cov_tab(1).exc_count := statements_cov_tab(1).exc_count + 1;
    DECLARE
       jobid VARCHAR2(255) := 'PU_CLERK';
       empid NUMBER := 115;
       sal_raise  NUMBER(3,2);
    BEGIN

   statements_cov_tab(2).id_row := 7;
   statements_cov_tab(2).id_col := 3;
   statements_cov_tab(2).exc_count := statements_cov_tab(2).exc_count + 1;

   branches_cov_tab(1).id_row := 7;
   branches_cov_tab(1).id_col := 3;
   branches_cov_tab(1).exc_count := branches_cov_tab(1).exc_count + 1;
    IF jobid = 'PU_CLERK' THEN sal_raise := .09;
      ELSIF jobid = 'SH_CLERK' THEN
          sal_raise := .08;
          branches_cov_tab(2).id_row := 8;
           branches_cov_tab(2).id_col := 3;
           branches_cov_tab(2).exc_count := branches_cov_tab(2).exc_count + 1;
      ELSIF jobid = 'ST_CLERK' THEN
          sal_raise := .07;
           branches_cov_tab(3).id_row := 9;
           branches_cov_tab(3).id_col := 3;
           branches_cov_tab(3).exc_count := branches_cov_tab(3).exc_count + 1;

    ELSE sal_raise := 0;
         branches_cov_tab(4).id_row := 10;
           branches_cov_tab(4).id_col := 3;
           branches_cov_tab(4).exc_count := branches_cov_tab(4).exc_count + 1;
    END IF;

    statements_cov_tab(3).id_row := 2;
   statements_cov_tab(3).id_col := 3;
   statements_cov_tab(3).exc_count := statements_cov_tab(3).exc_count + 1;
    dbms_output.put_line(sal_raise);
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