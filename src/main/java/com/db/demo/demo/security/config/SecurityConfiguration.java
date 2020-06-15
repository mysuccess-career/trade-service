package com.db.demo.demo.security.config;

import com.db.demo.demo.security.filter.AuthenticationFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author Savitha
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);

    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("method:configure| input={}", http);
        http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();
        http.addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class);
        LOGGER.info("method:configure| exit");
    }

    @Bean
    public FilterRegistrationBean disableServiceFilter(AuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    protected AuthenticationFilter securityFilter() {
        return new AuthenticationFilter();
    }

}
