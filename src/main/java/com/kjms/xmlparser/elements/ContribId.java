package com.kjms.xmlparser.elements;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.Tag;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;

import java.util.List;

import static com.kjms.xmlparser.util.Util.elementFilter;
import static com.kjms.xmlparser.util.Util.htmlHrefBinder;

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

    public ContribId(Node node) {
        try {

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

            if (node.getFirstChild().getNodeValue()!=null && !node.getFirstChild().getNodeValue().trim().isEmpty()) {

                this.html += htmlHrefBinder(node.getFirstChild().getNodeValue(), ContribId.HTML_ID_TAG, ContribId.HTML_ID_value);

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
