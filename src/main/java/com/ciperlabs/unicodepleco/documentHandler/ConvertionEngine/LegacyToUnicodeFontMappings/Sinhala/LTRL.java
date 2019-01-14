package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings.Sinhala;

/**
 * Created by gayan@ciperlabs.com on 4/21/18.
 */
public class LTRL {

    public static String convert(String text) {

        text = text.replaceAll("m", "ṃ");
        // text = text.replaceAll("",".");
        text = text.replaceAll("", "̣");
        text = text.replaceAll("", "̣");
        text = text.replaceAll("", "̣");

        text = text.replaceAll("ä", "ä");
        text = text.replaceAll("å", "ǟ");
        text = text.replaceAll("ê", "ē");
        text = text.replaceAll("þ", "ṛ");
        text = text.replaceAll("ƒ", "ṭ");
        text = text.replaceAll("ÿ", "ṝ");
        text = text.replaceAll("û", "ū");
        text = text.replaceAll("î", "ī");
        text = text.replaceAll("ô", "ō");
// ã
//     ä å ê þ ƒ ÿ û î ô 

        // text = text.replaceAll("ã", "ã");
        text = text.replaceAll("Á", "ś");
        text = text.replaceAll("ð", "ḍ");
        text = text.replaceAll("ò", "ḥ");
        text = text.replaceAll("è", "l̤");  // couldn't find unicode
        text = text.replaceAll("ç", "ḷ");
        // text = text.replaceAll("û", "ḷ");
        text = text.replaceAll("é ", "l̤̄");  // couldn't find unicode

        // ã  Á ð ò è ç é

        text = text.replaceAll("À", "ṣ");
        text = text.replaceAll("ý", "ñ");
        text = text.replaceAll("ú", "n̆");
        text = text.replaceAll("ø", "ṅ");
        text = text.replaceAll("ù", "ṇ");
        text = text.replaceAll("í", "ṃ");
        text = text.replaceAll("ì", "ṁ");
        text = text.replaceAll("ó", "m̆");

        // À ý ú ø ù í ì ó 

        text = text.replaceAll("Ã", "Ã");
        text = text.replaceAll("á", "Ś");
        text = text.replaceAll("Ð", "Ḍ");
        text = text.replaceAll("Æ", "Ḥ");
        text = text.replaceAll("Ó", "L̤");
        text = text.replaceAll("Ò", "Ḷ");
        text = text.replaceAll("Ô", "L̤̄");
        text = text.replaceAll("à", "Ṣ");
        // ÃáÐÆÓÒÔà
        // text = text.replaceAll("Ä", "ත්‍රෛ");
        text = text.replaceAll("Å", "Ǟ");
        text = text.replaceAll("Ê", "Ē");
        text = text.replaceAll("Þ", "Ṛ");
        text = text.replaceAll("â", "Ṭ");
        // ÄÅÊÞâ
        text = text.replaceAll("Â", "Ā");
        // text = text.replaceAll("Ð", "Ḍ");
        // text = text.replaceAll("Æ", "Ḥ");
        // text = text.replaceAll("Ó", "නෛ");
        // text = text.replaceAll("Ò", "Ḷ");
        // text = text.replaceAll("Ô", "මෛ");
        text = text.replaceAll("¨", "¨");
        // ÂÐÆÓÒÔ¨
        text = text.replaceAll("à", "Ṣ");
        text = text.replaceAll("Ñ", "Ñ");
        text = text.replaceAll("Ú", "Ṅ");
        text = text.replaceAll("Û", "Ṇ");
        text = text.replaceAll("Ø", "Ṃ");
        text = text.replaceAll("×", "Ṁ");
        text = text.replaceAll("Ù", "M̆");

        //àÑÚÛØ×Ù
        // text = text.replaceAll("Ä", "කෛ");
        // text = text.replaceAll("Ü", "මෛ");
        // text = text.replaceAll("Ö", "නෛ");
        // Ä Ü Ö
        text = text.replaceAll("Ý", "N̆");
        text = text.replaceAll("Ç", "Ū");
        // ÑÝÚÛØ×Ù

        return text;
    }
}
