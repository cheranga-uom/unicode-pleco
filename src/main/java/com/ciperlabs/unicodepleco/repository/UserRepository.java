package com.ciperlabs.unicodepleco.repository;

import com.ciperlabs.unicodepleco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
}
