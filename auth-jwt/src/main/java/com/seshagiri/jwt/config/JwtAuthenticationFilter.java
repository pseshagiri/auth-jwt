package com.seshagiri.jwt.config;

import java.io.IOException;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.seshagiri.jwt.authapi.service.JwtService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private JwtService jwtService;
	private UserDetailsService userDetailService;
	private HandlerExceptionResolver handlerExceptionResolver;
	
	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailService,
			HandlerExceptionResolver handlerExceptionResolver) {		
		this.jwtService = jwtService;
		this.userDetailService = userDetailService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		UsernamePasswordAuthenticationToken authToken=null;
		
		if(authHeader==null && !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return ;
		}
		
		try {
			final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUserName(jwt);
            
             Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
             
             if(userEmail !=null && authentication !=null) {
            	 UserDetails userDetails = userDetailService.
            			 loadUserByUsername(userEmail);
            	 if(!jwtService.isTokenValid(userDetails,jwt)) {
            	   authToken = new
            			 UsernamePasswordAuthenticationToken(userDetails,null,
            					 userDetails.getAuthorities());
            	 }
            	 authToken.setDetails(new WebAuthenticationDetails(request));
            	 SecurityContextHolder.getContext().setAuthentication(authToken);            	 
             }         
            
		}catch(Exception exp) {
			this.handlerExceptionResolver.resolveException(request, response, null, exp);
		}
		
				
	}

}
