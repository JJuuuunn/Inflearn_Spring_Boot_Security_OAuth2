package Infleard.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View 리턴
public class indexController {

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

    @GetMapping("/join")
    public @ResponseBody String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() {
        return "회원 가입 완료됨";
    }
}
