package com.ciperlabs.unicodepleco.service.storage;

import com.ciperlabs.unicodepleco.model.FileType;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class StoredFile {

    private String fileName;
    private String path;
    private InputStream inputStream;
    private FileType fileType;


    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        System.out.println(path);
        //TODO Remove sout
    }

    public InputStream getInputStream() {

        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(this.getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

//    public void setInputStream(InputStream inputStream) {
//        this.inputStream = inputStream;
//    }
}
