package cl.globallogic.ejerciciobci.repository;

import cl.globallogic.ejerciciobci.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdUser(java.util.UUID id);

    Optional<User> findByEmail(String email);

    Optional<User> findByToken(String token);
}
