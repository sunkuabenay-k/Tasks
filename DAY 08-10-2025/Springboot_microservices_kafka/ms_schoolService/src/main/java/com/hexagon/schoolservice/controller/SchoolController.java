package com.hexagon.schoolservice.controller;

import com.hexagon.schoolservice.entity.AuthRequest;
import com.hexagon.schoolservice.entity.School;
import com.hexagon.schoolservice.entity.User;
import com.hexagon.schoolservice.repository.UserRepository;
import com.hexagon.schoolservice.service.SchoolService;
import com.hexagon.schoolservice.util.JwtUtil;


import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RequestMapping("/school")
@RestController
public class SchoolController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private AuthenticationManager authManager;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/admin")
    public School addSchool(@RequestBody School school){
        return schoolService.addSchool(school);
    }
    @GetMapping("/user")
    public List<School> fetchSchools(){
        return  schoolService.fetchSchools();
    }
    @GetMapping("/user/{id}")
    public School fetchSchoolById(@PathVariable int id){
        return schoolService.fetchSchoolById(id);
    }
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                return jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
    
    @PostConstruct
    public void createTestUser() {
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User user = new User();
            user.setUsername("testuser");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(Set.of("ROLE_USER")); // Assign a default role
            userRepository.save(user);
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(Set.of("ROLE_ADMIN")); // Assign a default role
            userRepository.save(user);
        }
    }


    
}