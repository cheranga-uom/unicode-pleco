package com.ciperlabs.unicodepleco;

import com.ciperlabs.unicodepleco.documentHandler.word.HWPFtoXWPF;
import org.apache.poi.util.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Conversion {

    public static void main(String[] args) {
        File file = new File("/home/gayan/Desktop/CiperLabs/unicode-pleco/test Samples/doc/test.doc");
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            try {
                MultipartFile multipartFile = new MockMultipartFile("file",
                        file.getName(), "application/msword", IOUtils.toByteArray(input));
//                HWPFtoXWPF hwpFtoXWPF = new HWPFtoXWPF(multipartFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
