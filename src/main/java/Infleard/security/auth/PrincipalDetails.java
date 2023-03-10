package Infleard.security.auth;

import Infleard.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 *  Security 가 login 요청시 로그인을 진행
 *  로그인 진행 완료되면 Security Session 생성. (Security ContextHolder)
 *  오브젝트 타입 => Authentication 타입 객체
 *  Authentication 안에 User 정보가 있어야함.
 *  User 오브젝트 타입 => UserDetails 타입객체
 *
 *  Security Session => Authentication => UserDetails
 */
public class PrincipalDetails implements UserDetails {

    private User user; // 콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 유저의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        user.getRole();
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(user.getRole());
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

        // 1년동안 회원이 로그인 하지 않으면 휴면 계정으로 변경하기록 햇다면
        // 현재시간 - 로그인 시간 => 1년을 초과시 return false;

        return true;
    }
}
