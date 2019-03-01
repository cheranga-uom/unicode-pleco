package com.ciperlabs.unicodepleco.documentHandler.util;


public class FontLog extends FontLogAbs {

    private String font;
    private FontState status;

    @Override
    public String getFont() {
        return font;
    }

    @Override
    public void setFont(String font) {
        this.font = font;
    }

    @Override
    public FontState getStatus() {
        return status;
    }

    @Override
    public void setStatus(FontState status) {
        this.status = status;
    }
}
