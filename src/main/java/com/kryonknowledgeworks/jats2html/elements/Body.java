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

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/body.html
public class Body implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_BODY_FULL = "<body>";
    public static String ELEMENT_BODY = "body";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";

    String bodyHtml = "";

    static String thHtml = "";

    static String tdHtml = "";

    String tableContent="";

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

    public Body(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        this.node = node;
        elementFilter();

        this.html += "<div class='body-content'>";

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!node1.getNodeName().equals("#text")){


                this.html += Util.unParsedTagBuilder(node1);
            }

        }

        this.html += "</div>";


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

    public static void htmlTableContent(Node tableNode) {
        if (tableNode.getNodeName().equals("tr")) {
            thHtml += "</tr><tr>";
            tdHtml += "</tr><tr>";
        }
        if (tableNode.getNodeName().equals("th")) {
            if (tableNode.hasAttributes()) {
                String align = tableNode.getAttributes().getNamedItem("align").getNodeValue();
                String valign = tableNode.getAttributes().getNamedItem("valign").getNodeValue();
                thHtml += "<th align=" + align + " valign=" + valign + ">" + Util.getHtmlEscapeData(tableNode.getTextContent()) + "</th>";
            }
        } else if (tableNode.getNodeName().equals("td")) {
            String align = tableNode.getAttributes().getNamedItem("align").getNodeValue();
            String valign = tableNode.getAttributes().getNamedItem("valign").getNodeValue();
            tdHtml += "<td align=" + align + " valign=" + valign + ">" + tableNode.getTextContent() + "</td>";
        }
    }

}
