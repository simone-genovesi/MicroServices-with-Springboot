package it.cgmcosulting.ms_auth.repository;

import it.cgmcosulting.ms_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsernameOrEmail(String username, String email);
}
