package org.pockettech.qiusheng.cache;

import org.pockettech.qiusheng.entity.Chart;

import java.util.HashMap;

/**
 * 缓存数据，会定时清理
 */
public class CacheData {

    /**
     * 签名缓存
     */
    public static HashMap<Integer, String> signMap = new HashMap<>();

    /**
     * 文件md5值缓存
     */
    public static HashMap<String, Integer> hashMap = new HashMap<>();

    /**
     * 谱面信息缓存
     */
    public static HashMap<Integer, Chart> hashChart = new HashMap<>();
}
