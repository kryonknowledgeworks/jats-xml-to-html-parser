package com.kryonknowledgeworks.jats2html;

import java.util.List;


public interface Tag {
    String element();
    List<String> elements();
    Boolean isChildAvailable();
}
