package com.seshagiri.jwt.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration()
@EnableWebSecurity
public class SecurityConfig {

	AuthenticationProvider authenticationProvider;
	JwtAuthenticationFilter jwtAuthenicationFilter;
	
	public SecurityConfig(AuthenticationProvider authenticationProvider,
			JwtAuthenticationFilter jwtAuthenicationFilter) {
		super();
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenicationFilter = jwtAuthenicationFilter;
	}
	
	
	@Bean()
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		return http
			.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/auth/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authenticationProvider(this.authenticationProvider)
			.addFilterBefore(jwtAuthenicationFilter,UsernamePasswordAuthenticationFilter.class)
			.build();	
	}
	
	@Bean()
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsSource = new CorsConfiguration();
		corsSource.setAllowedOrigins(List.of("http://localhost:8005"));
		corsSource.setAllowedMethods(List.of("GET","POST","DELETE","PUT"));
		corsSource.setAllowedHeaders(List.of("Authorization","ContentType"));
		
		UrlBasedCorsConfigurationSource urlCors = new 
				UrlBasedCorsConfigurationSource();
		urlCors.registerCorsConfiguration("/**", corsSource);
		return urlCors;
	}
	
}
