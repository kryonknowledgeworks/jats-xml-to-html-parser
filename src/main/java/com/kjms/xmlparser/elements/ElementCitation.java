package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ElementCitation implements Tag {

    public static Boolean IMPLEMENT = false;
    public static String ELEMENT_CITATION_FULL = "<element-citation>";
    public static String ELEMENT_CITATION = "element-citation";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    String nameHtml="";

    public ElementCitation(Node node, String label) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;
        elementFilter();
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
        List<Node> nameList = Util.getCurrentNodes(this.nodeList, Name.ELEMENT_NAME);
        for (Node nodeData : nameList) {
            Name name = new Name(nodeData);
            nameHtml += name.element();
        }
        ArticleTitle articleTitle = new ArticleTitle(Util.getCurrentNode(this.nodeList, ArticleTitle.ELEMENT_ELEMENT_ARTICLE_TITLE));
        Source source = new Source(Util.getCurrentNode(this.nodeList, Source.ELEMENT_SEC));
        Year year = new Year(Util.getCurrentNode(this.nodeList, Year.ELEMENT));
        Volume volume = new Volume(Util.getCurrentNode(this.nodeList, Volume.ELEMENT_VOLUME));
        Fpage fpage = new Fpage(Util.getCurrentNode(this.nodeList, Fpage.ELEMENT_FPAGE));
        Lpage lpage = new Lpage(Util.getCurrentNode(this.nodeList, Lpage.ELEMENT_LPAGE));
        String finalData =label+"  "+nameHtml + "   " + articleTitle.element() + "  " + source.element() + "  " + year.element() + "  " + volume.element() + "  " + fpage.element() + "  " + lpage.element();
        this.html+=Util.htmlTagBinder(P.ELEMENT_P,Util.getHtmlEscapeData(finalData));

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
