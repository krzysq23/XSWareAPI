package pl.xsware.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xsware.domain.model.entity.auth.Role;
import pl.xsware.domain.model.entity.auth.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
