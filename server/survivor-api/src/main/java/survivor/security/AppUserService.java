package survivor.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import survivor.data.AppUserJdbcTemplateRepository;
import survivor.models.AppUser;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserJdbcTemplateRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserJdbcTemplateRepository repository,
                          PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findAppUserByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public List<AppUser> findAllAppUsers() {
        return repository.findAllAppUsers();
    }

    public AppUser findAppUserbyId(int id) throws UsernameNotFoundException {
        AppUser appUser = repository.findAppUserById(id);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException("user with id " + id + " not found");
        }

        return appUser;
    }

    public AppUser createAppUser(String username, String password) {
        validate(username);
        validatePassword(password);

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, username, password, false, List.of("User"));

        return repository.createAppUser(appUser);
    }

    public void updateAppUser(AppUser user) {
        validate(user.getUsername());
        if (user.getAppUserId() <= 0) {
            throw new ValidationException("cannot update user without user id");
        }

        if (!repository.updateAppUser(user)) {
            throw new UsernameNotFoundException(user.getUsername() + " not found");
        }
    }

    public void deleteAppUserById(int appUserId) {
        if (!repository.deleteAppUserById(appUserId)) {
            throw new UsernameNotFoundException("user with id " + appUserId + " not found");
        }
    }


    ///////////////////////////////////////// Validation Methods /////////////////////////////////////////////
    private void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }

        if (username.length() > 50) {
            throw new ValidationException("username must be less than 50 characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("password must be at least 8 characters");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }
}
