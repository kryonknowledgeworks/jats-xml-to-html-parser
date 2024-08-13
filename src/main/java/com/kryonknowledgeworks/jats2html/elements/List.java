package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.ListType;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class List implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<list>";
    public static String ELEMENT_LABEL = "list";

    Node node = null;
    NodeList childNodes = null;
    java.util.List<Node> nodeList = new ArrayList<>();
    String html = "";

    public List(Node node) {
        try{
            this.node = node;

            elementFilter();

            java.util.List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            String listType = node.getAttributes().getNamedItem("list-type").getNodeValue();

            if (listType.equals(ListType.ORDER.getName())){
                html = "<ol class=\"numbered-list\">";
            } else if (listType.equals(ListType.BULLET.getName())){
                html = "<ol class=\"bullet-list\">";
            } else if (listType.equals(ListType.ALPHA_LOWER.getName())){
                html = "<ol class=\"alpha-lower-list\">";
            }else if (listType.equals(ListType.ALPHA_UPPER.getName())){
                html = "<ol class=\"alpha-upper-list\">";
            }else if (listType.equals(ListType.ROMAN_LOWER.getName())){
                html = "<ol class=\"roman-lower-list\">";
            }else if (listType.equals(ListType.ROMAN_UPPER.getName())){
                html = "<ol class=\"roman-upper-list\">";
            } else {
                html = "<ol class=\"no-prefix-list\">";
            }

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }

            this.html += "</ol>";


        }catch (Exception e)
        {
            HandleException.processException(e);
        }
    }

    @Override
    public String element() {
        return html;
    }

    @Override
    public java.util.List<String> elements() {
        return null;
    }

    @Override
    public Boolean isChildAvailable() {
        return null;
    }

    public java.util.List<Node> elementFilter() {

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
