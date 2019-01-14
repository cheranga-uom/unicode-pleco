package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Sinhala;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gayan@ciperlabs.com on 4/22/18.
 */
public class SinhalaINet {

    private static Map<String, String> con = null;

    public static String convert(String text) {

        if (con == null) {
            con = new HashMap<String, String>();
            con.put("¿", "ඳ"); // added
//(nso,P"] )= "ඦ"; // added
            con.put("¹", "ඬ"); // added
            con.put("­", "ඟ"); // added even though this looks empty it is not it has the releavent mapping
//(nso,y"] )= "ඟ"; // not sure if this is correct -sagnaka ha does not exist
            con.put("¯", "ඡ"); //=
            con.put("¬", "ඞ");
            con.put("Ç", "ර");
            con.put("Ë", "ෂ");
            con.put("Å", "ඹ");
            con.put("°", "ජ");
            con.put("·", "ඪ");
            con.put("«", "ඝ");
            con.put("©", "ඛ");
            con.put("Î", "ළ");
            con.put("¸", "ණ");
            con.put("Á", "ඵ");
            con.put("µ", "ඨ");
            con.put("Ý", "ළු");
            con.put("Ê", "ශ");
            con.put("²", "ඤ");
            con.put("³", "ඥ");
            con.put("¿", "ඳ");
            //(nso,"] =) "ඬ";
            con.put("±", "ඣ");
            //(nso,"] =) "ඣ";
            //(nso,"] =) "ඟ";
            con.put("Â", "බ");
            con.put("®", "ච");
            con.put("¶", "ඩ");
            //(nso,M"]=) "ළු";
            //(nso,"] =) "ඵ";
            con.put("Ï", "ෆ");
            con.put("ª", "ග");
            con.put("Í", "හ");
            //(nso,"] =) "ජ";
            con.put("¨", "ක");
            con.put("È", "ල");
            con.put("Ä", "ම");
            con.put("¾", "න");
            con.put("À", "ප");
            con.put("¼", "ද");
            //(nso,"] =) "ර";
            con.put("Ì", "ස");
            con.put("´", "ට");
            con.put("É", "ව");
            con.put("º", "ත");
            con.put("Ã", "භ");
            con.put("Æ", "ය");
            con.put("½", "ධ");
            con.put("»", "ථ");
            // conso["sÀ"] = "ඳ";
        }

        text = text.replaceAll("A", "a");
        text = text.replaceAll("=", "q");
        text = text.replaceAll("\\+", "Q");

        // multiple vowels of same type replaced by one
        text = text.replaceAll("a{2,}", "a"); //"්"
        text = text.replaceAll("q{2,}", "q"); //"ු"
        text = text.replaceAll("Q{2,}", "Q"); //"ූ",
        text = text.replaceAll("s{2,}", "s"); //"ි"
        text = text.replaceAll("S{2,}", "S"); //"ී"
        text = text.replaceAll("%{2,}", "%"); //rakaransaya

        // uncommon seqs - might want to replicate if common
        text = text.replaceAll("ff;%", "ත්‍රෛ");
        text = text.replaceAll("ïÀòð", "ප්‍රෞ");

        // repl
        //e.g. text = text.replaceAll("ffk", "නෛ");
        text = replaceSeq("ïï", "", "", "ෛ", text);
        text = text.replaceAll("ïï¦", "එෛ"); // special non-conso

        // repl
        //e.g. text = text.replaceAll("ïkHda", "න්‍යෝ");
        text = replaceSeq("ï", "ñåà", "", "්‍යෝ", text);

        // repl
        //e.g. text = text.replaceAll("ï;Hd", "ත්‍යො");
        text = replaceSeq("ï", "ñå", "", "්‍යො", text);

        // repl
        // e.g. text = text.replaceAll("ï;H", "ත්‍යෙ");
        text = replaceSeq("ï", "ñ", "", "\\u0DCA\\u200D\\u0DBA\\u0DD9", text); //්‍යෙ

        // repl
        text = text.replaceAll("ïËòåà", "ෂ්‍රෝ");
        text = text.replaceAll("ï«òåà", "‍ඝ්‍රෝ");
        text = text.replaceAll("ïÊòåà", "ශ්‍රෝ");
        // text = text.replaceAll("ïCIòåà", "ක්‍ෂ්‍රෝ");
        // text = text.replaceAll("ïËòåà", "ක්‍ෂ්‍රෝ");
        text = text.replaceAll("ïÂòåà", "බ්‍රෝ");
        text = text.replaceAll("ï¶òåà", "ඩ්‍රෝ");
        text = text.replaceAll("ïÏòåà", "ෆ්‍රෝ");
        text = text.replaceAll("ïªòåà", "ග්‍රෝ");
        text = text.replaceAll("ï¨òåà", "ක්‍රෝ");
        text = text.replaceAll("ïÀòåà", "ප්‍රෝ");
        text = text.replaceAll("ï÷åà", "ද්‍රෝ");
        text = text.replaceAll("ïÌòåà", "ස්‍රෝ");
        text = text.replaceAll("ï´òåà", "ට්‍රෝ");
        text = text.replaceAll("ïºòåà", "ත්‍රෝ");

        // repl
        text = text.replaceAll("ïÊòå", "ශ්‍රො");
        text = text.replaceAll("ï¶òå", "ඩ්‍රො");
        text = text.replaceAll("ïÏòå", "ෆ්‍රො");
        text = text.replaceAll("ïªòå", "ග්‍රො");
        text = text.replaceAll("ï¨òå", "ක්‍රො");
        text = text.replaceAll("ïÀòå", "ප්‍රො");
        text = text.replaceAll("ïÌòå", "ස්‍රො");
        text = text.replaceAll("ï´òå", "ට්‍රො");
        text = text.replaceAll("ïºòå", "ත්‍රො");

        // sp
        text = text.replaceAll("ï÷å", "ද්‍රො");

        // repl
        text = text.replaceAll("%a", "a%"); // can swap
        text = text.replaceAll("ïÊàò", "ශ්‍රේ");
        text = text.replaceAll("ïÏàò", "ෆ්‍රේ");
        text = text.replaceAll("ïªàò", "ග්‍රේ");
        text = text.replaceAll("ï¨àò", "ක්‍රේ");
        text = text.replaceAll("ïÀàò", "ප්‍රේ");
        text = text.replaceAll("ïÌàò", "ස්‍රේ");
        text = text.replaceAll("ïºàò", "ත්‍රේ");

        //sp
        text = text.replaceAll("ï‰ò", "බ්‍රේ");
        text = text.replaceAll("ï†ò", "ඩ්‍රේ");
        text = text.replaceAll("ï÷à", "ද්‍රේ");
        text = text.replaceAll("ïˆò", "ධ්‍රේ");

        // repl
        text = text.replaceAll("ïËò", "ෂ්‍රෙ");
        text = text.replaceAll("ïÊò", "ශ්‍රෙ");
        text = text.replaceAll("ïÂò", "බ්‍රෙ");
        text = text.replaceAll("ïÏò", "ෆ්‍රෙ");
        text = text.replaceAll("ïªò", "ග්‍රෙ");
        text = text.replaceAll("ï¨ò", "ක්‍රෙ");
        text = text.replaceAll("ïÀò", "ප්‍රෙ");
        text = text.replaceAll("ïÌò", "ස්‍රෙ");
        text = text.replaceAll("ïºò", "ත්‍රෙ");
        text = text.replaceAll("ïÃò", "භ්‍රෙ");
        text = text.replaceAll("ï½ò", "ධ්‍රෙ");

        //sp
        text = text.replaceAll("ï÷", "ද්‍රෙ");

        // repl
        // e.g. text = text.replaceAll("ïk!", "නෞ");
        text = replaceSeq("ï", "ð", "", "ෞ", text);

        // repl
        // e.g. text = text.replaceAll("ïkda", "නෝ");
        text = replaceSeq("ï", "åà", "", "ෝ", text);

        // repl
        // e.g. text = text.replaceAll("ïkd", "නො");
        text = replaceSeq("ï", "å", "", "ො", text);

        // repl
        // e.g. text = text.replaceAll("ïka", "නේ");
        text = replaceSeq("ï", "à", "", "ේ", text);
        //text = replaceSeq("ï", "A", "", "ේ"); // shorter hal glyph is 'A' e.g. in ළේ

        // sp
        text = text.replaceAll("ïØ", "ඡේ");
        text = text.replaceAll("ï…", "ටේ");
        text = text.replaceAll("ïŒ", "වේ");
        text = text.replaceAll("ï‹", "ඹේ");
        text = text.replaceAll("ïŠ", "මේ");
        text = text.replaceAll("ï‰", "බේ");
        text = text.replaceAll("ïˆ", "ධේ");
        text = text.replaceAll("ï‡", "ඬේ");
        // text = text.replaceAll("ïâ", "ඞේ");
        text = text.replaceAll("ï†", "ඩේ");
        text = text.replaceAll("ïÚ", "රේ");
        text = text.replaceAll("ï‚", "ඛේ");
        text = text.replaceAll("ïƒ", "චේ");
        text = text.replaceAll("ïÙ", "ජේ");

        // repl
        // e.g. text = text.replaceAll("ïk", "නෙ");
        text = replaceSeq("ï", "", "", "ෙ", text);

        text = text.replaceAll("Æñþ", "ර්‍ය්‍ය"); //ර්ය  
        // text = text.replaceAll("hœ", "ර්‍ය්‍ය"); //ර්‍්‍ය
        // the font does not seem to support ර්‍්‍ය for anything other than ය 
        // so keep the replication disabled for now
        //text = replaceSeq("", "H_", "\\u0DBB\\u0DCA\\u200D", "්‍ය");
        //text = replaceSeq("", "œ", "\\u0DBB\\u0DCA\\u200D", "්‍ය");
        // e.g. text = text.replaceAll("h_", "ර්‍ය");  // added 
        text = replaceSeq("", "_", "\\u0DBB\\u0DCA\\u200D", "", text);
        // use replication rules to replace above

        // --------- special letters (mostly special glyphs in the FM font)
        text = text.replaceAll("Êéò", "ශ්‍රී");
        text = text.replaceAll("Çç", "රූ");
        text = text.replaceAll("Çæ", "රු");
        text = text.replaceAll("Ü", "රෑ"); //added
        text = text.replaceAll("Û", "රැ"); //=
        text = text.replaceAll("¿æ", "ඳැ"); //=
        text = text.replaceAll("¿å", "ඳා"); //added
        text = text.replaceAll("õ", "ලූ");
        text = text.replaceAll("ö", "ලු"); //corrected
        text = text.replaceAll("÷", "ද්‍ර");
        text = text.replaceAll("ß", "ඳු");
        // text = text.replaceAll("`ÿ", "ඳු"); //added
        text = text.replaceAll("ó", "දු");
        text = text.replaceAll("á", "ඳූ"); //added
        // text = text.replaceAll("`¥", "ඳූ"); //added
        text = text.replaceAll("ô", "දූ"); //added
        //text = text.replaceAll("μ", "ද්‍ය"); //one version of the FM fonts use this
        text = text.replaceAll("²ì", "ඤූ"); //=
        text = text.replaceAll("²ê", "ඤු"); //=
        text = text.replaceAll("¿û", "ඳී");
        // text = text.replaceAll("`§", "ඳී");
        text = text.replaceAll("¼û", "දී");
        text = text.replaceAll("š", "ඣී");
        text = text.replaceAll("µý", "ඨී");
        text = text.replaceAll("Ó", "ඡී");
        text = text.replaceAll("˜", "ඛී");
        text = text.replaceAll("×", "රී");
        text = text.replaceAll("·ý", "ඪී");
        text = text.replaceAll("»ý", "ථී");
        text = text.replaceAll("Õ", "ජී");
        text = text.replaceAll("™", "චී");
        text = text.replaceAll("Áý", "ඵී");
        // text = text.replaceAll("Ý", "ඵී");
        text = text.replaceAll("›", "ටී");
        text = text.replaceAll("é", "ී");            // Changed Looking at the converted results @Gayan
        // text = text.replaceAll("`ã", "ඬී");
        text = text.replaceAll("", "ඩී");  // though we cant see there is a mapping
        text = text.replaceAll("", "ධී");
        text = text.replaceAll("Ÿ", "බී");
        text = text.replaceAll("ä", "මී");
        text = text.replaceAll("Ð", "ඹී");
        text = text.replaceAll("Ñ", "වී");
        // text = text.replaceAll("Ú", "ඵී");
        text = text.replaceAll("¸ý", "ණී");
        // text = text.replaceAll("“", " ර්‍ණ");
        text = text.replaceAll("¿ú", "ඳි");
        // text = text.replaceAll("`È", "ඳි");
        text = text.replaceAll("¼ú", "දි");
        text = text.replaceAll("", "ඣි");
        text = text.replaceAll("µø", "ඨි");
        text = text.replaceAll("", "ඛි");
        text = text.replaceAll("Ö", "රි");
        text = text.replaceAll("·ü", "ඪි");
        text = text.replaceAll("", "චි");
        text = text.replaceAll("»ü", "ථි");
        text = text.replaceAll("", "ටි");
        text = text.replaceAll("’", "ඬි");
        // text = text.replaceAll("`ä", "ඬි");
        text = text.replaceAll("‘", "ඩි");
        text = text.replaceAll("“", "ධි");
        text = text.replaceAll("”", "බි");
        text = text.replaceAll("•", "මි");
        text = text.replaceAll("Ò", "ඡි"); //added
        text = text.replaceAll("Ô", "ජි");
        text = text.replaceAll("–", "ඹි");
        text = text.replaceAll("—", "වි");
        text = text.replaceAll("", "ඣි");
        text = text.replaceAll("¸ü", "ණි");
        // text = text.replaceAll("‹", "ද්‍ධි"); //added
        // text = text.replaceAll("‰", "ද්‍වි"); //added
        text = text.replaceAll("Ø", "ඡ්");
        text = text.replaceAll("…", "ට්");
        text = text.replaceAll("Œ", "ව්");
        text = text.replaceAll("‹", "ඹ්");
        text = text.replaceAll("Š", "ම්");
        text = text.replaceAll("‰", "බ්");
        text = text.replaceAll("ˆ", "ධ්");
        // text = text.replaceAll("‡", "ඞ්");
        text = text.replaceAll("‡", "ඬ්");
        // text = text.replaceAll("`Ù", "ඬ්");
        text = text.replaceAll("†", "ඩ්");
        text = text.replaceAll("Ú", "ර්");
        text = text.replaceAll("‚", "ඛ්");
        text = text.replaceAll("ƒ", "ච්");
        text = text.replaceAll("Ù", "ජ්");
        text = text.replaceAll("³å", "ඥා"); //ඥ
        text = text.replaceAll("²å", "ඤා"); //ඤ
        text = text.replaceAll("ºà", "ත්‍"); // todo - can we make bandi akuru or these
        text = text.replaceAll("¾à", "න්‍");
        text = text.replaceAll("¼å", "දා");
        text = text.replaceAll("¼æ", "දැ");
        // text = text.replaceAll("ˆ", "න්‍දා");


        // --------------- vowels
        // text = text.replaceAll("t", "ඓ");			// Not sure need to check back with darshana
        text = text.replaceAll("ï¦", "ඓ");
        text = text.replaceAll("§ð", "ඖ");
        text = text.replaceAll("¤ð", "ඌ");
        text = text.replaceAll("¡ç", "ඈ");
        text = text.replaceAll("¡å", "ආ");
        text = text.replaceAll("¡æ", "ඇ");
        text = text.replaceAll("¦à", "ඒ");
        text = text.replaceAll("¥î", "ඎ");
        text = text.replaceAll("¥", "ඍ");
        // text = text.replaceAll("Ï", "ඐ");
        // text = text.replaceAll("´", "ඕ");
        // text = text.replaceAll("Ta", "ඕ"); //error correcting
        // text = text.replaceAll("Ì", "ඏ");
        text = text.replaceAll("¢", "ඉ");
        text = text.replaceAll("£", "ඊ");
        text = text.replaceAll("¦", "එ");
        text = text.replaceAll("§", "ඔ");
        text = text.replaceAll("¤", "උ");
        text = text.replaceAll("¡", "අ");

        // few special cases
        text = text.replaceAll("²", "ඤ");
        // text = text.replaceAll("`G", "ට්ඨ"); // very rare

        // -----------consonants repl
        // e.g. text = text.replaceAll("k", "න");
        text = replaceSeq("", "", "", "", text);

        // needed to cover the cases like ක්‍ෂ that are not included in consonants 
        // text = text.replaceAll("C", "ක්‍");

        // ------- dependant vowels
        text = text.replaceAll("s%", "%s"); // this set added
        text = text.replaceAll("S%", "%S");
        text = text.replaceAll("èò", "්‍රි"); // e.g ත්‍රි
        // text = text.replaceAll("%S", "්‍රී"); // e.g. ත්‍රී

        text = text.replaceAll("ñ", "්‍ය"); // works fine
        text = text.replaceAll("ò", "්‍ර"); // changed
        text = text.replaceAll("æ", "ැ");
        text = text.replaceAll("ç", "ෑ");
        text = text.replaceAll("ê", "ු");
        text = text.replaceAll("ì", "ූ");
        text = text.replaceAll("è", "ි");
        text = text.replaceAll("é", "ී");
        text = text.replaceAll("îî", "ෲ");
        text = text.replaceAll("î", "ෘ");
        text = text.replaceAll("ð", "ෳ");
        // text = text.replaceAll("!", "ෟ");
        text = text.replaceAll("å", "ා");
        text = text.replaceAll("à", "්");
        text = text.replaceAll("â", "ං");
        text = text.replaceAll("ã", "ඃ");
        text = text.replaceAll("é", "ී");
        // text = text.replaceAll(" ‘", "ි");
        text = text.replaceAll("ï", "ෙ");
        text = text.replaceAll("ë", "ු");                // Added By @Gayan
        text = text.replaceAll("í", "ූ");                // Added By @Gayan

        return text;
    }

    private static String replaceSeq(String fm_pre, String fm_post, String un_pre, String un_post, String text) {
        for (String fm : con.keySet()) {
            String temp = fm_pre + fm + fm_post;
            text = text.replaceAll(temp, un_pre + con.get(fm) + un_post);
        }
        return text;
    }


    public static boolean lastCharError(String text) {
        if (text.endsWith("ï")) {
            return true;
        } else {
            return false;
        }
    }

    public static String fixLastCharError(String text) {
        return "ï" + text;
    }

    public static boolean lastCharError2(String text) {
//        if(text.endsWith("Z")){
//            return true;
//        }
//        else{
//            return false;
//        }
        return false;
    }

    public static String fixLastCharError2(String text) {
        return text;
    }
}
