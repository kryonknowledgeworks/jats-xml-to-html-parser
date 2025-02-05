package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ElementCitation implements Tag {

    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_CITATION_FULL = "<element-citation>";
    public static String ELEMENT_CITATION = "element-citation";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String nameHtml="";

    public ElementCitation(Node node, String label, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        List<Node> nodeList = new ArrayList<>();
        Node child = node.getFirstChild();

        // Collect all child nodes
        while (child != null) {
            nodeList.add(child);
            child = child.getNextSibling();
        }

        // Sort nodes based on tagOrderMap values (position)
        nodeList.sort(Comparator.comparingInt(n -> {
            Object value = metaDataBuilder.build().getOrDefault(n.getNodeName(), Integer.MAX_VALUE);
            try {
                return (value instanceof Integer) ? (Integer) value : Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                return Integer.MAX_VALUE;
            }
        }));

        // Generate HTML
        this.html += "<td><p>";

        for (Node sortedNode : nodeList) {
            if (sortedNode.getNodeName().equals("#text")) {
                this.html += sortedNode.getNodeValue();
            } else if (tagNames.contains(sortedNode.getNodeName())) {
                String className = ClassNameSingleTon.tagToClassName(sortedNode.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instance = ClassNameSingleTon.createInstanceFromClassName(className, sortedNode, metaDataBuilder);
                    this.html += ClassNameSingleTon.invokeMethod(instance, "element");
                }
            } else {
                this.html += "<pre style='color:red'>'''" + Util.convertToString(sortedNode).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
            }
        }

        this.html += "</p></td>";
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
