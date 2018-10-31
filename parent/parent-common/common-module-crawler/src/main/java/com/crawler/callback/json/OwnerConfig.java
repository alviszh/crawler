package com.crawler.callback.json;

import java.util.HashMap;

public class OwnerConfig {
    public static HashMap<String, Integer> ownerMap;

    public static final String TIANXI = "tianxi";//天曦
    public static final String HUICHENG = "huicheng";//汇城
    public static final String HUIJIN = "huijin";//汇金
    public static final String YANJIUYUAN = "yanjiuyuan";//研究院（薪动钱包）
    public static final String JINXINWANG = "jinxinwang";//金信网（贷乎）
    public static final String DAJINRONG = "dajinrong";//大金融（借么）

    public static final int TIANXI_INT = 0;
    public static final int HUICHENG_INT = 1;
    public static final int YANJIUYUAN_INT = 2;
    public static final int JINXINWANG_INT = 3;
    public static final int DAJINRONG_INT = 4;

    public static HashMap<String, Integer> getOwnerMap() {
        if (ownerMap == null) {
            ownerMap = new HashMap<>();
            ownerMap.put(TIANXI, TIANXI_INT);
            ownerMap.put(HUICHENG, HUICHENG_INT);
            ownerMap.put(YANJIUYUAN, YANJIUYUAN_INT);
            ownerMap.put(JINXINWANG, JINXINWANG_INT);
            ownerMap.put(DAJINRONG, DAJINRONG_INT);
        }
        return ownerMap;
    }
}
