package com.ciperlabs.unicodepleco.repository;

import com.ciperlabs.unicodepleco.model.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser, Long> {

    Boolean existsByApikeyEquals(String APIkey);
}
