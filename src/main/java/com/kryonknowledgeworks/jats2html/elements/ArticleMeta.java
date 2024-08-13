package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/article-meta.html
public class ArticleMeta implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_ARTICLEMETA_FULL = "<article-meta>";
    public static String ELEMENT_ARTICLEMETA = "article-meta";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList;

    String html = "";

    public ArticleMeta(Node node) {
        try {

            this.node = node;

            this.nodeList = Util.elementFilter(node.getChildNodes());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            String doi = "";

            String abstractData = "";

            String remaining = "";

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        if (node1.getNodeName().equals("article-id")){
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            doi = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        } else if(node1.getNodeName().equals("abstract")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            abstractData = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        } else {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            remaining += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }
            this.html = abstractData + "<div class='card'><div class='card-body'>" + remaining + doi + "</div></div>";

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
        return this.node.hasChildNodes();
    }
}
