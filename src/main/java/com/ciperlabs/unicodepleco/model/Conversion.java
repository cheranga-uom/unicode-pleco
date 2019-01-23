package com.ciperlabs.unicodepleco.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversion")
public class Conversion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer conversionId;
    private String inputFileType;
    private String outputFileType;
    private String inputFileName;
    private String outputFileName;
    private String inputFilePath;
    private String outputFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public Integer getConversionId() {
        return conversionId;
    }

//    public void setConversionId(Integer conversionId) {
//        this.conversionId = conversionId;
//    }

    public String getInputFileType() {
        return inputFileType;
    }

    public void setInputFileType(String inputFileType) {
        this.inputFileType = inputFileType;
    }

    public String getOutputFileType() {
        return outputFileType;
    }

    public void setOutputFileType(String outputFileType) {
        this.outputFileType = outputFileType;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public LocalDateTime getCreatedTime() {
        return createdTime;
    }


    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }


}
