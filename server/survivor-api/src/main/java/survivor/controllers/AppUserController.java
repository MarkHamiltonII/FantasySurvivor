package survivor.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import survivor.models.AppUser;
import survivor.security.AppUserService;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class AppUserController {
    private final AppUserService service;

    public AppUserController(AppUserService appUserService) {
        this.service = appUserService;
    }

    @GetMapping
    public List<AppUser> findAllAppUsers() {
        return service.findAllAppUsers();
    }

    @GetMapping("/user")
    public ResponseEntity<AppUser> loadAppUserByUsername(@RequestBody Map<String, String> credentials) {
        try{
            return new ResponseEntity<>(service.loadUserByUsername(credentials.get("username")), HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> findAppUserById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(service.findAppUserbyId(id), HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateAppUser(@PathVariable int id, @RequestBody AppUser user) {
        if (id != user.getAppUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }

        try {
            service.updateAppUser(user);
        } catch (ValidationException ex) {
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST); // 400
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(List.of("That username could not be located"), HttpStatus.NOT_FOUND); // 404
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("The provided username already exists."), HttpStatus.BAD_REQUEST); // 400
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteAppUserById(@PathVariable int id){
        try {
            service.deleteAppUserById(id);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(List.of("No user found with the user id: " + id), HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}
