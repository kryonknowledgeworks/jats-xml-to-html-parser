package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.kjms.xmlparser.util.Util.elementFilter;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/sub.html
public class Sub implements Tag {
    public static Boolean IMPLEMENT = true;
    public static String ELEMENT_SUB_FULL = "<sub>";
    public static String ELEMENT_SUB = "sub";

    public static String HTML_TAG = "sup";

    Node node = null;
    List<Node> nodeList;

    String html = "";

    public Sub(Node node) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        this.node = node;

        this.nodeList = elementFilter(node.getChildNodes());

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

        this.html += node.getFirstChild().getNodeValue();

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
