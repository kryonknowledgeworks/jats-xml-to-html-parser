package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Term implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_FULL = "<term>";
    public static String ELEMENT = "term";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Term(Node node) {
        try{
            this.node = node;
            this.nodeList = Util.getChildNode(node);

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        ClassNameSingleTon.createInstanceFromClassName(className, node1);
                    }
                } else if (!node1.getNodeName().equals("#text")) {
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                }

            }

            this.html+= Util.getHtmlEscapeData(node.getTextContent()) ;
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
}
