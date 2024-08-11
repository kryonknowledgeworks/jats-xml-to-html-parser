package com.kryonknowledgeworks.parser;

import java.util.List;


public interface Tag {
    String element();
    List<String> elements();
    Boolean isChildAvailable();
}
