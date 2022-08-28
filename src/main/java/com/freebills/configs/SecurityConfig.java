package com.freebills.configs;

import com.freebills.auth.JWTAuthenticationFilter;
import com.freebills.auth.JWTAuthorizationFilter;
import com.freebills.auth.service.CustomUserDetailService;
import com.freebills.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.freebills.auth.SecurityConstants.LOGIN;


@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailService customUserDetailService;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().headers().frameOptions().disable().and().csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.GET, LOGIN).permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/*/logout/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/*/forget/**").permitAll()

                //user roles
                .antMatchers("/*/me/**").hasRole("USER")

                //admin role
                .antMatchers("/*/user/**").hasRole("ADMIN")

                //permits
                .antMatchers("/*/actuator/**").permitAll()
                .antMatchers("/*/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userRepository))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailService));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
