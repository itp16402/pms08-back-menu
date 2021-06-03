package org.pms.sammenu.enums;

public enum BalanceSheetType {

    ELP(0, "elp"),
    DLP(1, "dlp"),
    EGLS(2, "egls");

    private final Integer code;
    private final String description;

    BalanceSheetType(Integer k, String v) {
        code = k;
        description = v;
    }

    public Integer code() {
        return code;
    }

    public String description() {
        return description;
    }

    public static BalanceSheetType fromValue(Integer k) {
        for (BalanceSheetType c: BalanceSheetType.values()) {
            if (c.code.equals(k)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(k));
    }

    public static BalanceSheetType fromValue(String v) {
        for (BalanceSheetType c: BalanceSheetType.values()) {
            if (c.description.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }
}
