package org.pms.sammenu.enums;

public enum OptionalStatus {

    MANDATORY((short) 0),
    NON_MANDATORY((short) 1);

    private final short code;

    OptionalStatus(short v) {
        code = v;
    }

    public short code() {
        return code;
    }

    public static OptionalStatus fromValue(short v) {
        for (OptionalStatus c: OptionalStatus.values()) {
            if (c.code == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
