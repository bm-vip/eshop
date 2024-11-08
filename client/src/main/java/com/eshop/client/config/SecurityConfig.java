package com.eshop.client.config;

import com.eshop.client.service.impl.CustomUserDetailsServiceImpl;
import com.eshop.client.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;
    @Autowired
    SuccessLoginConfig successLoginConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/ltr/**","/logout","/notfound","/login","/registration","/send-OTP","/api/v1/country/findAllSelect*","/api/v1/user/register*","/actuator/**").permitAll()
                .antMatchers(HttpMethod.GET,  "/dashboard","/subUsers","/deposit","/profile","/withdrawal","/notification","/arbitrage","/api/v1/files/**", "/api/v1/common/**","/api/v1/wallet/**","/api/v1/coin/**","/api/v1/exchange/**","/api/v1/parameter/**","/api/v1/subscription/**","/api/v1/subscription-package/**","/api/v1/user/**","/api/v1/role/**","/api/v1/arbitrage/**","/api/v1/notification/**").hasAnyRole(RoleType.name(RoleType.ADMIN),RoleType.name(RoleType.SUPER_WISER), RoleType.name(RoleType.USER))
                .antMatchers(HttpMethod.POST, "/api/v1/wallet","/api/v1/arbitrage","/api/v1/notification").hasAnyRole(RoleType.name(RoleType.ADMIN),RoleType.name(RoleType.SUPER_WISER), RoleType.name(RoleType.USER))
                .antMatchers("/**").hasRole(RoleType.name(RoleType.ADMIN))
                .anyRequest().authenticated()
                .and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?errorMsg=invalidUserNameOrPassword")
                .successHandler(successLoginConfig)
                .usernameParameter("login") // This will accept either email or username
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID", "SESSION").invalidateHttpSession(true)
                .logoutSuccessUrl("/login").and().exceptionHandling().accessDeniedPage("/access-denied")
                .and().sessionManagement()
                .sessionFixation().newSession()
                .invalidSessionUrl("/login?errorMsg=invalidSession").sessionAuthenticationErrorUrl("/login?errorMsg=sessionAuthenticationError")
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login?errorMsg=sessionExpired")
        ;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}