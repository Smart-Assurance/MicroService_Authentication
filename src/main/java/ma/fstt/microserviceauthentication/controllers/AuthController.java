package ma.fstt.microserviceauthentication.controllers;

import jakarta.validation.Valid;
import ma.fstt.microserviceauthentication.entities.Client;
import ma.fstt.microserviceauthentication.entities.User;
import ma.fstt.microserviceauthentication.payload.request.LoginRequest;
import ma.fstt.microserviceauthentication.payload.request.RegisterRequest;
import ma.fstt.microserviceauthentication.payload.response.JwtResponse;
import ma.fstt.microserviceauthentication.payload.response.MessageResponse;
import ma.fstt.microserviceauthentication.repository.UserRepository;
import ma.fstt.microserviceauthentication.security.jwt.JwtUtils;
import ma.fstt.microserviceauthentication.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CLIENT')")
    public String userAccess() {
        return "Client Content.";
    }
}
