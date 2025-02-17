package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/contrib-id.html
public class ContribId implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_CONTRIB_ID_FULL = "<contrib-id>";
    public static String ELEMENT_CONTRIB_ID = "contrib-id";

    public static String HTML_TAG = "sup";

    public static String HTML_ID_TAG = "span";

    public static String HTML_ID_value = " Id ";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    public ContribId(Node node, MetaDataBuilder metaDataBuilder) {
        try {

            this.node = node;

            this.nodeList = Util.elementFilter(node.getChildNodes());

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

            if (node.getFirstChild().getNodeValue()!=null && !node.getFirstChild().getNodeValue().trim().isEmpty() && !node.getParentNode().getNodeName().equals("contrib")) {

                this.html += Util.htmlHrefBinder(node.getFirstChild().getNodeValue(), ContribId.HTML_ID_TAG, ContribId.HTML_ID_value);

            }else{

                Element e = (Element) node;
                if(e.hasAttribute("contrib-id-type") && e.getAttribute("contrib-id-type").equals("orcid")&&e.getFirstChild().getNodeName().equals("#text")){
                    this.html +="<a href=\""+e.getFirstChild().getNodeValue()+"\">";
                  if (e.hasAttribute("authenticated") && e.getAttribute("authenticated").equals("true")){
                     this.html +="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 123.02 123.02\"  width=\"16\" height=\"16\" ><defs><style>.cls-1{fill:#a5ce39;}.cls-2{fill:#fff;}</style></defs><title>Asset 3</title><g id=\"Layer_2\" data-name=\"Layer 2\"><g id=\"Layer_1-2\" data-name=\"Layer 1\"><circle class=\"cls-1\" cx=\"61.51\" cy=\"61.51\" r=\"61.51\"/><path class=\"cls-2\" d=\"M33.14,30.66a4.87,4.87,0,0,1,9.74,0,4.87,4.87,0,0,1-9.74,0Z\"/><rect class=\"cls-2\" x=\"33.7\" y=\"40.12\" width=\"8.62\" height=\"49.12\"/><path class=\"cls-2\" d=\"M96.24,64.67c0,13.9-10.06,24.56-28.69,24.56H52V40.12H67.55C86.18,40.12,96.24,50.85,96.24,64.67Zm-7.94,0c0-11-7.7-18.17-20.75-18.17h-7.7V82.85h7.7C80.6,82.85,88.3,75.69,88.3,64.67Z\"/></g></g></svg></a>";
                  }else{
                      this.html +="<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 123.02 123.02\" width=\"16\" height=\"16\" ><defs><style>.cls-1{fill:#7fab40;}</style></defs><title>Asset 1</title><g id=\"Layer_2\" data-name=\"Layer 2\"><g id=\"Layer_1-2\" data-name=\"Layer 1\"><path class=\"cls-1\" d=\"M61.51,0A61.51,61.51,0,1,0,123,61.51,61.51,61.51,0,0,0,61.51,0Zm0,114.43a52.92,52.92,0,1,1,52.92-52.92A52.92,52.92,0,0,1,61.51,114.43Z\"/><path class=\"cls-1\" d=\"M36.88,32.72a4.32,4.32,0,0,1,8.63,0,4.32,4.32,0,1,1-8.63,0Z\"/><rect class=\"cls-1\" x=\"37.38\" y=\"41.1\" width=\"7.63\" height=\"43.51\"/><path class=\"cls-1\" d=\"M92.77,62.86c0,12.3-8.91,21.75-25.41,21.75H53.58V41.1H67.36C83.86,41.1,92.77,50.61,92.77,62.86Zm-7,0c0-9.76-6.82-16.1-18.38-16.1H60.54V79h6.82C78.92,79,85.74,72.61,85.74,62.86Z\"/></g></g></svg></a> <text font-size=\"20\" fill=\"blue\">(unauthenticated)</text>";

                  }
                }

            }
        }catch (Exception e)
        {
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
}
