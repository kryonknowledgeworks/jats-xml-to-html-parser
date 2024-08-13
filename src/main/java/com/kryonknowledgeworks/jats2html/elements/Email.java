package com.kryonknowledgeworks.jats2html.elements;


import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Email implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<email>";
    public static String ELEMENT_LABEL = "email";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Email(Node node) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        if (node.getNodeName().equals("contrib")){

            List<Node> childNodes = Util.getChildNode(node);

            for (Node node1 : childNodes){

                if (node1.getNodeName().equals("email")){

                    List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

                    for (Node node2 : Util.getChildNode(node1)) {
                        if (tagNames.contains(node2.getNodeName())) {

                            String className = ClassNameSingleTon.tagToClassName(node2.getNodeName());
                            if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node2);
                                this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                            }
                        } else if (!node2.getNodeName().equals("#text")){

                            this.html += "<pre style='color:red'>'''" + Util.convertToString(node2).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                        }

                    }

                    Node xref = Util.getCurrentNodeIsExists(childNodes, "xref");

                    String rid = xref.getAttributes().getNamedItem("rid").getNodeValue();
                    rid=rid.substring(rid.length() - 1);
                    this.html += Util.htmlTagBinder("div",  rid + " Corresponding author. Email: " + node1.getFirstChild().getNodeValue());


                }

            }


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
