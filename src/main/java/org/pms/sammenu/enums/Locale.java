package org.pms.sammenu.enums;

public enum Locale {

    el("el"),
    en("en");

    private final String code;

    Locale(String v) {
        code = v;
    }

    public String code() {
        return code;
    }

    public static Locale fromValue(String v) {
        for (Locale c: Locale.values()) {
            if (c.code.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}