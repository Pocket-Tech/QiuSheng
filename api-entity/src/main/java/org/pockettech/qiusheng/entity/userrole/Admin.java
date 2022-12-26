package org.pockettech.qiusheng.entity.userrole;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.pockettech.qiusheng.entity.data.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class Admin implements UserDetails {

    private User user;
    private boolean enable;
    private boolean locked;

    private List<String> permissions;

    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities;

    public Admin(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
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
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUser_name();
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
