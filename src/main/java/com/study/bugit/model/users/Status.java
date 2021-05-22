package com.study.bugit.model.users;

public enum Status {
    ACTIVE ("ACTIVE"),
    DISABLED ("DISABLED");

    private final String name;

    private Status(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public boolean isActive() {
        return this.name.equals(ACTIVE.toString());
    }
}
