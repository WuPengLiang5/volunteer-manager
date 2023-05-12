package com.wpl.volunteer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@JsonIgnoreProperties({"enabled","accountNonExpired", "accountNonLocked", "credentialsNonExpired", "authorities"})
public class SecurityUser implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private String salt;
    private Role role;
//    /**
//     * 当前登录用户
//     * 将不需要序列化的属性前添加关键字transient，序列化对象的时候，这个属性就不会被序列化。
//     */
//    private transient Admin currentUserInfo;

    /**
     * 角色
     */
//    private transient Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);
        }

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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
