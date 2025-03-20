package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import de.undercouch.citeproc.csl.CSLNameBuilder;
import de.undercouch.citeproc.csl.CSLType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
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

    public ElementCitation(Node node, String label, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {

        this.node = node;

        CSLItemData item = Util.parseElementCitation(node,node.getChildNodes()).build();

        this.html = "<td class='reference-content' >"+CSL.makeAdhocBibliography((String) metaDataBuilder.build().get("citationStyles"), item).makeString().replaceAll("<div class=\"csl-left-margin\">.*?</div>", "")+"</td>";

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

    public String getHTMLFromNode(String className,Node sortedNode,MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Object instance = ClassNameSingleTon.createInstanceFromClassName(className, sortedNode, metaDataBuilder);
        return ClassNameSingleTon.invokeMethod(instance, "element");
    }
}
