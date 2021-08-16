package com.joan.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.joan.jwt.model.AuthenticationRequest;
import com.joan.jwt.model.AuthenticationResponse;
import com.joan.jwt.services.MyUserDetailsService;
import com.joan.jwt.util.JwtUtil;

@RestController
public class HelloController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping("hello")
	public String hello() {
		return "Hello world";
	}
	
	@PostMapping("authenticate")
	public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest req) throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword())
					);
			
		}catch(BadCredentialsException ex) {
			throw new Exception("Incorrect Username or Password",ex);
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(req.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		return new AuthenticationResponse(jwt);
	}
}
