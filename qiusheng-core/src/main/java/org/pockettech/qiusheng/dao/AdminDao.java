package org.pockettech.qiusheng.dao;

import org.pockettech.qiusheng.entity.userrole.Admin;

public interface AdminDao {
    public Admin loadAdminByUsername(String username);
}
