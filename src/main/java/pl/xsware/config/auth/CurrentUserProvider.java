package pl.xsware.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.xsware.domain.model.auth.CustomUserDetails;
import pl.xsware.domain.model.user.UserDto;

@Component
public class CurrentUserProvider {

    public CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("Brak zalogowanego użytkownika");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails cud) {
            return cud;
        }

        throw new IllegalStateException("Niepoprawny typ użytkownika w kontekście");
    }

    public UserDto getCurrentUserDto() {
        return getCurrentUser().toDto();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

}
