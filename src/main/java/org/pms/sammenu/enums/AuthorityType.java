package org.pms.sammenu.enums;

public enum AuthorityType {

    OWNER((short) 1, "OWNER"),
    MANAGER((short) 2, "MANAGER"),
    MEMBER((short) 3, "MEMBER"),
    EQCR((short) 4, "EQCR"),
    PARTNER((short) 5, "PARTNER");

    private final short code;

    private final String description;

    AuthorityType(short code, String description) {
        this.code = code;

        this.description = description;
    }

    public short code() {
        return code;
    }

    public String description() {
        return description;
    }

    public static AuthorityType fromCode(short v) {
        for (AuthorityType c: AuthorityType.values()) {
            if (c.code == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.valueOf(v));
    }

}

