package com.example.springboot.jwt.application.config;

import com.example.springboot.jwt.application.controller.ControllerConstants;
import com.example.springboot.jwt.application.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author Prashant Patel
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${com.example.user.role}")
    private String userRole;

    @Value("${com.example.admin.role}")
    private String adminRole;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Temporally create two inmemory users
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("prashant")
                .password(encoder.encode("Welcome1234"))
                .roles(userRole)
                .and()
                .withUser("prashant-invalid")
                .password(encoder.encode("Welcome1234"))
                .roles()
                .and()
                .withUser("prashant-admin")
                .password(encoder.encode("Welcome1234"))
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles(adminRole);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(ControllerConstants.SERVICE_ROOT + ControllerConstants.LOGIN)
                .antMatchers(ControllerConstants.HEALTH + "/**")
                .antMatchers(ControllerConstants.H2_CONSOLE + "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //permit access to this urls
        http.authorizeRequests()
                .antMatchers(ControllerConstants.ADMIN_ROOT + "/**").hasRole(adminRole)
                .antMatchers(ControllerConstants.SERVICE_ROOT + "/**").hasAnyRole(userRole, adminRole)
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /*
     *
     * Prevent JwtAuthenticationFilter to be added to Spring Boot filter chain.
     * Only Spring Security must use it.
     */
/*

    @Bean
    public FilterRegistrationBean registration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
*/

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
