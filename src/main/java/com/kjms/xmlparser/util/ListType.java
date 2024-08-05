package com.kjms.xmlparser.util;

public enum ListType {

    ORDER("order"),BULLET("bullet"),ALPHA_LOWER("alpha-lower"), ALPHA_UPPER("alpha-upper"), ROMAN_LOWER("roman-lower"),ROMAN_UPPER("roman-upper"), SIMPLE("simple");

    private final String name;

    ListType(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
