package com.portfolio.mourad.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.portfolio.mourad.mailing.service.EmailService;
import com.portfolio.mourad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import com.portfolio.mourad.models.ERole;
import com.portfolio.mourad.models.Role;
import com.portfolio.mourad.models.User;
import com.portfolio.mourad.payload.request.LoginRequest;
import com.portfolio.mourad.payload.request.SignupRequest;
import com.portfolio.mourad.payload.response.UserInfoResponse;
import com.portfolio.mourad.payload.response.MessageResponse;
import com.portfolio.mourad.repository.RoleRepository;
import com.portfolio.mourad.repository.UserRepository;
import com.portfolio.mourad.security.jwt.JwtUtils;
import com.portfolio.mourad.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  EmailService emailService;


//  @GetMapping("/csrf")
//  public CsrfToken csrf(CsrfToken token) {
//    return token;
//  }

  @PostMapping("/signin")
//  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    Authentication authentication = null;
    try{
      authentication = authenticationManager
              .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }catch(Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

    User user = userRepository.findByUsername(userDetails.getUsername()).get();

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//            .body(new JwtResponse(jwt));
            .body(new UserInfoResponse(user.getId(),
                                   user.getUsername(),
                                   user.getEmail(),
                                   user.getFirstName(),
                                   user.getLastName(),
                                   roles, jwt,
                                   userService.getUserImageData(user.getId()),
                           "password"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    System.out.println(signUpRequest.toString());
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "ROLE_USER":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
//        case "mod":
//          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//          roles.add(modRole);
//
//          break;
//        default:
//          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//          roles.add(userRole);
          case "ROLE_ADMIN":
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            break;
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
    emailService.sendSimpleMessage(signUpRequest.getEmail(), "Confirm registration", "Your registration is complete");
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
