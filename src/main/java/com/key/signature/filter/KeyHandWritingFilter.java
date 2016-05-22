package com.key.signature.filter;

import com.key.signature.domain.UserRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by pyshankov on 22.05.2016.
 */


public class KeyHandWritingFilter implements Filter {

    private UserRepository userRepository;

    public KeyHandWritingFilter(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}