package com.ciperlabs.unicodepleco.repository;

import com.ciperlabs.unicodepleco.model.APIUser;
import com.ciperlabs.unicodepleco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser, Long> {

    Boolean existsByApikeyEquals(String APIkey);
    APIUser findAPIUserByApikey(String APIkey);
}
