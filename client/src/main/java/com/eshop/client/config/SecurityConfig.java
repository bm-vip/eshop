package com.eshop.client.config;

import com.eshop.client.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    SuccessLoginConfig successLoginConfig;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/ltr/**","/logout","/notfound","/login","/registration","/send-OTP","/api/v1/country/findAllSelect*","/api/v1/user/register*","/actuator/**").permitAll()
                .antMatchers(HttpMethod.GET,  "/dashboard","/deposit","/profile","/withdrawal","/notification","/arbitrage","/api/v1/files/**", "/api/v1/common/**","/api/v1/wallet/**","/api/v1/coin/**","/api/v1/exchange/**","/api/v1/parameter/**","/api/v1/subscription/**","/api/v1/subscription-package/**","/api/v1/user/**","/api/v1/role/**","/api/v1/arbitrage/**","/api/v1/notification/**").hasAnyRole(RoleType.name(RoleType.ADMIN),RoleType.name(RoleType.SUPER_WISER), RoleType.name(RoleType.USER))
                .antMatchers(HttpMethod.POST, "/api/v1/wallet","/api/v1/arbitrage").hasAnyRole(RoleType.name(RoleType.ADMIN),RoleType.name(RoleType.SUPER_WISER), RoleType.name(RoleType.USER))
                .antMatchers("/**").hasRole(RoleType.name(RoleType.ADMIN))
                .anyRequest().authenticated()
                .and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?errorMsg=invalidUserNameOrPassword")
                .successHandler(successLoginConfig)
                .usernameParameter("userName")
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