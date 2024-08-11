package com.kryonknowledgeworks.parser.elements;

import com.kryonknowledgeworks.parser.Tag;
import com.kryonknowledgeworks.parser.util.ClassNameSingleTon;
import com.kryonknowledgeworks.parser.util.Util;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/name.html
public class Name implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_NAME_FULL = "<name>";
    public static String ELEMENT_NAME = "name";

    public static String ELEMENT_HTML = "span";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    public Name(Node node) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        this.node = node;

        this.nodeList = Util.elementFilter(node.getChildNodes());

        this.html += "<span> ";

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                    this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                }
            } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }
        }

        this.html += " ,</span>";

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
