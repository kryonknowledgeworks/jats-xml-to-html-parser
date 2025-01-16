package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Fig implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_FULL = "<fig>";
    public static String ELEMENT = "fig";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";


    public Fig(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;

            elementFilter();

            String id = node.getAttributes().getNamedItem("id").getNodeValue();

            this.html += "<div class='bottom-nav-data' data-head='Figures' data-name='' order='4' data-id='"+ id +"'>";

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            String graphicName = "";

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                        if (node1.getNodeName().equals("graphic")){
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, id, metaDataBuilder);
                            this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        } else {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                            graphicName += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");

                        }
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += Util.unParsedTagBuilder(node1);
                }

            }

            this.html += "<p class='fig-name'>";

            this.html += graphicName;

            this.html += "</p>";

            this.html += "</div>";

            this.html = this.html.replace("data-name=''", "data-name='" + Util.getHtmlEscapeData(graphicName.split("-")[0].replace("<span class='label'>", "").replace("<span>", "").replace("</span>","").trim()) + "'");

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

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }

        }
        return nodeList;
    }
}
