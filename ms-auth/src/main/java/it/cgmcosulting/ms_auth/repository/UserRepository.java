package it.cgmcosulting.ms_auth.repository;

import it.cgmcosulting.ms_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Optionals;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);


    boolean existsByUsernameOrEmail(String username, String email);
}
