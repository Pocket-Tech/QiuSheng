package org.pockettech.qiusheng.api;

import javax.servlet.http.HttpServletRequest;

//TODO:做分布式的时候要用
//返回所有已知QiuSheng服务器的地址列表的服务接口
public interface RepoSignService {
    public int repoRegister(HttpServletRequest request);
}
