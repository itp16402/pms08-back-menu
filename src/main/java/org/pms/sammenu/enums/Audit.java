package org.pms.sammenu.enums;

public enum Audit {

    TAX(((short) 1)),
    REGULAR(((short) 0));

    private final short code;

    Audit(short v) {
        code = v;
    }

    public short code() {
        return code;
    }

    public static Audit fromValue(short v) {
        for (Audit c: Audit.values()) {
            if (c.code == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
