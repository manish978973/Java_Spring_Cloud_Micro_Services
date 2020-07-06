package com.learnprogramming.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.learnprogramming.photoapp.api.users.service.UsersService;

@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	
	private Environment environment;
	private UsersService usersService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@Autowired
	public WebSecurity(UsersService usersService,BCryptPasswordEncoder bCryptPasswordEncoder,Environment environment ) {
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.environment = environment;
	}
	
	
	protected void configure (HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/users").hasIpAddress(environment.getProperty("eureka.instance.hostname"))
		.anyRequest().authenticated()
		.and()
		.addFilter(getAuthenticationfilter())
		.addFilter(new AuthorizationFilter(authenticationManager(),environment));
		http.headers().frameOptions().disable();
	}
	
	
	private AuthenticationFilter getAuthenticationfilter() throws Exception{
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(usersService,environment);
		authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
	
	protected void configure (AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	
	

}
