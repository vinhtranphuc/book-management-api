package com.book.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.book.common.BaseController;
import com.book.mybatis.model.UserVO;
import com.book.payload.JwtAuthenticationResponse;
import com.book.payload.LoginRequest;
import com.book.security.JwtTokenProvider;
import com.book.security.UserPrincipal;
import com.book.service.UserService;

@Controller
@RequestMapping("/api/auth")
//@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:3000",
//		"http://127.0.0.1:3001" })
public class AuthController extends BaseController {

	protected Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}
	
	@GetMapping("/user/current-user")
    @PreAuthorize("hasRole('USER')")
    public UserVO getCurrentUser(Authentication authentication) {
    	UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
//        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userService.getUserById(currentUser.getId());
    }
}