package com.key.signature.security;

import com.key.signature.domain.User;
import com.key.signature.domain.UserRepository;
import com.key.signature.filter.SimpleCORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by pyshankov on 17.05.2016.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Service
    public static class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return new CustomUserDetails(username,userRepository);
        }

        public static class CustomUserDetails implements UserDetails {

            private final SimpleGrantedAuthority USER_ROLE = new SimpleGrantedAuthority("ROLE_USER");
            private final SimpleGrantedAuthority USER_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

            private final Collection<? extends GrantedAuthority> ROLES_USER =
                    Collections.singletonList(USER_ROLE);
            private final Collection<? extends GrantedAuthority> ROLES_USER_AND_ADMIN =
                    Arrays.asList(USER_ROLE, USER_ADMIN);

            private User user;
            private final Collection<? extends GrantedAuthority> roles;

            public CustomUserDetails(final String username,UserRepository userRepository) {
                user = userRepository.findByUserName(username);
                roles = user.getRole()==User.Role.ADMIN ? ROLES_USER_AND_ADMIN : ROLES_USER;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return roles;
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public String getUsername() {
                return user.getUserName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return user.isActivated();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

        }

    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic()
//                .and()
//                .formLogin().loginPage("/login")
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/user/**").permitAll()
//                .antMatchers(HttpMethod.PUT, "/user/**").hasRole("USER")
//                .antMatchers(HttpMethod.PATCH, "/user/**").hasRole("USER")
//                .antMatchers(HttpMethod.DELETE, "/user/**").hasRole("USER")
//                .antMatchers(HttpMethod.GET,"/curUser").permitAll()
//                .antMatchers("/index.html", "/welcomePage.html", "/login.html", "/","registration.html").permitAll()
//                .antMatchers("/login", "/registration","/").permitAll()
//                .antMatchers("/bower_components/**","/partials/**").permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
                .csrf().disable()


////                .headers().frameOptions().sameOrigin()
//                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//                .csrf().csrfTokenRepository(csrfTokenRepository())

        ;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    public static UserDetails  currentPrincipal() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static class CsrfHeaderFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                    .getName());
            if (csrf != null) {
                Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                String token = csrf.getToken();
                if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
                    cookie = new Cookie("XSRF-TOKEN", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
            filterChain.doFilter(request, response);
        }
    }



}
