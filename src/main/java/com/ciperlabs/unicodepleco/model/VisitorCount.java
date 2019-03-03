package com.ciperlabs.unicodepleco.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitorCount")
public class VisitorCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer visitorId;

    @CreatedDate
    private LocalDateTime createdTime;
}
