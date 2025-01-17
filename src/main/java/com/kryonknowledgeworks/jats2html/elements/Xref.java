package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.kryonknowledgeworks.jats2html.util.Util.*;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/xref.html
public class Xref implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_XREF_FULL = "<xref>";
    public static String ELEMENT_XREF = "xref";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";
    public Xref(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node=node;
        this.nodeList = Util.getChildNode(node);

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        String attrName = node.getAttributes().getNamedItem("ref-type").getNodeValue();
        String textContent = "";

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className)) && !attrName.equals("table-fn")) {

                    ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                }else if(attrName.equals("table-fn")){
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    textContent += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!node1.getNodeName().equals("#text")) {

                this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<", "&lt;").replace(">", "&gt;") + "'''</pre>";
            }

        }

        if (node.getParentNode().getNodeName().equals("contrib")){
            NamedNodeMap map=node.getAttributes();
            Node ridNode=  map.getNamedItem("rid");
            String xlink="";
            String  rid="";
            if(ridNode!=null) {
                rid = node.getAttributes().getNamedItem("rid").getNodeValue();
                rid=rid.substring(rid.length() - 1);
                this.html += "<sup class='sup-span' >"+htmlTagBinderWithId("a", "href","","#aff_@_"+rid,rid + ",")+"</sup>";
            }
            this.nodeList = elementFilter(node.getChildNodes());
        } else {
            String refType = node.getAttributes().getNamedItem("ref-type").getNodeValue();
            if (refType.equals("table-fn")){
                String rid = node.getAttributes().getNamedItem("rid").getNodeValue();
                String value = rid.replaceAll("[^0-9]", "").trim();
                this.html += Util.htmlRefBinder(rid, textContent, refType);
            }else{
                String rid = node.getAttributes().getNamedItem("rid").getNodeValue();
                String value = rid.replaceAll("[^0-9]", "").trim();
                this.html += Util.htmlRefBinder(rid, node.getFirstChild().getNodeValue(), refType);
            }
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
