package com.ciperlabs.unicodepleco.repository;

import com.ciperlabs.unicodepleco.model.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConversionRepository extends JpaRepository<Conversion, Integer> {


    Integer countConversionByCreatedTimeAfter(LocalDateTime localDateTime);

    @Query("SELECT COUNT(*)" +
            " FROM   Conversion conversion" +
            " WHERE  YEARWEEK(created_time, 1) = YEARWEEK(CURDATE(), 1)")
    Integer findConversionByCurrentWeek();


}
