package org.pms.sammenu.enums;

public enum  FormType {

    T("T"),
    F("F"),
    OTA("OTA");

    private final String code;

    FormType(String v) {
        code = v;
    }

    public String code() {
        return code;
    }

    public static FormType fromValue(String v) {
        for (FormType c: FormType.values()) {
            if (c.code.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
