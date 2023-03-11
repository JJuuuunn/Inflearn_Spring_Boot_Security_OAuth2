package Infleard.security.controller;

import Infleard.security.config.auth.PrincipalDetails;
import Infleard.security.repository.UserRepository;
import Infleard.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static Infleard.security.model.Role.ROLE_USER;

@Controller // View 리턴
@RequiredArgsConstructor
public class indexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { //DI 의존성 주입
        System.out.println("/test/login ==============");
        System.out.println("authentication : " + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { //DI 의존성 주입
        System.out.println("/test/login ==============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("attributes : " + oAuth2User.getAttributes());
        System.out.println("oauth2USer : " + oauth.getAttributes());
        return "OAuth 정보 확인하기";
    }

    @GetMapping({"","/"})
    public String index() {
        // Mustache 기본 폴더 src/main/resources/
        // viewResolver 설정 : templates (prefix), .mustache (suffix) 생략가능
        return "index"; // ser/main/resources/templates/index/mustache
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // Spring Security 에서 해당 주소를 낚아채감
    // SecurityConfig 파일 생성후 낚아쳐가지 않음
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        userRepository.save(new User(user.getUsername(), bCryptPasswordEncoder.encode(user.getPassword()), user.getEmail(), ROLE_USER));
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터 정보";
    }
}
