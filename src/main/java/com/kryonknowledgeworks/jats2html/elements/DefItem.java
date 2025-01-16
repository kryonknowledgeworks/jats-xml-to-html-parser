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

public class DefItem implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_EXTLINK_FULL = "<def-item>";
    public static String ELEMENT_EXTLINK = "def-item";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String defItemHtml = "";

    public DefItem(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;
            nodeList = Util.getChildNode(node);

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

            for (Node nodeListData : nodeList) {
                if (nodeListData.getNodeName().equals("term")) {
                    Term term = new Term(nodeListData, metaDataBuilder);
                    defItemHtml += "<div style= display:flex><div  style=\"width:20%;\">"+term.element()+"</div>";
                } else if (nodeListData.getNodeName().equals("def")) {
                    Def def = new Def(nodeListData, metaDataBuilder);
                    defItemHtml += "<div>"+def.element()+"</div></div>";
                }
            }
            this.html += defItemHtml;
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
}
