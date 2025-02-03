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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Th implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_LABEL_FULL = "<thead>";
    public static String ELEMENT_LABEL = "thead";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Th(Node node, MetaDataBuilder metaDataBuilder) {
        try {

            this.node = node;
            elementFilter();

            String tableTagPattern = "<th[^>]*>";
            Pattern pattern = Pattern.compile(tableTagPattern);
            Matcher matcher = pattern.matcher(Util.convertToString(node));

            if (matcher.find()) {
                this.html += matcher.group();
            }
            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            Node td = node.getFirstChild();

            NodeList childNodes = node.getChildNodes();
            if(td!=null){
            if (td.getNodeValue() != null){
                this.html += td.getNodeValue();
            }
            else {

                if (tagNames.contains(td.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(td.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, td, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!td.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(td).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }

            Node sibling = td.getNextSibling();

            while (sibling != null){

                if (sibling.getNodeName().equals("#text")){
                    this.html += sibling.getNodeValue();
                }

                if (tagNames.contains(sibling.getNodeName())) {

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
            }

            this.html += "</th>";

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
