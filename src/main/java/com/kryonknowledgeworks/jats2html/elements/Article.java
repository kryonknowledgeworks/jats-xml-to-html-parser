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

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/article.html
public class Article implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_ARTICLE_FULL = "<article>";
    public static String ELEMENT_ARTICLE = "article";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";


    public Article(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        elementFilter();

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
        this.html = String.format("<body>" +
                "<section><div class='container-fluid container-xl mt-5 kryon-xml-parser'>" +
                "<div class=\"row\">" +
                "   <div class=\"col-4\">" +
                "       <div class=\"navigation me-5\" id=\"navigation\">\n" +
                "                       <ul id=\"mainList\">\n" +
                "                       </ul>\n" +
                "                  <ul class=\"bottom-nav\" id=\"bottomList\">\n" +
                "                  </ul>\n" +
                "        </div>" +
                "     </div>" +
                "     <div class=\"col-8\"> %s </div>" +
                "</div></div></section></body>", this.html);

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
