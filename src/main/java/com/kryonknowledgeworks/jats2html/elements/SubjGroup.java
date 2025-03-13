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

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/title-group.html
public class SubjGroup implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_TITLE_GROUP_FULL = "<subj-group>";
    public static String ELEMENT_TITLE_GROUP = "subj-group";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";

    public SubjGroup(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;

            this.nodeList = Util.elementFilter(node.getChildNodes());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;



            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
                }

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
        return this.node.hasChildNodes();
    }

    public Node getNodeWithAttribute(List<Node> nodeList, String element, String attribute) {
        if(nodeList.size()>0) {
            for (Node node : nodeList) {

                if (node.getNodeName().equals(element)) {

                    if (node.getAttributes().getNamedItem("pub-type") != null && node.getAttributes().getNamedItem("pub-type").getNodeValue().equals(attribute)){
                        return node;
                    }
                }

            }
        }
        return (nodeList.size()>0)?nodeList.get(0):null;
    }
}
