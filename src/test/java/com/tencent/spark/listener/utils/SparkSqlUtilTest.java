package com.tencent.spark.listener.utils;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by lcc on 2018/12/17.
 */

public class SparkSqlUtilTest {

//    @Test
    @Ignore("不需要")
    public void getDataBaseTablenameAndOper() throws Exception {
        String sql = "insert into table_a (id,name ) select a.id,b.name  from a inner join b on a.id =b.id ";
        Map<String, Set<String>> aa = SparkSqlUtil.getDataBaseTablenameAndOper(sql);

        System.out.println(aa);
    }


//    @Test
    @Ignore("不需要")
    public void getDataBaseTablenameAndOper1() throws Exception {
        String sql = "select a.id,b.name  from a inner join b on a.id =b.id ";
        Map<String, Set<String>> aa = SparkSqlUtil.getDataBaseTablenameAndOper(sql);

        System.out.println(aa);
    }


//    @Test
    @Ignore("不需要")
    public void getDataBaseTablenameAndOper2() throws Exception {
        String sql = "select a.id,b.name  from a inner join b on a.id =b.id ";
        Map<String, Set<String>> aa = SparkSqlUtil.getDataBaseTablenameAndOper2(sql);

        System.out.println(aa);
    }




}