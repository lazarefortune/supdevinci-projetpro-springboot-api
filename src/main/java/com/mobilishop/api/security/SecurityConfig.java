package com.mobilishop.api.security;

import com.mobilishop.api.enums.AppUserRole;
import com.mobilishop.api.filter.CustomAuthenticationFilter;
import com.mobilishop.api.filter.CustomAuthorizationFilter;
import com.mobilishop.api.handler.AccessDeniedHandlerJwt;
import com.mobilishop.api.handler.AuthenticationEntryPointJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationEntryPointJwt authenticationEntryPointJwt;

    @Autowired
    private AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
/*
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/login/**").permitAll();
        http.authorizeRequests().antMatchers( GET, "/api/v1/users/**" ).hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter( customAuthenticationFilter );
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
*/
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/v1/login",
                        "/api/v*/register" ,
                        "/api/v*/register/*",
                        "/api/v1/token/refresh/**",
                        "/api/v*/products/**",
                        "/api/v*/products/*",
                        "/api/v*/categories/**"
                ).permitAll()
                .antMatchers("/api/v*/users/**").hasAnyAuthority(
                        AppUserRole.ROLE_USER.name(),
                        AppUserRole.ROLE_ADMIN.name(),
                        AppUserRole.ROLE_SUPER_ADMIN.name(),
                        AppUserRole.ROLE_CUSTOMER.name()
                )
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPointJwt)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerJwt)
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

