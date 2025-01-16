package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/bold.html
public class Speaker implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<speaker>";
    public static String ELEMENT = "speaker";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Speaker(Node node, MetaDataBuilder metaDataBuilder) {
        try {

            this.node = node;
            elementFilter();


            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            this.html +="<span><b>"+ node.getTextContent()+  "</b></span>";


//            for (Node node1 : nodeList) {
//
//                if (tagNames.contains(node1.getNodeName())) {
//
//                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
//                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
//                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
//                        this.html += node1.getTextContent();
//                    }
//                } else if (!node1.getNodeName().equals("#text")){
//                    this.html += node1.getTextContent();
////                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
//                }
//
//            }


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
