package org.pockettech.qiusheng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pockettech.qiusheng.entity.Admin;

@Mapper
public interface AdminMapper {
    public Admin getAdminByUsername(String username);

    public Admin getAdminById(Integer userId);
}
