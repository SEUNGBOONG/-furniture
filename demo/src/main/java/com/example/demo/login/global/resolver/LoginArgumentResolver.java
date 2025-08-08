package com.example.demo.login.global.resolver;

import com.example.demo.login.global.annotation.Member;
import com.example.demo.login.global.application.JwtTokenService;
import com.example.demo.login.member.exception.exceptions.auth.NotFoundTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_HEADER_NAME = "Authorization";

    private final JwtTokenService jwtTokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginAnnotation = parameter.hasParameterAnnotation(Member.class);
        boolean isMemberIdType = Long.class.isAssignableFrom(parameter.getParameterType());
        return isLoginAnnotation && isMemberIdType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = null;
        String tokenHeader = request.getHeader(TOKEN_HEADER_NAME);
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
        }

        if (token == null) {
            if (request.getCookies() != null) {
                token = Arrays.stream(request.getCookies())
                        .filter(cookie -> "token".equals(cookie.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
            }
        }

        if (token == null) {
            throw new NotFoundTokenException();
        }

        return jwtTokenService.verifyAndExtractJwtToken(token);
    }
}
