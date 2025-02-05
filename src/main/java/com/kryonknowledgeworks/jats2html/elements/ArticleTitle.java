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

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/article-title.html
public class ArticleTitle implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_ELEMENT_ARTICLE_TITLE_FULL = "<article-title>";
    public static String ELEMENT_ELEMENT_ARTICLE_TITLE = "article-title";

    public static String HTML_TAG = "<title>";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public ArticleTitle(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        this.nodeList = Util.getChildNode(node);

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

        if (node.getParentNode().getNodeName().equals("nlm-citation")||node.getParentNode().getNodeName().equals("element-citation")){
            this.html+= Util.htmlTagBinder("span",Util.getHtmlEscapeData(node.getTextContent()));
        } else {
            this.html+= Util.htmlTagBinder("h1",Util.getHtmlEscapeData(node.getTextContent()));
            this.html+= "<hr>";
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
