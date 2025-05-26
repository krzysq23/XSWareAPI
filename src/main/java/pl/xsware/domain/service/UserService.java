package pl.xsware.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.xsware.domain.model.dto.RegisterRequest;
import pl.xsware.domain.model.entity.auth.User;
import pl.xsware.domain.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public void createUser(RegisterRequest data) throws IllegalArgumentException {

        if (userRepository.existsByEmail(data.getEmail())) {
            throw new IllegalArgumentException("Email już istnieje");
        }

        User user = User.builder()
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .email(data.getEmail())
                .password(passwordEncoder.encode(data.getPassword()))
                .build();

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Nie znaleziono użytkownika: " + email));
    }
}
