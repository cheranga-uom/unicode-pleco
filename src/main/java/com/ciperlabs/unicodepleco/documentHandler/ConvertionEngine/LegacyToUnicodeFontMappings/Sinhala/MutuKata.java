package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Sinhala;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gayan@ciperlabs.com on 4/22/18.
 */
public class MutuKata {
    private static Map<String, String> con = null;

    public static String convert(String text) {

        if (con == null) {
            con = new HashMap<String, String>();

            con.put("CI", "ක්‍ෂ");
            con.put("Cj", "ක්‍ව");
            con.put("Ì", "ක්‍ෂ");
            con.put("Ï", "ත්‍ථ");
            con.put("Fg", "ත්ව");
            con.put("Jo", "න්ද");
            con.put("JO", "න්‍ධ");
            // co(so[,Š = "ද්)‍ධ";
            // co(so[,`] = )"ද්‍ධ";
            // co(so[,„ = "ද්)‍ව";
            // co(so[,`] = )"ද්‍ව";
            // fo(low,nare un)icode consos
            con.put("Zo", "ඳ"); // not sure
            con.put("Zc", "ඦ"); // not sure
            con.put("`v", "ඬ"); // not sure
            con.put("`\\.", "ඟ"); // not sure
            // co(so[,`] = )"ඟ"; // not sure if this is correct -sagnaka ha does not exist
            con.put("P", "ඡ"); //=
            con.put("X", "ඞ");
            con.put("r", "ර");
            con.put("I", "ෂ");
            con.put("U", "ඹ");
            con.put("c", "ජ");
            con.put("V", "ඪ");
            con.put("\\>", "ඝ");
            con.put("L", "ඛ");
            con.put("<", "ළ");
            con.put("K", "ණ");
            con.put("M", "ඵ");
            con.put("G", "ඨ");
            con.put("¿", "ළු");
            con.put("Y", "ශ");
            con.put("\\[", "ඤ");
            con.put("\\{", "ඥ");
            con.put("\\|", "ඳ");
            con.put("Ë", "ඬ");
            con.put("CO", "ඣ");
            // co(so[,® = "ඣ");
            con.put("Õ", "ඟ");
            con.put("n", "බ");
            con.put("p", "ච");
            con.put("v", "ඩ");
            con.put("`M", "ළු");
            con.put("M", "ඵ");
            con.put("\\*", "ෆ");
            con.put("\\.", "ග");
            con.put("y", "හ");
            con.put("c", "ජ");
            con.put("l", "ක");
            con.put("\\,", "ල");
            con.put("u", "ම");
            con.put("k", "න");
            con.put("m", "ප");
            con.put("o", "ද");
            con.put("r", "ර");
            con.put("i", "ස");
            con.put("g", "ට");
            con.put("j", "ව");
            con.put("\\;", "ත");
            con.put("N", "භ");
            con.put("h", "ය");
            con.put("O", "ධ");
            con.put(":", "ථ");

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
        text = text.replaceAll("fm%!", "ප්‍රෞ");

        // repl
        //e.g. text = text.replaceAll("ffk", "නෛ");
        text = replaceSeq("ff", "", "", "ෛ", text);
        text = text.replaceAll("fft", "එෛ"); // special non-conso

        // repl
        //e.g. text = text.replaceAll("fkHda", "න්‍යෝ");
        text = replaceSeq("f", "Hda", "", "්‍යෝ", text);

        // repl
        //e.g. text = text.replaceAll("f;Hd", "ත්‍යො");
        text = replaceSeq("f", "Hd", "", "්‍යො", text);

        // repl
        // e.g. text = text.replaceAll("f;H", "ත්‍යෙ");
        text = replaceSeq("f", "H", "", "\\u0DCA\\u200D\\u0DBA\\u0DD9", text); //්‍යෙ

        // repl
        text = text.replaceAll("fI%da", "ෂ්‍රෝ");
        text = text.replaceAll("f>%da", "‍ඝ්‍රෝ");
        text = text.replaceAll("fY%da", "ශ්‍රෝ");
        text = text.replaceAll("fCI%da", "ක්‍ෂ්‍රෝ");
        text = text.replaceAll("fÌ%da", "ක්‍ෂ්‍රෝ");
        text = text.replaceAll("fn%da", "බ්‍රෝ");
        text = text.replaceAll("fv%da", "ඩ්‍රෝ");
        text = text.replaceAll("f\\*%da", "ෆ්‍රෝ");
        text = text.replaceAll("f\\.%da", "ග්‍රෝ");
        text = text.replaceAll("fl%da", "ක්‍රෝ");
        text = text.replaceAll("fm%da", "ප්‍රෝ");
        text = text.replaceAll("føda", "ද්‍රෝ");
        text = text.replaceAll("fi%da", "ස්‍රෝ");
        text = text.replaceAll("fg%da", "ට්‍රෝ");
        text = text.replaceAll("f\\;%da", "ත්‍රෝ");

        // repl
        text = text.replaceAll("fY%d", "ශ්‍රො");
        text = text.replaceAll("fv%d", "ඩ්‍රො");
        text = text.replaceAll("f\\*%d", "ෆ්‍රො");
        text = text.replaceAll("f\\.%d", "ග්‍රො");
        text = text.replaceAll("fl%d", "ක්‍රො");
        text = text.replaceAll("fm%d", "ප්‍රො");
        text = text.replaceAll("fi%d", "ස්‍රො");
        text = text.replaceAll("fg%d", "ට්‍රො");
        text = text.replaceAll("f\\;%d", "ත්‍රො");

        // sp
        text = text.replaceAll("fød", "ද්‍රො");

        // repl
        text = text.replaceAll("%a", "a%"); // can swap
        text = text.replaceAll("fYa%", "ශ්‍රේ");
        text = text.replaceAll("f\\*a%", "ෆ්‍රේ");
        text = text.replaceAll("f\\.a%", "ග්‍රේ");
        text = text.replaceAll("fla%", "ක්‍රේ");
        text = text.replaceAll("fma%", "ප්‍රේ");
        text = text.replaceAll("fia%", "ස්‍රේ");
        text = text.replaceAll("f\\;a%", "ත්‍රේ");

        //sp
        text = text.replaceAll("fí%", "බ්‍රේ");
        text = text.replaceAll("fâ%", "ඩ්‍රේ");
        text = text.replaceAll("føa", "ද්‍රේ");
        text = text.replaceAll("fè%", "ධ්‍රේ");

        // repl
        text = text.replaceAll("fI%", "ෂ්‍රෙ");
        text = text.replaceAll("fY%", "ශ්‍රෙ");
        text = text.replaceAll("fn%", "බ්‍රෙ");
        text = text.replaceAll("f\\*%", "ෆ්‍රෙ");
        text = text.replaceAll("f\\.%", "ග්‍රෙ");
        text = text.replaceAll("fl%", "ක්‍රෙ");
        text = text.replaceAll("fm%", "ප්‍රෙ");
        text = text.replaceAll("fi%", "ස්‍රෙ");
        text = text.replaceAll("f\\;%", "ත්‍රෙ");
        text = text.replaceAll("fN%", "භ්‍රෙ");
        text = text.replaceAll("fO%", "ධ්‍රෙ");

        //sp
        text = text.replaceAll("fø", "ද්‍රෙ");

        // repl
        // e.g. text = text.replaceAll("fk!", "නෞ");
        text = replaceSeq("f", "!", "", "ෞ", text);

        // repl
        // e.g. text = text.replaceAll("fkda", "නෝ");
        text = replaceSeq("f", "da", "", "ෝ", text);

        // repl
        // e.g. text = text.replaceAll("fkd", "නො");
        text = replaceSeq("f", "d", "", "ො", text);

        // repl
        // e.g. text = text.replaceAll("fka", "නේ");
        text = replaceSeq("f", "a", "", "ේ", text);
        //text = replaceSeq("f", "A", "", "ේ"); // shorter hal glyph is 'A' e.g. in ළේ

        // sp
        text = text.replaceAll("fþ", "ඡේ");
        text = text.replaceAll("fÜ", "ටේ");
        text = text.replaceAll("fõ", "වේ");
        text = text.replaceAll("fò", "ඹේ");
        text = text.replaceAll("fï", "මේ");
        text = text.replaceAll("fí", "බේ");
        text = text.replaceAll("fè", "ධේ");
        text = text.replaceAll("fâ", "ඬේ");
        text = text.replaceAll("fÙ", "ඞේ");
        // text = text.replaceAll("fâ", "ඩේ");
        text = text.replaceAll("f¾", "රේ");
        text = text.replaceAll("fÄ", "ඛේ");
        text = text.replaceAll("fÉ", "චේ");
        text = text.replaceAll("f–", "ජේ"); // chnaged fm
        text = text.replaceAll("fÊ", "ජේ"); // added
        // repl
        // e.g. text = text.replaceAll("fk", "නෙ");
        text = replaceSeq("f", "", "", "ෙ", text);

        text = text.replaceAll("hH_", "ර්‍ය්‍ය"); //ර්ය
        text = text.replaceAll("hœ", "ර්‍ය්‍ය"); //ර්‍්‍ය   // note
        // the font does not seem to support ර්‍්‍ය for anything other than ය
        // so keep the replication disabled for now
        //text = replaceSeq("", "H_", "\\u0DBB\\u0DCA\\u200D", "්‍ය");
        //text = replaceSeq("", "œ", "\\u0DBB\\u0DCA\\u200D", "්‍ය");
        // e.g. text = text.replaceAll("h_", "ර්‍ය");  // added
        text = replaceSeq("", "_", "\\u0DBB\\u0DCA\\u200D", "", text);
        // use replication rules to replace above

        // --------- special letters (mostly special glyphs in the FM font)
        text = text.replaceAll("rE", "රූ");
        text = text.replaceAll("re", "රු");
        text = text.replaceAll("\\?", "රෑ"); //added
        text = text.replaceAll("\\/", "රැ"); //=
        text = text.replaceAll("ƒ", "ඳැ"); //=
        // text = text.replaceAll("\\\\", "ඳා"); //added
        text = text.replaceAll("Æ", "ලු");
        text = text.replaceAll("¨", "ලූ"); //corrected
        text = text.replaceAll("ø", "ද්‍ර");
        text = text.replaceAll("÷", "ඳු");
        text = text.replaceAll("Zÿ", "ඳු"); //not sure
        text = text.replaceAll("ÿ", "දු");
        text = text.replaceAll("ª", "ඳූ"); //added
        text = text.replaceAll("Z¥", "ඳූ"); //not sure
        text = text.replaceAll("¥", "දූ"); //added
        //text = text.replaceAll("μ", "ද්‍ය"); //one version of the FM fonts use this
        // text = text.replaceAll("ü", "ඤූ"); //=
        // text = text.replaceAll("û", "ඤු"); //=
        text = text.replaceAll("£", "ඳී");
        text = text.replaceAll("Z§", "ඳී"); // not sure
        text = text.replaceAll("§", "දී");
        text = text.replaceAll("Cë", "ඣී");
        text = text.replaceAll("À", "ඨී");
        text = text.replaceAll("Â", "ඡී");
        text = text.replaceAll("Ç", "ඛී");
        text = text.replaceAll("Í", "රී");
        text = text.replaceAll("Ð", "ඪී");
        text = text.replaceAll("Ò", "ථී");
        text = text.replaceAll("„", "ජී");
        text = text.replaceAll("Ö", "චී");
        text = text.replaceAll("Ú", "ඵී");
        text = text.replaceAll("Ý", "ඵී");
        text = text.replaceAll("à", "ටී");
        text = text.replaceAll("é", "ඬී");
        // text = text.replaceAll("`ã", "ඬී");
        text = text.replaceAll("ã", "ඩී");
        text = text.replaceAll("ë", "ධී");
        text = text.replaceAll("î", "බී");
        text = text.replaceAll("ó", "මී");
        text = text.replaceAll("ö", "ඹී");
        text = text.replaceAll("ù", "වී");
        text = text.replaceAll("Ý", "ඵී");
        text = text.replaceAll("”", "ණී");
        text = text.replaceAll("“", " ර්‍ණ");
        text = text.replaceAll("¢", "ඳි");
        text = text.replaceAll("ZÈ", "ඳි"); //not sure
        text = text.replaceAll("È", "දි");
        // text = text.replaceAll("¯", "ඣි");
        text = text.replaceAll("À", "ඨි");
        text = text.replaceAll("Å", "ඛි");
        text = text.replaceAll("ß", "රි");
        text = text.replaceAll("Î", "ඪි");
        text = text.replaceAll("Ñ", "චි");
        text = text.replaceAll("Ó", "ථි");
        text = text.replaceAll("á", "ටි");
        text = text.replaceAll("ç", "ඬි");
        text = text.replaceAll("Zä", "ඬි"); // not sure
        text = text.replaceAll("ä", "ඩි");
        text = text.replaceAll("ê", "ධි");
        text = text.replaceAll("ì", "බි");
        text = text.replaceAll("ñ", "මි");
        text = text.replaceAll("ý", "ඡි"); //added
        text = text.replaceAll("ð", "ජි");
        text = text.replaceAll("ô", "ජී");
	text = text.replaceAll("Ô", "ජී");
        text = text.replaceAll("ú", "වි");
        text = text.replaceAll("Cä", "ඣි");
        text = text.replaceAll("‚", "ණි");
        text = text.replaceAll("‹", "ද්‍ධි"); //added
        text = text.replaceAll("‰", "ද්‍වි"); //added
        text = text.replaceAll("þ", "ඡ්");
        text = text.replaceAll("Ü", "ට්");
        text = text.replaceAll("õ", "ව්");
        text = text.replaceAll("ò", "ඹ්");
        text = text.replaceAll("ï", "ම්");
        text = text.replaceAll("í", "බ්");
        text = text.replaceAll("è", "ධ්");
        text = text.replaceAll("â", "ඞ්");
        text = text.replaceAll("å", "ඬ්");
        text = text.replaceAll("ZÙ", "ඬ්"); //not sure
        text = text.replaceAll("Ù", "ඩ්");
        text = text.replaceAll("¾", "ර්");
        text = text.replaceAll("Ä", "ඛ්");
        text = text.replaceAll("É", "ච්");
        text = text.replaceAll("Ê", "ජ්");
        text = text.replaceAll("«", "ඥා"); //ඥ
        text = text.replaceAll("\\[d", "ඤා"); //ඤ
        // text = text.replaceAll("F", "ත්‍"); // todo - can we make bandi akuru for these
        // text = text.replaceAll("J", "න්‍");
        text = text.replaceAll("Þ", "දා");
        text = text.replaceAll("±", "දැ");
        // text = text.replaceAll("ˆ", "න්‍දා");
        // text = text.replaceAll("›", "ශ්‍රී");


        // --------------- vowels
        text = text.replaceAll("ft", "ඓ");
        text = text.replaceAll("T!", "ඖ");
        text = text.replaceAll("W!", "ඌ");
        text = text.replaceAll("wE", "ඈ");
        text = text.replaceAll("wd", "ආ");
        text = text.replaceAll("we", "ඇ");
        text = text.replaceAll("ta", "ඒ");
        text = text.replaceAll("RD", "ඎ");
        text = text.replaceAll("R", "ඍ");
        // text = text.replaceAll("Ï", "ඐ");
        text = text.replaceAll("´", "ඕ");
        text = text.replaceAll("b", "ඉ");
        text = text.replaceAll("B", "ඊ");
        text = text.replaceAll("t", "එ");
        text = text.replaceAll("T", "ඔ");
        text = text.replaceAll("W", "උ");
        text = text.replaceAll("w", "අ");

        // few special cases
        // text = text.replaceAll("`Co", "ඤ");
        text = text.replaceAll("ZG", "ට්ඨ"); // very rare// not sure

        // -----------consonants repl
        // e.g. text = text.replaceAll("k", "න");
        text = replaceSeq("", "", "", "", text);

        // needed to cover the cases like ක්‍ෂ that are not included in consonants
        text = text.replaceAll("C", "ක්‍");

        // ------- dependant vowels
        text = text.replaceAll("s%", "%s");
        text = text.replaceAll("S%", "%S");
        text = text.replaceAll("%s", "්‍රි");
        text = text.replaceAll("%S", "්‍රී");

        text = text.replaceAll("H", "්‍ය");
        text = text.replaceAll("%", "්‍ර");
        text = text.replaceAll("e", "ැ");
        text = text.replaceAll("E", "ෑ");
        text = text.replaceAll("q", "ු");
        text = text.replaceAll("Q", "ූ");
        text = text.replaceAll("s", "ි");
        text = text.replaceAll("S", "ී");
        text = text.replaceAll("DD", "ෲ");
        text = text.replaceAll("D", "ෘ");
        text = text.replaceAll("!!", "ෳ");
        text = text.replaceAll("!", "ෟ");
        text = text.replaceAll("d", "ා");
        text = text.replaceAll("a", "්");
        text = text.replaceAll("x", "ං");
        text = text.replaceAll("#", "ඃ");
        text = text.replaceAll(" ’", "ී");
        text = text.replaceAll(" ‘", "ි");
        text = text.replaceAll("f", "ෙ");

        text = text.replaceAll("'", ".");
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
        if (text.endsWith("f")) {
            return true;
        } else {
            return false;
        }
    }

    public static String fixLastCharError(String text) {
        return "f" + text;
    }

    public static boolean lastCharError2(String text) {
        if (text.endsWith("Z")) {
            return true;
        } else {
            return false;
        }
    }

    public static String fixLastCharError2(String text) {
        return "Z" + text;
    }
}
