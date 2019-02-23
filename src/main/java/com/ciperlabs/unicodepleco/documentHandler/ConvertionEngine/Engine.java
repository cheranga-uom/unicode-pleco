package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine;

import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Sinhala.*;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Symbol;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Bamini;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Kalaham;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Nallur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by gayan@ciperlabs.com on 4/21/18.
 */
public class Engine {
    private final Logger logger = LoggerFactory.getLogger(Engine.class);
    private static String sinhalaUnicodeFont = "SINHALA";
    private static String tamilUnicodeFont = "TAMIL";
    private static String lastFont = "";
    private static boolean sinhalaLastCharError1;
    private static boolean sinhalaLastCharError2;
    private static boolean tamilLastCharError1;
    private static boolean tamilLastCharError2;
    private static boolean tamilLastCharError3;


    public Engine() {
        lastFont = "";
        sinhalaLastCharError1 = false;
        sinhalaLastCharError2 = false;
        tamilLastCharError1 = false;
        tamilLastCharError2 = false;
        tamilLastCharError3 = false;

    }

    public String[] toUnicode(String text, String font) {
        /*
        Accepts the @param text and @param font
        return  an array of strings containing the @convertedText and @UnicodeFont
         */
        logger.debug("Text  :" + text + " font   : " +font);
        String unicodeText = text;
        String newFont = null;
        if (font == null) {
            font = "";
        } else {
            lastFont = font;
        }
        if (text == null) {

            return new String[]{text, sinhalaUnicodeFont};

        }
        if (lastFont.equalsIgnoreCase("LTRL") && StringUtils.containsIgnoreCase(font,"")) {
            font = "LTRL";
            lastFont = font;
            //System.out.println("LTRL : "+text);

        } else if (lastFont.equalsIgnoreCase("LTRL") && StringUtils.containsIgnoreCase(font,"Arial")) {
            font = "LTRL";
            lastFont = "LTRL";
            //System.out.println("Arial : "+text);


        }

        /*
            Starts font mappings
         */
        if (StringUtils.containsIgnoreCase(font,"Thibus16STru") || StringUtils.containsIgnoreCase(font,"Thibus15STru") || StringUtils.containsIgnoreCase(font,"Thibus02S")
                || StringUtils.containsIgnoreCase(font,"Thibus02STru") || StringUtils.containsIgnoreCase(font,"Thibus05STru")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;
            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = Thibus.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = Thibus.fixLastCharError2(text);
            }
            if (Thibus.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (Thibus.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }

            unicodeText = Thibus.convert(text);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"FMAbhaya") || StringUtils.containsIgnoreCase(font,"FMAbabld") || StringUtils.containsIgnoreCase(font,"FMAbabldBold") || StringUtils.containsIgnoreCase(font,"FMAbhayax") || StringUtils.containsIgnoreCase(font,"FMEmaneex") || StringUtils.containsIgnoreCase(font,"FMDeranax")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;
            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = FMAbhaya_UCSC.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = FMAbhaya_UCSC.fixLastCharError2(text);
            }
            if (FMAbhaya_UCSC.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (FMAbhaya_UCSC.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }

            unicodeText = FMAbhaya_UCSC.convert(text);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"DL-Manel-bold") || StringUtils.containsIgnoreCase(font,"DL-Manel") || StringUtils.containsIgnoreCase(font,"DL-Manel-bold.")
                || StringUtils.containsIgnoreCase(font,"DL-Manel-bold.-x") || StringUtils.containsIgnoreCase(font,"DL-Manel..")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = DLManel.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = DLManel.fixLastCharError2(text);
            }
            if (DLManel.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (DLManel.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }


            unicodeText = DLManel.convert(text);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"mutu kata")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = MutuKata.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = MutuKata.fixLastCharError2(text);
            }
            if (MutuKata.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (DLManel.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }
            unicodeText = MutuKata.convert(text);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"Sinhala INetFont")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = SinhalaINet.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = SinhalaINet.fixLastCharError2(text);
            }
            if (SinhalaINet.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (SinhalaINet.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }
            unicodeText = SinhalaINet.convert(text);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"LTRL") || StringUtils.containsIgnoreCase(font,"Arial")) {                // Checking arial is not correct but not incorrect either

            unicodeText = LTRL.convert(text);
            return new String[]{unicodeText, sinhalaUnicodeFont};
        } else if (StringUtils.containsIgnoreCase(font,"T06ThibusTru") || StringUtils.containsIgnoreCase(font,"T02Thibus") || StringUtils.containsIgnoreCase(font,"T04ThibusTru")
                ) {

            sinhalaLastCharError1 = false;
            sinhalaLastCharError2 = false;

            if (tamilLastCharError1) {
                tamilLastCharError1 = false;
                text = com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.fixLastCharError(text);
            } else if (tamilLastCharError2) {
                tamilLastCharError2 = false;
                text = com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.fixLastCharError2(text);
            } else if (tamilLastCharError3) {
                tamilLastCharError3 = false;
                text = com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.fixLastCharError3(text);
            }
            if (com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.lastCharError(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError1 = true;
            } else if (com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.lastCharError2(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError2 = true;
            } else if (com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.lastCharError3(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError3 = true;
            }

            unicodeText = com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Thibus.convert(text);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"Kalaham")) {

            sinhalaLastCharError1 = false;
            sinhalaLastCharError2 = false;

            if (tamilLastCharError1) {
                tamilLastCharError1 = false;
                text = Kalaham.fixLastCharError(text);
            } else if (tamilLastCharError2) {
                tamilLastCharError2 = false;
                text = Kalaham.fixLastCharError2(text);
            } else if (tamilLastCharError3) {
                tamilLastCharError3 = false;
                text = Kalaham.fixLastCharError3(text);
            }
            if (Kalaham.lastCharError(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError1 = true;
            } else if (Kalaham.lastCharError2(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError2 = true;
            } else if (Kalaham.lastCharError3(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError3 = true;
            }


            unicodeText = Kalaham.convert(text);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"Nallur")) {

            sinhalaLastCharError1 = false;
            sinhalaLastCharError2 = false;

            if (tamilLastCharError1) {
                tamilLastCharError1 = false;
                text = Nallur.fixLastCharError(text);
            } else if (tamilLastCharError2) {
                tamilLastCharError2 = false;
                text = Nallur.fixLastCharError2(text);
            } else if (tamilLastCharError3) {
                tamilLastCharError3 = false;
                text = Nallur.fixLastCharError3(text);
            }
            if (Nallur.lastCharError(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError1 = true;
            } else if (Nallur.lastCharError2(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError2 = true;
            } else if (Nallur.lastCharError3(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError3 = true;
            }

            unicodeText = Nallur.convert(text);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (StringUtils.containsIgnoreCase(font,"Baamini") || StringUtils.containsIgnoreCase(font,"Bamini")) {

            sinhalaLastCharError1 = false;
            sinhalaLastCharError2 = false;

            if (tamilLastCharError1) {
                tamilLastCharError1 = false;
                text = Bamini.fixLastCharError(text);
            } else if (tamilLastCharError2) {
                tamilLastCharError2 = false;
                text = Bamini.fixLastCharError2(text);
            } else if (tamilLastCharError3) {
                tamilLastCharError3 = false;
                text = Bamini.fixLastCharError3(text);
            }
            if (Bamini.lastCharError(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError1 = true;
            } else if (Bamini.lastCharError2(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError2 = true;
            } else if (Bamini.lastCharError3(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError3 = true;
            }


            unicodeText = Bamini.convert(text);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (StringUtils.equals(font,"Symbol")) {
            sinhalaLastCharError1 = false;
            sinhalaLastCharError2 = false;
            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;
            unicodeText = Symbol.convert(text);
            return new String[]{unicodeText, sinhalaUnicodeFont};
        } else if (StringUtils.equals(font,"")) {                    // Just in case for any left Overs - This should be removed from production
            unicodeText = LTRL.convert(text);
            return new String[]{unicodeText, sinhalaUnicodeFont};
        } else {
            return new String[]{text, font};
        }
    }
}
