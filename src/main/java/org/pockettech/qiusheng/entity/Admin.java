package org.pockettech.qiusheng.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements UserDetails {

    /** 主键id */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 角色 */
    private String role;

    /**  */
    private boolean enable;

    /** 上锁（禁止登录） */
    private boolean locked;

    /** 权限列表 */
    private List<String> permissions;

    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Admin(Integer id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        if (role == null || "".equals(role)) {
            this.permissions = new ArrayList<>();
        } else {
            this.permissions = Arrays.asList(role.split(","));
        }
    }

    public Admin(Integer id, String username, String role, boolean locked) {
        this.id = id;
        this.username = username;
        this.role = role;
        if (role == null || "".equals(role)) {
            this.permissions = new ArrayList<>();
        } else {
            this.permissions = Arrays.asList(role.split(","));
        }
        this.locked = locked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null) {
            return authorities;
        }

        List<GrantedAuthority> newList = new ArrayList<>();
        for (String permission : permissions) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
            newList.add(authority);
        }
        authorities = newList;
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean getIsLocked() {
        return locked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
