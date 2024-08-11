package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Fn implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<fn>";
    public static String ELEMENT = "fn";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String fnHtml="";

    public Fn(Node node) {

        try{
            this.node = node;
            nodeList= Util.getChildNode(node);
            String parentNode = node.getParentNode().getNodeName();
            for(Node pNode:nodeList) {
                if (pNode.getNodeName().equals("p") && !parentNode.equals("fn-group")) {
                    fnHtml += Util.getNestedNodeValue(pNode);
                } else {
                    String className = ClassNameSingleTon.tagToClassName(pNode.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, pNode);
                        fnHtml += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element").replace("<p>","<span>").replace("</p>","</span>");
                    }
                }
            }
              if(parentNode.equals("fn-group")){
                  this.html = "<p class='mb-0' "+ ((node.getAttributes().getNamedItem("id") == null)?"":node.getAttributes().getNamedItem("id")) + " >"+ fnHtml + "</p>";
              }else{
                  this.html = fnHtml;
              }

        }catch(Exception e){
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
