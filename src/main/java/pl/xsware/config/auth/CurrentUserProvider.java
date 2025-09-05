package pl.xsware.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.xsware.domain.model.auth.CustomUserDetails;

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

    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

}
