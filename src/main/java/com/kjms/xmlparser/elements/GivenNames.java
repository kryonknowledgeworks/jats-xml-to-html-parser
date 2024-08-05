package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/given-names.html
public class GivenNames implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_GIVEN_NAME_FULL = "<given-names>";
    public static String ELEMENT_GIVEN_NAME = "given-names";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";
    public GivenNames(Node node) {
        try{
            this.node = node;
            this.nodeList = Util.getChildNode(node);

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }
            this.html+= "<span> " + node.getTextContent() + "</span>";
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
