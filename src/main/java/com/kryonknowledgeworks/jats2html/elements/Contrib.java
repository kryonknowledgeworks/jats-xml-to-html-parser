package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/contrib.html
public class Contrib implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_CONTRIB_FULL = "<contrib>";
    public static String ELEMENT_CONTRIB = "contrib";

    Node node = null;
    List<Node> nodeList;

    String html= "";

    public Contrib(Node node, MetaDataBuilder metaDataBuilder) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.node = node;

        this.nodeList = Util.elementFilter(node.getChildNodes());

        List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

        String lastXrefNode = null;

        for (int i = 0; i < nodeList.size(); i++) {
            Node nodec = nodeList.get(i);
            if ("xref".equals(nodec.getNodeName())) {
                lastXrefNode =  nodec.getAttributes().getNamedItem("rid").getNodeValue();
            }
        }


        for (Node node1 : nodeList) {

            if (tagNames.contains(node1.getNodeName())) {

                String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                    Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1, metaDataBuilder);
                    if ("xref".equals(node1.getNodeName()) &&  node1.getAttributes().getNamedItem("rid").getNodeValue().equals(lastXrefNode)) {
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element").replaceFirst(",(?!.*,)", "");;
                    }else{
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                }
            } else if (!node1.getNodeName().equals("#text")){

                    this.html += Util.unParsedTagBuilder(node1);
                }

        }



//        Name name = new Name(Util.getCurrentNode(this.nodeList, Name.ELEMENT_NAME));
//
//        Xref xref = new Xref(Util.getCurrentNode(this.nodeList, Xref.ELEMENT_XREF));
//
//        ContribId contribId = new ContribId(Util.getCurrentNode(this.nodeList, ContribId.ELEMENT_CONTRIB_ID));

//        String contribIdHtml;
//        List<String> injectDatas;
//
//        if (contribId.element() != null) {
//
//            contribIdHtml = htmlTagBinder(ContribId.HTML_TAG, contribId.element());
//
//            injectDatas = Arrays.asList(xref.element(), contribIdHtml);
//
//        } else {
//
//            injectDatas = Collections.singletonList(xref.element());
//        }

//        this.html = htmlTagInject("</" + Name.ELEMENT_HTML + ">", injectDatas, name.element());
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
