package org.pms.sammenu.enums;

public enum FormStatus {

    SAVED((short) 0),
    COMPLETED((short) 1),
    PROCESSED((short) 2);

    private final Short code;

    FormStatus(Short v) {
        code = v;
    }

    public Short code() {
        return code;
    }

    public static FormStatus fromValue(Short v) {
        for (FormStatus c: FormStatus.values()) {
            if (c.code.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
