package com.example.demo.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        chain.doFilter(request, response);

        if (response instanceof HttpServletResponse res) {
            Collection<String> headers = res.getHeaders("Set-Cookie");
            boolean first = true;
            for (String header : headers) {
                // 이미 SameSite 설정이 있으면 중복 방지
                if (header.toLowerCase().contains("samesite")) continue;

                String updatedHeader = header + "; SameSite=None";
                if (first) {
                    res.setHeader("Set-Cookie", updatedHeader);
                    first = false;
                } else {
                    res.addHeader("Set-Cookie", updatedHeader);
                }
            }
        }
    }
}
