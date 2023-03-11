package Infleard.security.config.auth;

import Infleard.security.model.User;
import Infleard.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Security 설정에서 loginProcessingUrl("/login") 요청이 오면
// 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // UserDetails 가 Authentication 내부에 들어가고
    // Authentication 가 Security Session 내부에 들어감
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;
    }
}
