package com.eshop.app.config;

import com.eshop.app.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                .antMatchers("/logout","/notfound","/login","/actuator/**").permitAll()
                .antMatchers("/dashboard","/userManagement","/wallet","/subscriptionPackage","/fileUpload/**","/subscription","/api/v1/user/**","/api/v1/role/**","/api/v1/wallet/**","/api/v1/subscription-package/**","/api/v1/subscription/**","/api/v1/files/**","/api/v1/common/**").hasAnyRole(RoleType.name(RoleType.ADMIN), RoleType.name(RoleType.SUPER_WISER))
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
                .expiredUrl("/login?errorMsg=sessionExpired")
        ;
    }
}