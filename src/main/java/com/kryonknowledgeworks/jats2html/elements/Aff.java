package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static com.kryonknowledgeworks.jats2html.util.Util.htmlTagBinder;
import static com.kryonknowledgeworks.jats2html.util.Util.htmlTagBinderWithId;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/aff.html
public class Aff implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_AFF_FULL = "<aff>";
    public static String ELEMENT_AFF = "aff";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";


    public Aff(Node node) {
        try {
            this.node = node;

            elementFilter();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;
            Node ridNode=  node.getAttributes().getNamedItem("id");
            String  id="";
            String  affNav = "";
            if(ridNode!=null) {
                id = node.getAttributes().getNamedItem("id").getNodeValue();
                id=id.substring(id.length() - 1);
                affNav = htmlTagBinderWithId("span","id","class='aff-serial'","aff_@_"+id,id);
            }

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

            Label label=null;
            if(nodeList.size()>0) {
                if(Util.getCurrentNode(this.nodeList, Label.ELEMENT_LABEL).getNodeName().equals("label"))
                    label = new Label(Util.getCurrentNode(this.nodeList, Label.ELEMENT_LABEL));
            }
            else
            {
                label=null;
            }

            if(nodeList.size()>0 && label!=null)

                this.html += Util.htmlTagBinder(Label.ELEMENT_HTML,  new StringBuffer(this.node.getTextContent()).deleteCharAt(0).toString());

            if(nodeList.size()>0 && label==null)
                this.html += Util.htmlTagBinderWithId(Label.ELEMENT_HTML, "id","class='mt-1'","", affNav+" "+ this.node.getTextContent());
            if (nodeList.size() == 0){
                this.html += Util.htmlTagBinder("div", node.getTextContent());
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


    public List<Node> elementFilter() {

        this.childNodes = this.node.getChildNodes();

        for (int i = 0; i < this.childNodes.getLength(); i++) {

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }

        }
        return nodeList;
    }
}
