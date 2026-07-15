package com.ca.cafe_uni.config;

import com.ca.cafe_uni.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEventListener {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(AuthEventListener.class);


    @EventListener
    public void onLoginFallido(AuthenticationFailureBadCredentialsEvent event) {
        String ip = request.getRemoteAddr();
        logger.warn("Intento de login fallido desde IP: {}", ip);
        loginAttemptService.loginFallido(ip);
    }


    @EventListener
    public void onLoginExitoso(AuthenticationSuccessEvent event) {
        String ip = request.getRemoteAddr();
        logger.info("Login exitoso desde IP: {}", ip);
        loginAttemptService.loginExitoso(ip);
    }
}