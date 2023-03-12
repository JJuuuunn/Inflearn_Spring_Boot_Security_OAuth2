package Infleard.security.config.oauth;


import Infleard.security.config.auth.PrincipalDetails;
import Infleard.security.config.oauth.provider.FacebookUserInfo;
import Infleard.security.config.oauth.provider.GoogleUserInfo;
import Infleard.security.config.oauth.provider.OAuth2userInfo;
import Infleard.security.model.Role;
import Infleard.security.model.User;
import Infleard.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static Infleard.security.model.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    // oauth2 로 부터 받은 userRequest 데이터에 대한 후처리 함수
    // 해당 함수 종료시 @AuthenticationPrincipal 어노테이션 생성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration : " + userRequest.getClientRegistration()); // registration 으로 어떤 OAuth 에 로그인하는지 확인 가능
        System.out.println("userRequest.getAccessToken : " + userRequest.getAccessToken());
        System.out.println("userRequest.getAdditionalParameters : " + userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Oath2 로그인 버튼 클릭 -> 로그인창 -> 로그인 완료 -> code 를 리턴(OAuth-Client 라이브러리) -> Access Token 요청
        // userRequest 정보 -> loadUser 함수 호출 -> OAuth 로부터 회원 프로필 받음.
        System.out.println("userRequest.getAttributes : " + oAuth2User.getAttributes());

        OAuth2userInfo oAuth2userInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2userInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("google facebook 아님");
        }

        String provider = oAuth2userInfo.getProvider();
        String providerId = oAuth2userInfo.getProviderId(); // 102025265846967414199
        String username = provider + "_" + providerId; // google_102025265846967414199
        String password = encoder.encode("겟인데어"); // 의미 없음
        String email = oAuth2userInfo.getEmail();
        Role role = ROLE_USER;

        // OAuth 로그인시 아이디가 존재 하지 않으면 자동 회원가입
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            System.out.println("OAuth 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("이미 로그인한 OAuth 계정 입니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}

