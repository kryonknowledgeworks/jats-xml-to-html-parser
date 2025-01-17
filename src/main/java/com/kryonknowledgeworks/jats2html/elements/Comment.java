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

public class Comment implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_COMMENT_FULL = "<comment>";
    public static String ELEMENT_COMMENT = "comment";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html="";

    String extHtml="";

    public Comment(Node node, MetaDataBuilder metaDataBuilder) {
        try{
            this.node = node;
            this.html+=node.getTextContent();
            elementFilter();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += Util.unParsedTagBuilder(node1);
                }
            }
            List<Node> innerCommandList= Util.getChildNode(node);
            String extLinkval="";
            String textValue="";
            for(Node innerNode:innerCommandList) {
                if(innerNode.getNodeName().equals(ExtLink.ELEMENT_EXTLINK))
                {
                    ExtLink extLink = new ExtLink(innerNode, metaDataBuilder);
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
