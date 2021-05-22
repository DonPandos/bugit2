package com.study.bugit.model.users;


public enum Role {
    ROLE_ADMIN ("ROLE_ADMIN"),
    ROLE_USER ("ROLE_USER");

    private final String name;

    private Role(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
