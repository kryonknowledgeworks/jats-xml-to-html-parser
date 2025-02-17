package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/name.html
public class Name implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_NAME_FULL = "<name>";
    public static String ELEMENT_NAME = "name";

    public static String ELEMENT_HTML = "span";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    public Name(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        this.node = node;

        this.nodeList = Util.elementFilter(node.getChildNodes());

        this.html += " <span> ";



        if(node.getParentNode().getNodeName().equals("element-citation") && metaDataBuilder.build().containsKey("ordered-name")){
            nodeList.sort(Comparator.comparingInt(n -> {
                Object value =  ((Map<String, Object>) metaDataBuilder.build().get("ordered-name")).getOrDefault(n.getNodeName(), Integer.MAX_VALUE);
                try {
                    return (value instanceof Integer) ? (Integer) value : Integer.parseInt(value.toString());
                } catch (NumberFormatException e) {
                    return Integer.MAX_VALUE;
                }
            }));
        }



        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += Util.unParsedTagBuilder(node1);
                }
        }

        this.html += " </span> ";

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
