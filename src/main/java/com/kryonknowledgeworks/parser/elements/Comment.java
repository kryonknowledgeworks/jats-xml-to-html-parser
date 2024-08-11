package com.kryonknowledgeworks.parser.elements;

import com.kryonknowledgeworks.parser.Exception.HandleException;
import com.kryonknowledgeworks.parser.Tag;
import com.kryonknowledgeworks.parser.util.ClassNameSingleTon;
import com.kryonknowledgeworks.parser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Comment implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_COMMENT_FULL = "<comment>";
    public static String ELEMENT_COMMENT = "comment";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html="";

    String extHtml="";

    public Comment(Node node) {
        try{
            this.node = node;
            this.html+=node.getTextContent();
            elementFilter();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        ClassNameSingleTon.createInstanceFromClassName(className, node1);
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }
            }
            List<Node> innerCommandList= Util.getChildNode(node);
            String extLinkval="";
            String textValue="";
            for(Node innerNode:innerCommandList) {
                if(innerNode.getNodeName().equals(ExtLink.ELEMENT_EXTLINK))
                {
                    ExtLink extLink = new ExtLink(innerNode);
                    extLinkval=extLink.element();
                }
                else
                {
                    textValue=innerNode.getTextContent();
                }
            }

            this.html+= extLinkval;
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

            this.childNodes.item(i);

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }
        }
        return nodeList;
    }
}
