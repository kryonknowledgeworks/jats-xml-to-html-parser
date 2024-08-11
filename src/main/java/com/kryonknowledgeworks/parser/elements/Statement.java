package com.kryonknowledgeworks.parser.elements;

import com.kryonknowledgeworks.parser.Tag;
import com.kryonknowledgeworks.parser.util.ClassNameSingleTon;
import com.kryonknowledgeworks.parser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Statement implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_SEC_FULL = "<statement>";
    public static String ELEMENT_SEC = "statement";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Statement(Node node) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        this.nodeList = Util.getChildNode(node);

        if(node.getAttributes().getNamedItem("content-type").getNodeValue().equals("theorem") || node.getAttributes().getNamedItem("content-type").getNodeValue().equals("lemma")|| node.getAttributes().getNamedItem("content-type").getNodeValue().equals("definition")|| node.getAttributes().getNamedItem("content-type").getNodeValue().equals("proposition") ){
            this.html += "<div class='statement' "+ node.getAttributes().getNamedItem("id") +">";
        }else{
            this.html += "<div class='statement'>";
        }

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

        this.html += "</div>";
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
