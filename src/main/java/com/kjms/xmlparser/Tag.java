package com.kjms.xmlparser;

import java.util.List;


public interface Tag {
    String element();
    List<String> elements();
    Boolean isChildAvailable();
}
