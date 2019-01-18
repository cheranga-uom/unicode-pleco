package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.repository.ConversionRepository;
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
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class DetailsHandler {

    Logger logger = LoggerFactory.getLogger(DocumentHandler.class);

//    @Autowired
    private ConversionRepository conversionRepository;

    public DetailsHandler(ConversionRepository conversionRepository){
            this.conversionRepository = conversionRepository;
    }

    public Integer conversionsForWeek(){

        LocalDateTime firstDateOfWeek = LocalDateTime.now().withDayOfMonth(1);

        logger.info("First Day of weak : "+ firstDateOfWeek);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(firstDateOfWeek);
//        Integer conversions = conversionRepository.findConversionByCurrentWeek();

        return conversions;
    }
    public Integer conversionsOfTheMonth(){

        LocalDateTime firstDateOfMonth = LocalDateTime.now().withDayOfMonth(1);

        logger.info("First Day of Month : "+ firstDateOfMonth);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(firstDateOfMonth);

        return conversions;
    }

    public Integer conversiondForToday(){

        LocalDate today = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
        logger.info("Today Midnight : "+ todayMidnight);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(todayMidnight);

        return conversions;
    }

    public Integer conversionsOfTheYear(){

        LocalDateTime firstDateOfYear = LocalDateTime.now().withDayOfYear(1);

        logger.info("First Day of Year : "+ firstDateOfYear);

        Integer conversions = conversionRepository.countConversionByCreatedTimeAfter(firstDateOfYear);

        return conversions;
    }

    public Long totalConversions(){


        return conversionRepository.count();
    }


}
