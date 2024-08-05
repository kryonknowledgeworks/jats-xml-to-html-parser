package com.kjms.xmlparser.mapbuilder;

import com.kjms.xmlparser.front.element.Article;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {

    Document document;

    public static Map<String,Object> XMLmap = new HashMap<>();

    public MapBuilder(Document document) {
        this.document = document;
    }

    public Map<String,Object> buildMap(){
        Article article = new Article(document.getDocumentElement());
        article.getMapXML();
         return XMLmap;
    }

}
