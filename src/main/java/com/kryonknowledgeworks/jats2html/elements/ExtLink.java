package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/ext-link.html
public class ExtLink implements Tag {
    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_EXTLINK_FULL = "<ext-link>";
    public static String ELEMENT_EXTLINK = "ext-link";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html="";

    public ExtLink(Node node) {
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
                } else if (!node1.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                }

            }

            String href="";
            if(node.getNodeName().equals(ExtLink.ELEMENT_EXTLINK)) {
                if (node.getAttributes().getNamedItem("xlink:href") != null){
                    href = node.getAttributes().getNamedItem("xlink:href").getNodeValue();
                } else if(node.getAttributes().getNamedItem("xlink-href") != null){
                    href = node.getAttributes().getNamedItem("xlink-href").getNodeValue();
                }
            }



//            this.html+=  Util.htmlHrefBinder(href,null,null);
            this.html += "<a target='_blank' href='"+ href + "'> View Article</a>";
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
