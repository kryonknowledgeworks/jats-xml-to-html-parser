package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/sec.html
public class Sec implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_SEC_FULL = "<sec>";
    public static String ELEMENT_SEC = "sec";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String sechtml = "";

    public Sec(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        elementFilter();
        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        String id = node.getAttributes().getNamedItem("id") != null ? node.getAttributes().getNamedItem("id").getNodeValue() : "";

        if (node.getParentNode().getNodeName().equals("sec") &&  node.getParentNode().getAttributes().getNamedItem("id") != null) {
            String parentId = node.getParentNode().getAttributes().getNamedItem("id") != null ? node.getParentNode().getAttributes().getNamedItem("id").getNodeValue() : "";
            this.html += "<div class='nav-data' data-type='child' data-parent-id='" + parentId + "' data-name='' order='2' id='" + id + "'>";
        } else if (!id.equals("")){
            this.html += "<div class='nav-data' data-name='' order='2' id='" + id + "'>";
        }
        String title = "";

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {

                    if (node1.getNodeName().equals("label")) {
                        continue;
                    }

                    if (node1.getNodeName().equals("title")) {

                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");

                        title = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");

                    } else {

                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");

                    }
                }
            } else if (!node1.getNodeName().equals("#text")) {


                this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
            }

        }
        title = title.replace("<h4><span class='label'>", "").replace("</span>", "").replace("</h4>", "");

        this.html = this.html.replace("data-name=''", "data-name=\" " + title.replaceAll("<[^>]+>", "")  + "  \"  ");

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
