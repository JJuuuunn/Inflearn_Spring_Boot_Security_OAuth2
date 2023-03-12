package Infleard.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// 빈 순환 참조 문제로 인해 따로 등록
@Component
public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder {

}