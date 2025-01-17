package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/nlm-citation.html
public class NlmCitation implements Tag {

    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_NLM_CITATION_FULL = "<nlm-citation>";
    public static String ELEMENT_NLM_CITATION = "nlm-citation";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String commentHtml="";

    String label="";



    public NlmCitation(Node node,String label, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        Node paragraph = node.getFirstChild();

        this.html += "<td><p>";

        if (paragraph.getNodeValue() != null){
            this.html += paragraph.getNodeValue();
        } else {

            if (tagNames.contains(paragraph.getNodeName())) {

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
