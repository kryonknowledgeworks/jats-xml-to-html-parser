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

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/ref-list.html
public class RefList implements Tag {

    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_REF_LIST_FULL = "<ref-list>";
    public static String ELEMENT_REF_LIST_SEC = "ref-list";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String refHtml = "";

    public RefList(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;
            elementFilter();

            this.html += "<div class='nav-data' data-name='Reference' order='3' id='Reference'>";

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += Util.unParsedTagBuilder(node1);
                }
            }

            Title title = new Title(Util.getCurrentNode(this.nodeList, Title.ELEMENT_TITLE), metaDataBuilder);
            List<Node> refList = Util.getCurrentNodes(this.nodeList, Ref.ELEMENT_REF_SEC);
            for (Node nodeData : refList) {
                Ref ref = new Ref(nodeData, metaDataBuilder);
                refHtml += ref.element();
            }

            this.html += title.element() + refHtml;
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
