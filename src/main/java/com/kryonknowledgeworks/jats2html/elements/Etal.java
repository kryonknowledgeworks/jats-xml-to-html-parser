package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Etal implements Tag {
    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<etal>";
    public static String ELEMENT_LABEL = "etal";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Etal(Node node, MetaDataBuilder metaDataBuilder) {
        this.node = node;
        elementFilter();
        this.html = "et al.";

//            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

//            for (Node node1 : nodeList) {
//
//                if (tagNames.contains(node1.getNodeName())) {
//
//                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
//                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
//
////                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
////                        this.html = "<hr/>";
//                    }
//                } else if (!node1.getNodeName().equals("#text")){
//
//                    this.html = "<hr/>";
//
////                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
//                }
//            }


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
