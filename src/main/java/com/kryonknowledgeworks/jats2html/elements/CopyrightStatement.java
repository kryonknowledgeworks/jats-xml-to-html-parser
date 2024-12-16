package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;

import java.util.List;

public class CopyrightStatement implements Tag {

    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_COPYRIGHTSTATEMENT_FULL = "<copyright-statement>";
    public static String ELEMENT_COPYRIGHTSTATEMENT = "copyright-statement";

    Node node = null;
    List<Node> nodeList;

    String html = "";
    public CopyrightStatement(Node node) {
        try{
            this.node=node;
            this.nodeList = Util.elementFilter(node.getChildNodes());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                }

            }

            this.html += "<div class='mb-3' id='copyrights-content' ><h4>Copyrights</h4><p class='copyright-block'>" + (node.getFirstChild().getNodeValue() != null ? node.getFirstChild().getNodeValue() : "") + "</p></div>";


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
