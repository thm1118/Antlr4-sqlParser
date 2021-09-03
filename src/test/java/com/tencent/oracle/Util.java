package com.tencent.oracle;

import com.google.common.io.Resources;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Util {

    // String sql = readResource("ifelse.sql");
    public static String readResource(String name)
            throws IOException
    {
        //noinspection UnstableApiUsage
        return Resources.toString(Resources.getResource("oracle/"+name), UTF_8);
    }

    /**
     * 计算 一个字符串 在一个 大字符串中的 出现次数
     * */
    public static int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    public static List<String> splitToLines(String result) {
        return Arrays.asList(result.split("\n"));
    }
}
