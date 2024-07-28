package com.shoppingcartsimulation.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppingcartsimulation.app.model.Login;

public interface LoginRepo extends JpaRepository<Login,Long> {
	Optional<Login> findByEmail(String email);
}
