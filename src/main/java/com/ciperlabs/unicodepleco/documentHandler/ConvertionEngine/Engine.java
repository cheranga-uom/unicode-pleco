package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine;

import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Sinhala.*;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Symbol;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Bamini;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Kalaham;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.Nallur;
import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Tamil.ThibusT;
import com.ciperlabs.unicodepleco.documentHandler.util.FontLog;
import com.ciperlabs.unicodepleco.documentHandler.util.FontLogAbs;
import com.ciperlabs.unicodepleco.documentHandler.util.FontState;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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

    private ArrayList<FontLogAbs> fontLogs;


    public Engine(ArrayList<FontLogAbs> fontLogs) {
        lastFont = "";
        sinhalaLastCharError1 = false;
        sinhalaLastCharError2 = false;
        tamilLastCharError1 = false;
        tamilLastCharError2 = false;
        tamilLastCharError3 = false;

        this.fontLogs = fontLogs;

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
        if (lastFont.equalsIgnoreCase("LTRL") && font.equalsIgnoreCase("")) {
            font = "LTRL";
            lastFont = font;
            //System.out.println("LTRL : "+text);

        } else if (lastFont.equalsIgnoreCase("LTRL") && font.equalsIgnoreCase("Arial")) {
            font = "LTRL";
            lastFont = "LTRL";
            //System.out.println("Arial : "+text);


        }

        /*
            Starts font mappings
         */
        if (font.equalsIgnoreCase("Thibus16STru") || font.equalsIgnoreCase("Thibus15STru")
                || font.equalsIgnoreCase("Thibus02S") || font.equalsIgnoreCase("Thibus02STru")
                || font.equalsIgnoreCase("Thibus05STru")) {

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (font.equalsIgnoreCase("FMAbhaya") || font.equalsIgnoreCase("FMAbabld")
                || font.equalsIgnoreCase("FMAbabldBold") || font.equalsIgnoreCase("FMAbhayax")
                || font.equalsIgnoreCase("FMEmaneex") || font.equalsIgnoreCase("FMDeranax")
                //TODO Following font mappings Need to be tested
                || font.equalsIgnoreCase("FMEmanee") ||  font.equalsIgnoreCase("FMBindumathi")
                || font.equalsIgnoreCase("FMMalithi") ||  font.equalsIgnoreCase("FMBasuru")
                || font.equalsIgnoreCase("FMBindu") ||  font.equalsIgnoreCase("FMArjunn")
                || font.equalsIgnoreCase("FMGemunu") ||  font.equalsIgnoreCase("FMSaman")
                || font.equalsIgnoreCase("FMGangani") ||  font.equalsIgnoreCase("FMRajantha")
                || font.equalsIgnoreCase("FMSamantha") ||  font.equalsIgnoreCase("FMGanganee")
                || font.equalsIgnoreCase("Hemawathy") || font.equalsIgnoreCase("SandayaBOI")){

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (font.equalsIgnoreCase( "DL-Manel-bold") || font.equalsIgnoreCase( "DL-Manel")
                || font.equalsIgnoreCase( "DL-Manel-bold.")) {

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (font.equalsIgnoreCase("mutu kata")) {

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (font.equalsIgnoreCase("Sinhala INetFont")) {

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, sinhalaUnicodeFont};

        }
        else if (font.equalsIgnoreCase("Mi_Damidu2000")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = MiDamindu2000.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = MiDamindu2000.fixLastCharError2(text);
            }
            if (MiDamindu2000.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (MiDamindu2000.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }
            unicodeText = MiDamindu2000.convert(text);
            addFontLogs(font, FontState.SUPPORTED);
            logger.info("Converted Text : "+unicodeText);
            return new String[]{unicodeText, sinhalaUnicodeFont};

        }else if (font.equalsIgnoreCase("DL-Lihini")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = DL_Lihini.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = DL_Lihini.fixLastCharError2(text);
            }
            if (DL_Lihini.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (DL_Lihini.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }
            unicodeText = DL_Lihini.convert(text);
            addFontLogs(font, FontState.SUPPORTED);
            logger.info("Converted Text : "+unicodeText);
            return new String[]{unicodeText, sinhalaUnicodeFont};

        }else if (font.equalsIgnoreCase("DL-Araliya") || font.equalsIgnoreCase("Dimuthu")
                 || font.equalsIgnoreCase("Dimuthu-bld") || font.equalsIgnoreCase("DL-Biso")
                 || font.equalsIgnoreCase("DL-Paras")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = DL_Araliya.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = DL_Araliya.fixLastCharError2(text);
            }
            if (DL_Araliya.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (DL_Araliya.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }
            unicodeText = DL_Araliya.convert(text);
            addFontLogs(font, FontState.SUPPORTED);
            logger.info("Converted Text : "+unicodeText);
            return new String[]{unicodeText, sinhalaUnicodeFont};

        }else if (font.equalsIgnoreCase("Kandy")) {

            tamilLastCharError1 = false;
            tamilLastCharError2 = false;
            tamilLastCharError3 = false;

            if (sinhalaLastCharError1) {
                sinhalaLastCharError1 = false;
                text = Kandy.fixLastCharError(text);
            } else if (sinhalaLastCharError2) {
                sinhalaLastCharError2 = false;
                text = Kandy.fixLastCharError2(text);
            }
            if (Kandy.lastCharError(text)) {
                sinhalaLastCharError1 = true;
                text = text.substring(0, text.length() - 1);
            } else if (Kandy.lastCharError2(text)) {
                sinhalaLastCharError2 = true;
                text = text.substring(0, text.length() - 1);
            }
            unicodeText = Kandy.convert(text);
            addFontLogs(font, FontState.SUPPORTED);
            logger.info("Converted Text : "+unicodeText);
            return new String[]{unicodeText, sinhalaUnicodeFont};

        } else if (font.equalsIgnoreCase("LTRL") || font.equalsIgnoreCase("Arial")) {                // Checking arial is not correct but not incorrect either

            unicodeText = LTRL.convert(text);
            return new String[]{unicodeText, sinhalaUnicodeFont};
        } else if (font.equalsIgnoreCase("T06ThibusTru") || font.equalsIgnoreCase("T02Thibus") || font.equalsIgnoreCase("T04ThibusTru")
                ) {

            sinhalaLastCharError1 = false;
            sinhalaLastCharError2 = false;

            if (tamilLastCharError1) {
                tamilLastCharError1 = false;
                text = ThibusT.fixLastCharError(text);
            } else if (tamilLastCharError2) {
                tamilLastCharError2 = false;
                text = ThibusT.fixLastCharError2(text);
            } else if (tamilLastCharError3) {
                tamilLastCharError3 = false;
                text = ThibusT.fixLastCharError3(text);
            }
            if (ThibusT.lastCharError(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError1 = true;
            } else if (ThibusT.lastCharError2(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError2 = true;
            } else if (ThibusT.lastCharError3(text)) {
                text = text.substring(0, text.length() - 1);
                tamilLastCharError3 = true;
            }

            unicodeText = ThibusT.convert(text);
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (font.equalsIgnoreCase("Kalaham")) {

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (font.equalsIgnoreCase("Nallur")) {

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
            addFontLogs(font, FontState.SUPPORTED);

            return new String[]{unicodeText, tamilUnicodeFont};

        } else if (font.equalsIgnoreCase("Baamini") || font.equalsIgnoreCase("Bamini")) {

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

            addFontLogs(font, FontState.SUPPORTED);

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

            addFontLogs(font, FontState.NO_UNICODE_SUPPORT_AVAILABLE_YET);
            return new String[]{text, font};
        }
    }

    private void addFontLogs(String font, FontState fontState) {

        FontLog fontLog = new FontLog();
        fontLog.setFont(font);
        fontLog.setStatus(fontState);

        for (FontLogAbs fontLogi : fontLogs) {

            if (fontLogi.getFont().equals(fontLog.getFont())) {
                return;
            }
        }
        this.fontLogs.add(fontLog);

    }

}
