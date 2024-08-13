package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/volume.html
public class Volume implements Tag {
    public static Boolean IMPLEMENT = false;
    public static String ELEMENT_VOLUME_FULL = "<volume>";
    public static String ELEMENT_VOLUME = "volume";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Volume(Node node) {
        this.node = node;
        this.html= Util.getHtmlEscapeData(node.getTextContent());
    }

    @Override
    public String element() {
        return html;
    }

    @Override
    public List<String> elements() {
        return null;
    }

    @Override
    public Boolean isChildAvailable() {
        return null;
    }
}
