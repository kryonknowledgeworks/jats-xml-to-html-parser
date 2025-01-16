package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class MmlMath implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<mml:math>";
    public static String ELEMENT = "mml:math";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public MmlMath(Node node, MetaDataBuilder metaDataBuilder) {
        try {

            this.node = node;
            elementFilter();

            this.html += Util.convertToString(node);

            this.html = this.html.replaceAll("mml:", "");

        } catch (Exception e) {
            HandleException.processException(e);
        }

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

    public List<Node> elementFilter() {

        this.childNodes = this.node.getChildNodes();

        for (int i = 0; i < this.childNodes.getLength(); i++) {

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }

        }
        return nodeList;
    }
}
