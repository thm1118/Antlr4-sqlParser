package com.tencent.oracle.config;

import org.antlr.v4.runtime.Token;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class CoverageDataConfig {
    // todo: 这里为了演示插桩用了文字，需要设计 覆盖率数据声明头，结尾，增量计数器（语句、分支、函数（含存储过程、触发器等））
    /**
     *  注入的头，需要 对所有 行覆盖和分支覆盖数据的初始化
     * */
    private  final String coverageDeclareTemplate = "DECLARE\n" +
            "  -- 定义单个语句（或分支）的覆盖率 record 数据结构\n" +
            "  TYPE t_cov_rec_type IS RECORD (id_row INTEGER,id_col INTEGER, exc_count INTEGER default 0);\n" +
            "  -- 定义 存储多个 语句覆盖率的 table 数据结构\n" +
            "  TYPE t_statements_cov_tab_type IS TABLE OF t_cov_rec_type INDEX BY PLS_INTEGER;\n" +
            "    -- 定义 存储分支语句覆盖率的 table 数据结构\n" +
            "  TYPE t_branches_cov_tab_type IS TABLE OF t_cov_rec_type INDEX BY PLS_INTEGER;\n" +
            "  -- 初始化 语句覆盖率的 table\n" +
            "  statements_cov_tab t_statements_cov_tab_type;\n" +
            "  branches_cov_tab t_branches_cov_tab_type;\n" +
            "  -- 初始化tabel中的每个语句和分支" +
            "  %s \n " + // 这里注入 所有 行覆盖和分支覆盖数据的初始化；
            "BEGIN\n";
    /**
     * 注入的 尾： todo： 需要对收集的数据 存放到 物理表/或 发送至 远程服务
     * */
    private final String  coverageEndTemplate = "\n  -- 输出结果\n" +
            "   dbms_output.put_line('语句覆盖率情况：');\n" +
            "   FOR i IN statements_cov_tab.first .. statements_cov_tab.last\n" +
            "   LOOP\n" +
            "       dbms_output.put_line('第'||statements_cov_tab(i).id_row ||'行，第'||statements_cov_tab(i).id_col||' 列 起始的语句的覆盖次数 '||statements_cov_tab(i).exc_count);\n" +
            "   END LOOP;\n" +
            "    -- 总语句数\n" +
            "   dbms_output.put_line('总语句数量为：'||statements_cov_tab.count);\n" +
            "\n" +
            "   dbms_output.put_line('分支覆盖率情况：');\n" +
            "   FOR i IN branches_cov_tab.first .. branches_cov_tab.last\n" +
            "   LOOP\n" +
            "       dbms_output.put_line('第'||branches_cov_tab(i).id_row ||'行，第'||branches_cov_tab(i).id_col||' 列 起始的分支覆盖次数 '||branches_cov_tab(i).exc_count);\n" +
            "   END LOOP;\n" +
            "    -- 总分支数\n" +
            "   dbms_output.put_line('总分支分支数量为：'||branches_cov_tab.count);\n" +
            "END;\n";
    /**
     *  单条语句覆盖初始化
     * */
    private  final String coverageStatementIncrementorInitTemplate =
            "statements_cov_tab(%d).id_row := %d;\n" +
                    "statements_cov_tab(%d).id_col := %d; \n";
    /**
     * 单条分支 覆盖初始化
     * */
    private  final String coverageBranchIncrementorInitTemplate =
            "branches_cov_tab(%d).id_row := %d;\n" +
                    "branches_cov_tab(%d).id_col := %d; \n";
    /**
     * 单条语句覆盖收集器模板
     * */
    private final String coverageStatementIncrementor =
            " \nstatements_cov_tab(%d).exc_count := statements_cov_tab(%d).exc_count + 1; \n";

    /**
     * 单条分支覆盖收集器模板
     * */
    private final String coverageBranchBeforeIncrementor =
            " \nbranches_cov_tab(%d).exc_count := branches_cov_tab(%d).exc_count + 1; \n";

    /**
     * 单条分支覆盖收集器模板
     * */
    private final String coverageBranchAfterIncrementor =
            " \n(branches_cov_tab(%d).exc_count := branches_cov_tab(%d).exc_count + 1) AND \n";



    private String getCoverageStatementIncrementorInit(CovData covData){
        return String.format(this.coverageStatementIncrementorInitTemplate,
                covData.seq, covData.row, covData.seq , covData.col);
    }

    private String getCoverageBranchIncrementorInit(CovData covData){
        return String.format(this.coverageBranchIncrementorInitTemplate,
                covData.seq, covData.row, covData.seq, covData.col);
    }

    /*(
    *  语句和分支
    * */
    static class CovData
    {
        public int seq;
        public int row;
        public int col;
    };

    private final ArrayList<CovData> statementInitData = new ArrayList<>(100);
    private final ArrayList<CovData> branchInitData = new ArrayList<>(100);


    public int addStatement(int row, int col) {
        CovData covData = new CovData();
        covData.row = row;
        covData.col = col;
        covData.seq = statementInitData.size() + 1 ;
        statementInitData.add(covData);
        return covData.seq;
    }

    public int addBranch(int row, int col) {
        CovData covData = new CovData();
        covData.row = row;
        covData.col = col;
        covData.seq = branchInitData.size() + 1 ;
        branchInitData.add(covData);
        return covData.seq;
    }

    /**
     * todo: 返回 头 需要的 覆盖率初始化模板内容；
     * */
    private String getInitCovData() {
        StringBuilder covAllInitData = new StringBuilder(200);
        for (CovData statementInitDatum : statementInitData) {
            covAllInitData.append(getCoverageStatementIncrementorInit(statementInitDatum));
        }
        for (CovData branchInitDatum : branchInitData) {
            covAllInitData.append(getCoverageBranchIncrementorInit(branchInitDatum));
        }
        return covAllInitData.toString();
    }

    /**
     * 获取用来 单条语句覆盖率收集器
     * */
    public String getCoverageStatementIncrementor(int seq){
        return String.format(coverageStatementIncrementor, seq, seq);
    }

    /**
     * 获取用来 单条分支覆盖率收集器
     * */
    public String getCoverageBranchIncrementor(int seq,boolean isBefore){
        if (isBefore)
            return String.format(coverageBranchBeforeIncrementor, seq, seq);
        else return String.format(coverageBranchAfterIncrementor, seq, seq);
    }

    /*
    *  获取 整体 注入头
    * */
    public String getCoverageDeclare(){
        return String.format(coverageDeclareTemplate, getInitCovData());
    }

    public String getCoverageEnd() {
        return this.coverageEndTemplate;
    }


}
