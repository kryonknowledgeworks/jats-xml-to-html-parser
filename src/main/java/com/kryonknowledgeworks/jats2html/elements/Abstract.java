package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/abstract.html
public class Abstract implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_ABSTRACT_FULL = "<abstract>";
    public static String ELEMENT_ABSTRACT = "abstract";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList;

    String html = "";

    String abstractHtml = "";

    String secHtml = "";

    public Abstract(Node node) {
        try {
            this.node = node;
            nodeList = Util.getChildNode(node);

            this.html += "<div class='nav-data' data-name='Abstract' id='abstract' order='1'>";

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            boolean containTitle = false;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    if (node1.getNodeName().equals("title")){
                        containTitle = true;
                    }

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }

            if (!containTitle){
                this.html = "<h4>Abstract</h4>" + this.html;
            }

            this.html += "</div>";

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

    public List<Node> elementFilter() {

        this.childNodes = this.node.getChildNodes();

        for (int i = 0; i < this.childNodes.getLength(); i++) {

            this.childNodes.item(i);

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }
        }
        return nodeList;
    }
}
