package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import static com.kryonknowledgeworks.jats2html.util.Util.htmlTagBinderWithId;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/contrib-group.html
public class ContribGroup implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_CONTRIB_GROUP_FULL = "<contrib-group>";
    public static String ELEMENT_CONTRIB_GROUP = "contrib-group";

    public static String HTML_TAG = "h3";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    public ContribGroup(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;

        this.nodeList = Util.elementFilter(node.getChildNodes());

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        Map<String, String> map = new HashMap<>();

        String elementHtml = "";
        String coAuthorName = "";
        String emailElementHtml = "";
        char c = '`';
        char[] symbols = {
                '\u2666',  '\u25CB', '\u25CF', '\u25A0', '\u25A1', '\u25C6', '\u25C7', '\u25B2', '\u25BC' // Shapes: ○ ● ■ □ ◆ ◇ ▲ ▼ ♦
        };
        int i = -1;
        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    String element = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    if(node1.getNodeName().equals("contrib")){

                        String type = node1.getAttributes().getNamedItem("contrib-type").getNodeValue();
                        Node correspNode = node1.getAttributes().getNamedItem("corresp");
                        Boolean authorType = Boolean.FALSE;
                        if (correspNode != null && "yes".equals(correspNode.getNodeValue())) {
                            authorType = Boolean.TRUE;
                            coAuthorName = Util.extractName(node1.getChildNodes());
                        }

                        Email email = new Email(node1, metaDataBuilder);

                        if(!email.element().isEmpty()){
                            String isComma = "";
                            if(element.contains("<sup class='sup-span' >")){
                                isComma+=",";
                            }
                            element+="<sup class='sup-span' >" + isComma;
                            if(authorType){
                                i++;
                                emailElementHtml += "<div class='mt-1'><span class='aff-serial' id='aff_@_CA"+symbols[i]+"'>"+symbols[i]+"</span> Corresponding author. Email:";
                                element+=htmlTagBinderWithId("a", "href","","#aff_@_CA"+symbols[i],""+symbols[i] )+"</sup>";
                            }else{
                                c++;
                                emailElementHtml += "<div class='mt-1'><span class='aff-serial' id='aff_@_A"+c+"'>"+(c)+"</span> Author. Email:";
                                element+=htmlTagBinderWithId("a", "href","","#aff_@_A"+c, ""+c)+"</sup>";
                            }
                            emailElementHtml += email.element()+"</div>";
                        }



                        if (!map.containsKey(type)){
                            map.put(type, element);
                        } else {
                            map.put(type, map.get(type) + ", " +element);
                        }




                    } else {
                        elementHtml += element;
                    }

                }
            } else if (!node1.getNodeName().equals("#text")){
                this.html += Util.unParsedTagBuilder(node1);
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.html+="<div id='author-contents' class='mb-3'>";
            this.html += Util.htmlTagBinder("h4", ClassNameSingleTon.tagToClassName(entry.getKey()));
            this.html +=  "<div class='authors-block'>" + entry.getValue();
        }



        this.html += elementHtml;
        this.html += emailElementHtml + "</div></div>";
        this.html += coAuthorName;

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
