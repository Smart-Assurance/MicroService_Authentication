package ma.fstt.microserviceauthentication.controllers;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import ma.fstt.microserviceauthentication.entities.Client;
import ma.fstt.microserviceauthentication.entities.User;
import ma.fstt.microserviceauthentication.payload.request.LoginRequest;
import ma.fstt.microserviceauthentication.payload.request.RegisterRequest;
import ma.fstt.microserviceauthentication.payload.request.TokenValidationRequest;
import ma.fstt.microserviceauthentication.payload.response.JwtResponse;
import ma.fstt.microserviceauthentication.payload.response.MessageResponse;
import ma.fstt.microserviceauthentication.payload.response.TokenValidationResponse;
import ma.fstt.microserviceauthentication.repository.UserRepository;
import ma.fstt.microserviceauthentication.security.jwt.JwtUtils;
import ma.fstt.microserviceauthentication.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new JwtResponse(jwt,role));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new Client(registerRequest.getL_name(),registerRequest.getF_name(),registerRequest.getUsername(), encoder.encode(registerRequest.getPassword()),registerRequest.getEmail(),registerRequest.getPhone(),registerRequest.getCity(),registerRequest.getAddress(),registerRequest.getAdd_wallet_cli(),registerRequest.getCin(),registerRequest.getDate_of_birth());


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    // validate token and role
    @PostMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest tokenValidationRequest) {

        String token = tokenValidationRequest.getToken();
        if (jwtUtils.validateJwtToken(token)){
            Claims claims = jwtUtils.getRolesFromJwtToken(token);
            Map<String, Object> roleMap = (Map<String, Object>) claims.get("role");
            // Extract roles from claims
            String roleAuthority = (String) roleMap.get("authority");

            TokenValidationResponse response;
            if(roleAuthority.equals("ROLE_ADMIN"))
                response = new TokenValidationResponse("administrator");
            else if(roleAuthority.equals("ROLE_EMPLOYEE"))
                response = new TokenValidationResponse("employee");
            else if(roleAuthority.equals("ROLE_EXAMINATER"))
                response = new TokenValidationResponse("examinater");
            else if(roleAuthority.equals("ROLE_CLIENT"))
                response = new TokenValidationResponse("client");
            else
                response = new TokenValidationResponse();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse());
        }
    }

    @PostMapping("/get-id")
    public ResponseEntity<TokenValidationResponse> getId(@RequestBody TokenValidationRequest tokenValidationRequest) {

        String token = tokenValidationRequest.getToken();
        if (jwtUtils.validateJwtToken(token)){
            Claims claims = jwtUtils.getRolesFromJwtToken(token);

            String id = claims.get("id", String.class);

            TokenValidationResponse response = new TokenValidationResponse(id);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse());
        }
    }
}
