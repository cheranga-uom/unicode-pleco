package com.ciperlabs.unicodepleco.repository;

import com.ciperlabs.unicodepleco.model.Issue;
import com.ciperlabs.unicodepleco.model.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Integer> {

    List<Issue> findAllByIssueStatusEquals(IssueStatus issueStatus);

}
