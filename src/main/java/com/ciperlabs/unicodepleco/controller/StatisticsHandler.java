package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.repository.ConversionRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class StatisticsHandler {

    /*
     * This Class will provide the basic Statistics about the users and the conversion activities
     * Created By Gayan Kavirathne on 18-Jan-2019
     */

    Logger logger = LoggerFactory.getLogger(DocumentHandler.class);

    //    @Autowired
    private ConversionRepository conversionRepository;
    private UserRepository userRepository;

    public StatisticsHandler(ConversionRepository conversionRepository, UserRepository userRepository) {
        this.conversionRepository = conversionRepository;
        this.userRepository = userRepository;
    }

    public Integer conversionsForWeek() {

        TemporalField fieldISO = WeekFields.of(Locale.UK).dayOfWeek();                              // Starts week on monday
        LocalDateTime firstDateOfWeek = LocalDateTime.now().with(fieldISO, 1);

        logger.info("First Day of weak : " + firstDateOfWeek);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(firstDateOfWeek);
//        Integer conversions = conversionRepository.findConversionByCurrentWeek();

        return conversions;
    }

    public Integer conversionsOfTheMonth() {

        LocalDateTime firstDateOfMonth = LocalDateTime.now().withDayOfMonth(1);

        logger.info("First Day of Month : " + firstDateOfMonth);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(firstDateOfMonth);

        return conversions;
    }

    public Integer conversionsForToday() {

        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
        logger.info("Today Midnight : " + todayMidnight);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(todayMidnight);

        return conversions;
    }

    public Integer conversionsOfTheYear() {

        LocalDateTime firstDateOfYear = LocalDateTime.now().withDayOfYear(1);

        logger.info("First Day of Year : " + firstDateOfYear);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(firstDateOfYear);

        return conversions;
    }

    public Long totalConversions() {

        return conversionRepository.count();
    }

    public Integer usersForWeek(){


        TemporalField fieldISO = WeekFields.of(Locale.UK).dayOfWeek();                              // Starts week on monday
        LocalDateTime firstDateOfWeek = LocalDateTime.now().with(fieldISO, 1);

        Integer userCount = userRepository.countUsersByCreatedAtAfter(firstDateOfWeek);
        return  userCount;
    }

    public Integer usersForMonth(){

        LocalDateTime firstDateOfMonth = LocalDateTime.now().withDayOfMonth(1);

        logger.info("First Day of Month : " + firstDateOfMonth);

        Integer usersCount = userRepository.countUsersByCreatedAtAfter(firstDateOfMonth);

        return usersCount;
    }

    public Integer usersForYear(){
        LocalDateTime firstDateOfYear = LocalDateTime.now().withDayOfYear(1);

        logger.info("First Day of Year : " + firstDateOfYear);

        Integer usersCount = userRepository.countUsersByCreatedAtAfter(firstDateOfYear);

        return usersCount;
    }

    public Integer usersJoinedToday(){
        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
        logger.info("Today Midnight : " + todayMidnight);

        Integer usersCount = userRepository.countUsersByCreatedAtAfter(todayMidnight);

        return usersCount;
    }

    public Long totalUsers(){

        return userRepository.count();
    }
}
