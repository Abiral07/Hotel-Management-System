package com.SpringBootProject.hms.config;

import com.SpringBootProject.hms.constants.PathConstant;
import com.SpringBootProject.hms.utils.jwt.JwtAuthenticationEntryPoint;
import com.SpringBootProject.hms.utils.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//allows customization to both WebSecurity and HttpSecurity
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    UserDetailsService jwtUserDetailsService;
    private static final String[] WHITE_LIST_URLS = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            PathConstant.REGISTRATION,
            PathConstant.VERIFY_REGISTRATION,
            PathConstant.RESEND_VERIFICATION,
            PathConstant.LOGIN,
            PathConstant.CHANGE_PASSWORD,
            PathConstant.FORGET_PASSWORD,
            PathConstant.CURRENT_USER
    };
    private static final String[] USER_URL = {
            PathConstant.ADD_RESERVATION,
            PathConstant.GET_ALL_ROOM,
            PathConstant.GET_ROOM_BY_ID,
            PathConstant.GET_ROOM_BY_TYPE,
            PathConstant.GET_RESERVATION_OF_USER,
            PathConstant.GET_USER_BY_ID

    };
    private static final String[] VIEW_URLS = {
            PathConstant.GET_ALL_USERS,
            PathConstant.GET_USER_BY_NAME,
            PathConstant.GET_USER_BY_ID,
            PathConstant.GET_ALL_RESERVATION,
            PathConstant.GET_RESERVATION_BY_ID,
            PathConstant.GET_RESERVATION_OF_TODAY
    };
    private static final String[] ADMIN_URLS = {
            PathConstant.UPDATE_USER,
            PathConstant.UPDATE_ROOM,
            PathConstant.ADD_ROOM,
            PathConstant.UPDATE_RESERVATION,
            PathConstant.GET_ALL_ROLES,
            PathConstant.GET_ROLE_BY_NAME,
            PathConstant.ADD_ROLE,
            PathConstant.UPDATE_ROLE,
            PathConstant.ADD_ROLE_TO_USER
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .antMatchers(USER_URL).hasRole("USER")
                .antMatchers(VIEW_URLS).hasRole("VIEW")
                .antMatchers(ADMIN_URLS).hasRole("ADMIN")
                .antMatchers("/**").authenticated()
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic();
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // This line provides passwords to be encoded using BCrypt
        provider.setUserDetailsService(jwtUserDetailsService);
        return provider;
    }
}

