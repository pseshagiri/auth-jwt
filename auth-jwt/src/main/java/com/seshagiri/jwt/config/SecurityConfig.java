package com.seshagiri.jwt.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer;
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
	
	 private static final String[] AUTH_WHITELIST = {
	            // -- Swagger UI v2
	            "/v2/api-docs",
	            "/swagger-resources",
	            "/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/swagger-ui.html",
	            "/webjars/**",
	            // -- Swagger UI v3 (OpenAPI)
	            "/v3/api-docs/**",
	            "/swagger-ui/**",
	            // other public endpoints of your API may be appended to this array
	            "/auth/**"
	    };
	
	
	
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
			.requestMatchers(AUTH_WHITELIST)
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
		corsSource.setAllowedOrigins(List.of("http://localhost:9002"));
		corsSource.setAllowedMethods(List.of("GET","POST","DELETE","PUT"));
		corsSource.setAllowedHeaders(List.of("Authorization","ContentType"));
		
		UrlBasedCorsConfigurationSource urlCors = new 
				UrlBasedCorsConfigurationSource();
		urlCors.registerCorsConfiguration("/**", corsSource);
		return urlCors;
	}


	
	
}
