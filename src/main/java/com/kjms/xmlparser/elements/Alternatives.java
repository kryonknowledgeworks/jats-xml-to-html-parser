package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/alternatives.html
public class Alternatives implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<alternatives>";
    public static String ELEMENT_LABEL = "alternatives";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Alternatives(Node node) {
        try {

            this.node = node;
            elementFilter();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            List<String> childNodeNames = new ArrayList<>();

            for (Node node1 : nodeList) {

                childNodeNames.add(node1.getNodeName());

            }

            if (childNodeNames.contains("mml:math")){

                MmlMath mmlMath = new MmlMath(nodeList.stream().filter(node1 -> node1.getNodeName().equals("mml:math")).findFirst().get());

                this.html += mmlMath.element();

            } else {
                for (Node node1 : nodeList) {

                    if (tagNames.contains(node1.getNodeName().replace(":","-"))) {

                        String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                        if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                            break;
                        }
                    } else if (!node1.getNodeName().equals("#text")){

                        this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                    }

                }
            }


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
