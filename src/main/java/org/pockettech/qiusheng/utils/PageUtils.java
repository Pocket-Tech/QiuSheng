package org.pockettech.qiusheng.utils;

import com.github.pagehelper.PageHelper;
import org.pockettech.qiusheng.entity.page.PageDomain;
import org.pockettech.qiusheng.entity.page.TableSupport;

/**
 * 分页工具类
 */
public class PageUtils extends PageHelper {

    /**
     * 设置请求分页数据(默认一页10条)
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest(null, null);
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 设置请求分页数据(可控单页数量)
     */
    public static void startPage(Integer size, Integer num)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest(size, num);
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }

}
