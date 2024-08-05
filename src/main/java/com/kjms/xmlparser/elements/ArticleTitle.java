package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static com.kjms.xmlparser.util.Util.htmlTagBinder;

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

    public ArticleTitle(Node node) {
        try{
            this.node = node;
            this.nodeList = Util.getChildNode(node);

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        ClassNameSingleTon.createInstanceFromClassName(className, node1);
                    }
                } else if (!node1.getNodeName().equals("#text")){


                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }
            }

            if (node.getParentNode().getNodeName().equals("nlm-citation")){
                this.html+= htmlTagBinder("span",Util.getHtmlEscapeData(node.getTextContent()));
            } else {
//                this.html+= htmlTagBinder("h1",Util.getHtmlEscapeData(node.getTextContent()));
//                this.html+= "<hr>";
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
}
