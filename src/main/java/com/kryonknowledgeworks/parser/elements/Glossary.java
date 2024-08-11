package com.kryonknowledgeworks.parser.elements;

import com.kryonknowledgeworks.parser.Exception.HandleException;
import com.kryonknowledgeworks.parser.Tag;
import com.kryonknowledgeworks.parser.util.ClassNameSingleTon;
import com.kryonknowledgeworks.parser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class Glossary implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<glossary>";
    public static String ELEMENT = "glossary";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList;

    String html = "";

    String titleHtml="";

    String defListHtml="";

    public Glossary(Node node) {
        try{
            this.node = node;
            nodeList= Util.getChildNode(node);

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

            for(Node data:nodeList)
            {
                if(data.getNodeName().equals("title"))
                {
                    titleHtml="<h3>"+data.getTextContent()+"</h3>";
                }
                else if(data.getNodeName().equals("def-list"))
                {
                    DefList defList=new DefList(data);
                    defListHtml=defList.element();
                }
            }
            this.html+=titleHtml+defListHtml;
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
