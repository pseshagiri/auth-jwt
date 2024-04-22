package com.seshagiri.jwt.authapi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@Service()
public class JwtService {
	
	@Value("${security.jwt.secret-key}")
	private String secretKey;
	
	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;
	
	// Extracting Claims
	
	
	
	public String extractClaim(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public long getJwtExpiration() {
		return jwtExpiration;
	}

	public void setJwtExpiration(long jwtExpiration) {
		this.jwtExpiration = jwtExpiration;
	}

	public <T> T extractClaim(String token,Function<Claims,T> claimsResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims extractAllClaims(String token) {
		
		return Jwts.
				parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Key getSignInKey() {
		byte[] bytesKey = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytesKey);
	}
	
	// Generate Token 
	
	public String generateToken(UserDetails userDetails) {
		return buildToken(new HashMap<String,Object>(),userDetails, jwtExpiration);
	}
	
	public String buildToken(Map<String,Object> extractClaims,UserDetails userDetails,
			long expiration) {
	
		return Jwts.
				builder()
				.setClaims(extractClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean isTokenValid(UserDetails userDetails,String token) {
		String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && isTokenExpired(token));
	}
	
	public boolean isTokenExpired(String token) {
		Date date = extractClaim(token, Claims::getExpiration);
		return date.before(new Date());
	}
	
	public String extractUserName(String token) {
		String userName = extractClaim(token,Claims::getSubject);
		return userName;
	}
}
