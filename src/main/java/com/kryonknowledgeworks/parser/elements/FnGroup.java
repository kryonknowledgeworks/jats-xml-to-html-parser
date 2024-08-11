package com.kryonknowledgeworks.parser.elements;

import com.kryonknowledgeworks.parser.Tag;
import com.kryonknowledgeworks.parser.util.ClassNameSingleTon;
import com.kryonknowledgeworks.parser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FnGroup implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<fn-group>";
    public static String ELEMENT = "fn-group";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String fnHtml="";

    public FnGroup(Node node) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        Fn fn=null;
        this.nodeList= Util.getChildNode(node);

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

        for(Node fnGroupNode:nodeList)
        {
            if(fnGroupNode.getNodeName().equals("fn"))
            {
                fn=new Fn(fnGroupNode);
                fnHtml+= fn.element();
            }
        }

        this.html += fnHtml;

//        this.html=Util.htmlTagBinder("p",fnHtml);
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
