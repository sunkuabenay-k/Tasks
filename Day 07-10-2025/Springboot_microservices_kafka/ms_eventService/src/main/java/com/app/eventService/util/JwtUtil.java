package com.app.eventService.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
    private static final String SECRET_KEY = "myverysecretkeymyverysecretkey1234567890abcdef"; // >= 32 chars for HS256
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	
    public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact()
				;
	}
	public String extractUserName(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
				
	}
	
	public boolean validateToken(String token) {
		try {
		Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
		return true;
		}
		catch (Exception e) {
			throw e;
			
		}
				
	}

}
