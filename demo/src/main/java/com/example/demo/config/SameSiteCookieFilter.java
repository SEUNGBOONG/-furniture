package com.example.demo.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (response instanceof HttpServletResponse httpServletResponse) {
            HttpServletResponse wrappedResponse = new HttpServletResponseWrapper(httpServletResponse) {
                @Override
                public void addCookie(Cookie cookie) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; Path=")
                            .append(cookie.getPath() == null ? "/" : cookie.getPath());

                    if (cookie.getMaxAge() > 0) {
                        sb.append("; Max-Age=").append(cookie.getMaxAge());
                    }

                    if (cookie.getSecure()) sb.append("; Secure");
                    if (cookie.isHttpOnly()) sb.append("; HttpOnly");

                    // 여기 추가됨!
                    sb.append("; SameSite=None");

                    if (cookie.getDomain() != null) {
                        sb.append("; Domain=").append(cookie.getDomain());
                    }

                    super.addHeader("Set-Cookie", sb.toString());
                }
            };

            chain.doFilter(request, wrappedResponse);
        } else {
            chain.doFilter(request, response);
        }
    }
}
