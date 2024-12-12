package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;

import java.util.List;

import static com.kryonknowledgeworks.jats2html.util.Util.*;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/kwd-group.html
public class KwdGroup implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_KWDGROUP_FULL = "<kwd-group>";
    public static String ELEMENT_KWDGROUP_KWD = "kwd-group";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    String kwdHtml = "";

    public KwdGroup(Node node) {
        try {
            this.node = node;
            this.nodeList = elementFilter(node.getChildNodes());

            boolean containTitle = false;

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    if (node1.getNodeName().equals("title")){
                        containTitle = true;
                    }

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }
            if (this.html != ""){
                this.html = "<div class='keywords-block mb-3'  id='keywords-content'>" + this.html.substring(0, this.html.length() - 2) + "." + "</div>";

            }

            if (!containTitle){
                this.html = "<h4>Keywords</h4>" + this.html;
            }

//            String heading = htmlTagBinder("h3", "KeyWords");
//            for (int i = 0; i < nodeList.size(); i++) {
//                if (nodeList.get(i).getNodeName().equals("kwd")) {
//                    Kwd kwd = new Kwd(nodeList.get(i));
//                    if (kwdHtml.equals(""))
//                        kwdHtml = kwdHtml + kwd.element();
//                    else
//                        kwdHtml = kwdHtml + ", " + kwd.element();
//                }
//                this.html = heading + htmlTagBinder("p", getHtmlEscapeData(kwdHtml) + ".");
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
}
