package com.bloghub.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final String domain;
    private final String logoutRedirectUrl;
    private final String userPoolClientId;

    public CognitoLogoutHandler(
            @Value("${cognito.logout.domain}") String domain,
            @Value("${cognito.logout.redirect-url}") String logoutRedirectUrl,
            @Value("${spring.security.oauth2.client.registration.cognito.client-id}") String userPoolClientId) {
        this.domain = domain;
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.userPoolClientId = userPoolClientId;
    }

    @Override
    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        if (domain == null || domain.isBlank()) {
            return logoutRedirectUrl;
        }

        return UriComponentsBuilder
                .fromUri(URI.create(domain + "/logout"))
                .queryParam("client_id", userPoolClientId)
                .queryParam("logout_uri", logoutRedirectUrl)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}
