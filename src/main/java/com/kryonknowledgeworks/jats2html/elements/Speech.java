package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Speech implements Tag {
    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<speech>";
    public static String ELEMENT_LABEL = "speech";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Speech(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;
            elementFilter();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;
            this.html += "<p>“ ";

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className)) && className.equals("P")) {
                        String speakerContent = "";
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                        speakerContent = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element").replace("<p>","").replace("</p>","");
                        this.html += "<span> ‘"+speakerContent+"’</span>";
                    }else{
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                }
            }
            this.html += " ”</p>";
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
