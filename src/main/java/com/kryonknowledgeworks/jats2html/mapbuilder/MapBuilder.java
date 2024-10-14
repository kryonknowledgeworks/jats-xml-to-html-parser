package com.kryonknowledgeworks.jats2html.mapbuilder;

import com.kryonknowledgeworks.jats2html.front.element.Article;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {

    Document document;

    public MapBuilder(Document document) {
        this.document = document;
    }

    public Map<String,Object> buildMap(){
        Article article = new Article(document.getDocumentElement());
        return article.getMapXML();
    }

}
