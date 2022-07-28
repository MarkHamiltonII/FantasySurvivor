package survivor.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import survivor.models.AppUser;
import survivor.security.AppUserService;
import survivor.security.JwtConverter;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class AuthController {

    // The `AuthenticationManager` interface defines a single method `authenticate()`
    // that processes an Authentication request.
    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;
    private final AppUserService appUserService;

    public AuthController(AuthenticationManager authenticationManager, JwtConverter converter, AppUserService appUserService){
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.appUserService = appUserService;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials){

        // The `UsernamePasswordAuthenticationToken` class is an `Authentication` implementation
        // that is designed for simple presentation of a username and password.
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));

        try {
            // The `Authentication` interface Represents the token for an authentication request
            // or for an authenticated principal once the request has been processed by the //
            // `AuthenticationManager.authenticate(Authentication)` method.
            Authentication authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                String jwtToken = converter.getTokenFromUser((User) authentication.getPrincipal());

                HashMap<String, String> map = new HashMap<>();
                map.put("jwt_token", jwtToken);

                AppUser user = appUserService.loadUserByUsername(credentials.get("username"));
                map.put("appUserId", ""+user.getAppUserId());

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (AuthenticationException ex) {
            System.out.println(ex);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<Map<String, String>> refreshToken(UsernamePasswordAuthenticationToken principal) {
        User user = new User(principal.getName(), principal.getName(), principal.getAuthorities());
        String jwtToken = converter.getTokenFromUser(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("jwt_token", jwtToken);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/create_account")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials) {
        AppUser appUser = null;

        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            appUser = appUserService.createAppUser(username, password);
        } catch (ValidationException ex) {
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("The provided username already exists"), HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("appUserId", appUser.getAppUserId());

        return new ResponseEntity<>(map,HttpStatus.CREATED);
    }
}
