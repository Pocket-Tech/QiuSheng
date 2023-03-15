package org.pockettech.qiusheng.entity.page;

import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.utils.Convert;
import org.pockettech.qiusheng.utils.ServletUtils;

/**
 * 表格数据处理
 */
public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "from";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "size";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain(Integer size, Integer num)
    {
        size = size == null ? SystemConfig.PAGE_NUM : size;
        num = num == null ? 1 : num;
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), num));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), size));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static PageDomain buildPageRequest(Integer size, Integer num)
    {
        return getPageDomain(size, num);
    }
}
