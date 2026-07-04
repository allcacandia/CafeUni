package com.ca.cafe_uni.config;

import com.ca.cafe_uni.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEventListener {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @EventListener
    public void onLoginFallido(AuthenticationFailureBadCredentialsEvent event) {
        String ip = request.getRemoteAddr();
        loginAttemptService.loginFallido(ip);
    }

    @EventListener
    public void onLoginExitoso(AuthenticationSuccessEvent event) {
        String ip = request.getRemoteAddr();
        loginAttemptService.loginExitoso(ip);
    }
}