package com.leyou;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class test {
    @Test
    public void test1(){
        String s = StringUtils.substringAfterLast("as.da.sd.gds", ".");
        System.out.println(s);
    }
}
