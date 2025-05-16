package pl.xsware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.xsware.domain.model.entity.auth.User;
import pl.xsware.domain.repository.UserRepository;

@Component
public class DBDataTest implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("kontakt@xsware.pl").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .email("kontakt@xsware.pl")
                            .password(passwordEncoder.encode("1234"))
                            .build()
            );
        }
    }
}
