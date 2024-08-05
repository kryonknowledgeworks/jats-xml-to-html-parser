package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/label.html
public class Label implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<label>";
    public static String ELEMENT_LABEL = "label";

    public static String ELEMENT_HTML = "div";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Label(Node node) {
        try {
            this.node = node;
            this.nodeList = Util.getChildNode(node);

            String parentNode = node.getParentNode().getNodeName();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName()) && !parentNode.equals("fn")) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        ClassNameSingleTon.createInstanceFromClassName(className, node1);
                    }
                }
                else if (!node1.getNodeName().equals("#text") ) {
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                }
            }

            if (node.getParentNode().getNodeName().equals("fig") || node.getParentNode().getNodeName().equals("table-wrap")) {
                this.html += "<span class='label'>" + node.getTextContent() + " - " + " </span>";
            } else {
                this.html += "<span class='label'>" + node.getTextContent() + "</span>";
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
}
