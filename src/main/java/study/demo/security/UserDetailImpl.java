package study.demo.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    
    private Integer userId;
    
    private String userName;
    
    private String password;
    
    private List<GrantedAuthority> authorities;

    public UserDetailImpl(String userName, String password, List<GrantedAuthority> authorities) {
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
    }

    public UserDetailImpl(Integer userId, String userName, String password, List<GrantedAuthority> authorities) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "CustomUserDetails [userId=" + userId + ", userName=" + userName + ", password=" + password
                + ", authorities=" + authorities + "]";
    }

}
