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

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/ref.html
public class Ref implements Tag {

    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_REF_FULL = "<ref>";
    public static String ELEMENT_REF_SEC = "ref";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public Ref(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            String refId = node.getAttributes().getNamedItem("id").getNodeValue();
            this.node = node;
            nodeList = Util.getChildNode(node);

            String id = node.getAttributes().getNamedItem("id").getNodeValue();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            this.html += "<tr id='" + id +"'>";

            boolean citationPresent = false;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        if (node1.getNodeName().equals("element-citation") ){
                            if (!citationPresent){
                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, refId, metaDataBuilder);
                                this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                                citationPresent = true;
                            }

                        } else if (node1.getNodeName().equals("nlm-citation")){
                            if (!citationPresent){
                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, refId, metaDataBuilder);
                                this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                                citationPresent = true;
                            }
                        }else if (node1.getNodeName().equals("mixed-citation")){
                            if (!citationPresent){
                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                                this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                                citationPresent = true;
                            }
                        } else {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                            this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }

                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += Util.unParsedTagBuilder(node1);
                }
            }

            this.html += "</tr>";

//            Label label = new Label(Util.getCurrentNode(nodeList, Label.ELEMENT_LABEL));
//            List<Node> nlmCitationNode = Util.getCurrentNodes(nodeList, NlmCitation.ELEMENT_NLM_CITATION);
//            List<Node> mixedCitationNode = Util.getCurrentNodes(nodeList, MixedCitation.ELEMENT_MIXED_CITATION);
//            List<Node> elementCitationNode = Util.getCurrentNodes(nodeList, ElementCitation.ELEMENT_CITATION);
//            NlmCitation nlmCitation = null;
//            if (!nlmCitationNode.isEmpty()) {
//                nlmCitation = new NlmCitation(Util.getCurrentNode(nodeList, NlmCitation.ELEMENT_NLM_CITATION), label.element());
//                this.html += Util.divGenerator(refId, nlmCitation.element());
//            }
////            else if(!mixedCitationNode.isEmpty()&& elementCitationNode.isEmpty()) {
////
////                Node mixedCitationNodeData=Util.getCurrentNode(nodeList, ElementMixedCitation.ELEMENT_MIXED_CITATION);
////                nlmCitation = new NlmCitation(Util.getCurrentNode(nodeList, ElementMixedCitation.ELEMENT_MIXED_CITATION), label.element());
////            }
//            else {
//                MixedCitation mixedCitation=new MixedCitation(Util.getCurrentNode(nodeList, MixedCitation.ELEMENT_MIXED_CITATION));
//                this.html += Util.divGenerator(refId, Util.htmlTagBinder(P.ELEMENT_P, label.element() + " " + mixedCitation.element()));
//            }


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
