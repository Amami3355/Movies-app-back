package com.portfolio.mourad.controllers;

import com.portfolio.mourad.models.Movie;
import com.portfolio.mourad.repository.MovieRepository;
import com.portfolio.mourad.security.jwt.AuthTokenFilter;
import com.portfolio.mourad.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000",exposedHeaders = "Set-Cookie", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

  private final JwtUtils jwtUtils;
  private final AuthTokenFilter authTokenFilter;
  private final HttpServletRequest context;
  private final MovieRepository movieRepository;


  @GetMapping("/all2")
//    @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<Movie>> getAllMovies(){
    return ResponseEntity.ok(movieRepository.findAll());
  }

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<String> userAccess(Principal principal) {

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authTokenFilter.parseJwt(context))
            .body("User Content. "  + principal.getName()+ " " + authTokenFilter.parseJwt(context) + " " +  "*****");

  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess(Principal principal) {
    return "Moderator Board. " + principal.getName();
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess(Principal principal) {
    return "Admin Board. " + principal.getName();
  }
}
