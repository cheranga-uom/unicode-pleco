package com.ciperlabs.unicodepleco.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "apiuser")
public class APIUser {

    @Id
    @Column(columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String apikey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

    public Long getId() {
        return id;
    }

    public String getApikey() {
        return apikey;
    }

    public User getUser() {
        return user;
    }
}
