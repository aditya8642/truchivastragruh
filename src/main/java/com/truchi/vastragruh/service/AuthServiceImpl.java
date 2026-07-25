package com.truchi.vastragruh.service;

import com.truchi.vastragruh.dto.AuthenticationResponse;
import com.truchi.vastragruh.dto.LoginRequest;
import com.truchi.vastragruh.dto.LoginResponse;
import com.truchi.vastragruh.dto.RegisterRequest;
import com.truchi.vastragruh.entity.Role;
import com.truchi.vastragruh.entity.User;
import com.truchi.vastragruh.repository.RoleRepository;
import com.truchi.vastragruh.repository.UserRepository;
import com.truchi.vastragruh.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public String register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already registered.");

        if(userRepository.existsByMobile(request.getMobile()))
            throw new RuntimeException("Mobile already registered.");

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.getRoles().add(customerRole);

        userRepository.save(user);

        return "Registration Successful";

    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )

        );

        String token = jwtService.generateToken(request.getEmail());
        User user= userRepository.findByEmail(request.getEmail()).get();
        return new AuthenticationResponse(token, user.getFirstName());


    }

}