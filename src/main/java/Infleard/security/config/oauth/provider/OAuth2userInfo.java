package Infleard.security.config.oauth.provider;

public interface OAuth2userInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
