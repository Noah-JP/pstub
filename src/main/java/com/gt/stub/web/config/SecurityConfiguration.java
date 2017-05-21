package com.gt.stub.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by noah on 2017. 5. 21..
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/support/**");
        web.ignoring().antMatchers("/gt/**");
    }
}
