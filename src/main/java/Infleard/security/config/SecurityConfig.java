package Infleard.security.config;

import Infleard.security.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션(Secured, PreAuthorize) 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

//    // 메소드 리턴되는 오브젝트를 IoC로 등록
//    // Password Encoding 메소드
//    @Bean
//    public BCryptPasswordEncoder encodePwd() {
//        return new BCryptPasswordEncoder();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 해당 주소로 접근 가능
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 해당 주소는 ADMIN 또는 MANAGER 권한이 필요
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //// 해당 주소는 ADMIN 권한이 필요
                .anyRequest().permitAll() // 나머지 주소는 전부 허용
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // login 호출시 Security 에서 로그인 진행
                .defaultSuccessUrl("/") // 로그인 성공 후에 로그인 화면 전 경로로 이동
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
        /** OAuth 로그인 후 후처리 필요
         *  1. 코드 받기(인증)
         *  2. 엑세스 토큰(권한)
         *  3. 사용자프로필 정보 가져오기
         *  4. 가져온 정보로 회원가입 자동 진행
         *  Tip. 구글 로그인 완료시 코드로 받지않고 (엑세스토큰 + 사용자 프로필 정보)를 바로 받음
         */
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        ;
    }
}
