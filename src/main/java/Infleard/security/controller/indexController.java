package Infleard.security.controller;

import Infleard.security.repository.UserRepository;
import Infleard.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
}
