package re.ethernitydev.mcquest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.ethernitydev.mcquest.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
