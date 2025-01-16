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

public class LicenseP implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<license-p>";
    public static String ELEMENT_LABEL = "license-p";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public LicenseP(Node node, MetaDataBuilder metaDataBuilder) {
        try {

            this.node = node;
            elementFilter();

            this.html += "<p>";

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            Node paragraph = node.getFirstChild();

            if (paragraph.getNodeValue() != null){

                this.html += paragraph.getNodeValue();
            } else {

                if (tagNames.contains(paragraph.getNodeName())) {

                    if (this.html.endsWith("</div>") || this.html.endsWith("</div>")){
                        this.html += "<p>";
                    }

                    String className = ClassNameSingleTon.tagToClassName(paragraph.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, paragraph, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!paragraph.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(paragraph).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }
            Node sibling = paragraph.getNextSibling();

            while (sibling != null){

                if (sibling.getNodeName().equals("#text")){
                    this.html += sibling.getNodeValue();
                }

                if (tagNames.contains(sibling.getNodeName())) {

                    if (this.html.endsWith("</div>") || this.html.endsWith("</p>")){
                        this.html += "<p>";
                    }

                    String className = ClassNameSingleTon.tagToClassName(sibling.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, sibling, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!sibling.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(sibling).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

                sibling = sibling.getNextSibling();

            }

            if (!node.getParentNode().getNodeName().equals("fig") && !node.getParentNode().getNodeName().equals("caption") && !node.getParentNode().getNodeName().equals("list-item")){
                this.html += "</p>";
            }

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