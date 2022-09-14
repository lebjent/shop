package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shop.service.MemberService;
/*
 	스프링 시큐리티 관련Config 설정
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MemberService memberService;
	
	@Override
	protected void configure(HttpSecurity http)throws Exception{
		http.formLogin()
				.loginPage("/members/login")//로그인 페이지 URL을 설정
				.defaultSuccessUrl("/")//로그인 성공시 이동할 URL 설정
				.usernameParameter("email")//로그인 시 사용할 파라미터 이름으로 email을 지정
				.failureUrl("/members/login/error")//로그인에 실패 했을시
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))//logout url
				.logoutSuccessUrl("/");
		
		http.authorizeHttpRequests()
				.mvcMatchers("/","/members/**","/images/**").permitAll()//permitAll()을 통해 해당경로에 접근할 수 있도록 설정
				.mvcMatchers("/admin/**").hasRole("ADMIN")//해당계정이 'ADMIN' ROLE일경우 접근할수있도록 설정
				.anyRequest().authenticated();//위에 접근제한설정해준 경로를 제외한 모든경로는 모두 인증을 요구하도록 한다.
		
		http.exceptionHandling()
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint()); //인증되지 않은 사용자가 리소스에 접근하였을때 수행되는 핸들러
		
		
		
	}
	
	//Spring Security 인증을 해주는 메서드
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/css/**","/js/**","/img/**");
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
