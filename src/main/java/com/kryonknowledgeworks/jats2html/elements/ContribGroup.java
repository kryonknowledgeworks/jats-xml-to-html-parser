package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;

import java.util.*;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/contrib-group.html
public class ContribGroup implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_CONTRIB_GROUP_FULL = "<contrib-group>";
    public static String ELEMENT_CONTRIB_GROUP = "contrib-group";

    public static String HTML_TAG = "h3";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    public ContribGroup(Node node) {
        try {
            this.node = node;

            this.nodeList = Util.elementFilter(node.getChildNodes());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            Map<String, String> map = new HashMap<>();

            String elementHtml = "";

            String emailElementHtml = "";

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        String element = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        if(node1.getNodeName().equals("contrib")){

                            String type = node1.getAttributes().getNamedItem("contrib-type").getNodeValue();

                             if (!map.containsKey(type)){
                                map.put(type, element);
                            } else {
                                map.put(type, map.get(type) + ", " +element);
                            }

                            Email email = new Email(node1);

                            emailElementHtml += email.element();

                        } else {
                            elementHtml += element;
                        }

                    }
                } else if (!node1.getNodeName().equals("#text")){
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }
            }

            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.html += Util.htmlTagBinder("h4", ClassNameSingleTon.tagToClassName(entry.getKey()));
                this.html +=  "<div class='authors-block'>" + entry.getValue();
            }

            this.html += elementHtml;
            this.html += emailElementHtml + "</div>";



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


    public List<String> getAttributes(List<Node> currentNodes) {

        List<String> attributes = new ArrayList<>();

        for (Node node1 : currentNodes) {

            String contribType = node1.getAttributes().getNamedItem("contrib-type").getNodeValue();

            if (attributes.isEmpty()) {

                attributes.add(contribType);

            } else {

                if (!attributes.contains(contribType)) {

                    attributes.add(contribType);
                }

            }

        }

        return attributes;
    }
}
