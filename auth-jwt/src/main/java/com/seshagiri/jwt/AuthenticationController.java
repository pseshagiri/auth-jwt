package com.seshagiri.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody() RegistrationDto dto) {
		System.out.println("dto full Name::" + dto.getFullName());
		System.out.println("dto:: email" + dto.getEmail());
		 User user = this.authenticationService.signUp(dto);
		 return ResponseEntity.ok(user);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<LoginResponseDto> login(@RequestBody() LoginDto dto){
		
		System.out.println("dto full Name::");
		System.out.println("dto:: email" + dto.getEmail());
		User user=null;
		try {
		 user = this.authenticationService.autheticateUser(dto);
		}catch(RuntimeException exp) {
			exp.printStackTrace();
		}
		
		String token = jwtService.generateToken(user);
		LoginResponseDto response = new LoginResponseDto();
		response.setToken(token);
		response.setExpirationTime(jwtService.getJwtExpiration());
		return ResponseEntity.ok(response);
	}
	
	

}
