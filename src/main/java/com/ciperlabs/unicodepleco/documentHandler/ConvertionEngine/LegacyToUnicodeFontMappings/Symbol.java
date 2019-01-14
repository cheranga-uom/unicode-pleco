package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.LegacyToUnicodeFontMappings;

public class Symbol {

    public static String convert(String text) {
        if (text.contains("") && text.length() == 1) {

//			System.out.println("Font symbol found...");
            text = "]";
        } else if (text.contains("") && text.length() == 1) {
            text = "[";
        }
//		text.replace("", "\\]");
//		text.replace("", "\\[");
        return text;
    }
}
