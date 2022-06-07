package org.pockettech.qiusheng.impl.admin;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.dao.AdminDao;
import org.pockettech.qiusheng.entity.userrole.Admin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Slf4j
@Controller
public class AdminDetailService implements UserDetailsService {
    @Resource
    private AdminDao adminDao;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminDao.loadAdminByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin account not found.");
        }

        String encodePassword = passwordEncoder.encode(admin.getPassword());
        log.info("Password(encrypted):" + encodePassword);
        admin.setPassword(encodePassword);
        return admin;
    }
}
