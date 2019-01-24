package com.ciperlabs.unicodepleco.repository;

import com.ciperlabs.unicodepleco.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Integer> {

}
