package org.pockettech.qiusheng.cache;

import org.pockettech.qiusheng.entity.Chart;

import java.util.HashMap;

/**
 * 缓存数据，会定时清理
 */
public class CacheData {

    /**
     * 拥有过期时间的缓存属性名
     */
    public static final String[] TIME_FIELD = {
            "tokenCache",
    };

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

    /**
     * 令牌缓存
     */
    public static HashMap<String, Object> tokenCache = new HashMap<>();
}
