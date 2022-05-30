package com.mobilishop.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mobilishop.api.model.Role;
import com.mobilishop.api.model.User;
import com.mobilishop.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {

  @Autowired private UserService userService;

  @PostConstruct
  public void setService() {
    userService.init();
  }

  @PreAuthorize("hasRole( 'ADMIN' )")
  @GetMapping("/api/v1/users")
  public List<User> getAllUsers() {
    return userService.findAllUsers();
  }

  @PostMapping("/api/v1/register")
  public String registerUser(@RequestBody User user) {
    System.out.println("UserController.registerUser()");
    return userService.createUser(user);
  }

  @GetMapping("/api/v1/users/{id}")
  public User getUserById(@PathVariable Long id) {
    return userService.findUserById(id);
  }

  @PutMapping("/api/v1/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    return ResponseEntity.ok(userService.updateUser(id, user));
  }

  @DeleteMapping("/api/v1/users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    System.out.println("BEFORE UserController.deleteUser()");
    userService.deleteUser(id);
    System.out.println("UserController.deleteUser()");
    return ResponseEntity.ok("User with id " + id + " deleted");
  }

  @GetMapping("/api/v1/users/me")
  @ResponseBody
  public String getCurrentUser(Authentication authentication) {

    return authentication.getName();
  }

  @GetMapping("/api/v1/register/confirm")
  public String confirmUserAccount(@RequestParam("token") String token) {
    return userService.confirmToken(token);
  }

  @GetMapping("/api/v1/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        System.out.println("token: " + refreshToken);
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        // find the user in database
        User user = userService.findByUserName(username);

        String accessToken =
            JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(
                    "roles",
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
      } catch (Exception exception) {
        response.setHeader("error", exception.getMessage());
        // response.sendError(FORBIDDEN.value());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    } else {
      throw new RuntimeException("JWT Refresh Token is missing");
    }
  }
}
