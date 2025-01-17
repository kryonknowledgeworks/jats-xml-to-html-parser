package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class  JournalTitle implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<journal-title>";
    public static String ELEMENT_LABEL = "journal-title";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public JournalTitle(Node node, MetaDataBuilder metaDataBuilder) {
//        try {
//
//            this.node = node;
//            elementFilter();
//
//            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;
//
//            for (Node node1 : nodeList) {
//
//                if (tagNames.contains(node1.getNodeName())) {
//
//                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
//                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
//
//                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
//                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
//                    }
//                } else if (!node1.getNodeName().equals("#text")){
//
//                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
//                }
//
//            }
//
//            this.html += "<h1 s>"+ node.getFirstChild().getNodeValue() +"</h1><hr>";
//
//        } catch (Exception e) {
//            HandleException.processException(e);
//        }

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
