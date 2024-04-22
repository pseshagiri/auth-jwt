package com.seshagiri.jwt;

import java.util.Date;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seshagiri.jwt.authapi.dto.LoginDto;
import com.seshagiri.jwt.authapi.dto.LoginResponseDto;
import com.seshagiri.jwt.authapi.dto.RegistrationDto;
import com.seshagiri.jwt.authapi.entity.User;
import com.seshagiri.jwt.authapi.service.AuthenticationService;
import com.seshagiri.jwt.authapi.service.JwtService;

@RestController()
@RequestMapping("/auth")
public class AuthenticationController {
	
	private JwtService jwtService;
	private AuthenticationService authenticationService;
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		super();
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(RegistrationDto dto) {
		 User user = this.authenticationService.signUp(dto);
		 return ResponseEntity.ok(user);
	}
	
	public ResponseEntity<LoginResponseDto> login(LoginDto dto){
		User user = this.authenticationService.autheticateUser(dto);
		String token = jwtService.generateToken(user);
		LoginResponseDto response = new LoginResponseDto();
		response.setToken(token);
		response.setExpirationTime(jwtService.getJwtExpiration());
		return ResponseEntity.ok(response);
	}
	
	

}
